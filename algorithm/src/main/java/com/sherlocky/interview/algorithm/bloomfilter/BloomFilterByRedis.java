package com.sherlocky.interview.algorithm.bloomfilter;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.nio.charset.Charset;

/**
 * <p>布隆算法是一个以牺牲一定的准确率来换取低内存消耗的过滤算法，可以实现大量数据的过滤、去重等操作。</p>
 * <p>本例(参考了guava的一些实现)不讨论布隆算法的具体原理，可以查看:<a href="https://www.cnblogs.com/liyulong1982/p/6013002.html">布隆过滤器(Bloom Filter)详解</a></p>
 * <p>为了实现分布式过滤器，在这里使用了Redis，利用Redis的BitMap实现布隆过滤器的底层映射。</p>
 * <p>布隆过滤器的一个关键点就是如何根据预计插入量和可接受的错误率推导出合适的BIt数组长度和Hash函数个数，当然Hash函数的选取也能影响到过滤器的准确率和性能。</p>
 * <br>
 * <p>布隆过滤器中需要n个哈希函数，可以使用Arash Partow提供的<a href="http://www.partow.net/programming/hashfunctions/index.html">常见哈希函数</a>。</p>
 * <p>
 * 优点:
 * <li>存储空间和插入/查询时间都是常数，远远超过一般的算法</li>
 * <li>Hash函数相互之间没有关系，方便由硬件并行实现</li>
 * <li>不需要存储元素本身，在某些对保密要求非常严格的场合有优势</li>
 * 缺点:
 * <li>有一定的误识别率（但不会漏判）</li>
 * <li>删除困难</li>
 * </p>
 * <br>
 * <p>其实：redis官方在4.0+提供了插件化的布隆过滤器功能。布隆过滤器作为一个插件加载到 Redis Server 中，给 Redis 提供了强大的布隆去重功能。</p>
 * <li>bf.add 添加元素，一次只能添加一个</li>
 * <li>bf.madd 添加元素，一次添加多个</li>
 * <li>bf.exists 查询元素是否存在，它的用法和 set 集合的 sadd 和 sismember 差不多</li>
 * <li>bf.mexists 一次查询多个元素</li>
 * <br>
 * <p>很多工具类也都提供了布隆过滤器的实现，例如 guava的BloomFilter、hutool的BitMapBloomFilter 等等</p>
 * <br>
 * <p>布隆过滤的应用还是蛮多的，比如数据库、爬虫、防缓存击穿等。特别是需要精确知道某个数据不存在时做点什么事情就非常适合布隆过滤</p>
 * @author: zhangcx
 * @date: 2018/11/29 14:42
 */
public class BloomFilterByRedis {
    // 预计插入量
    private long expectedInsertions = 1000;
    // 可接受的错误率
    private double fpp = 0.001F;
    // 布隆过滤器的键在Redis中的前缀 利用它可以统计过滤器对Redis的使用情况
    private String redisKeyPrefix = "bloomfilter:";
    private Jedis jedis;

    public static void main(String[] args) {
        BloomFilterByRedis bf = new BloomFilterByRedis();
        bf.init();
        System.out.println(bf.isExist("blackUrl", "aaa"));
        System.out.println(bf.isExist("blackUrl", "ccc"));
        System.out.println(bf.isExist("blackUrl", "bbb"));
        System.out.println(bf.isExist("blackUrl", "aaa"));

        // bf.setExpectedInsertions() ...
        // jedis.
        // jedis.set(redisKeyPrefix + "test", "1111111111111");
        // System.out.println(jedis.get(redisKeyPrefix + "test"));
    }

    // bit数组长度
    private long numBits = optimalNumOfBits(expectedInsertions, fpp);
    // hash函数数量
    private int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
    // 计算 bit 数组合适的长度 方法来自 guava
    private long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
    // 计算 hash 函数合适的个数 方法来自 guava
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
    /**
     * 判断keys是否存在于集合where中
     */
    public boolean isExist(String where, String key) {
        long[] indexs = getIndexs(key);
        boolean result = false;
        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
        Pipeline pipeline = jedis.pipelined();
        try {
            for (long index : indexs) {
                pipeline.getbit(getRedisKey(where), index);
            }
            result = !pipeline.syncAndReturnAll().contains(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pipeline.close();
        }
        if (!result) {
            put(where, key);
        }
        return result;
    }
    /**
     * 将key存入redis bitmap
     */
    private void put(String where, String key) {
        long[] indexs = getIndexs(key);
        //这里使用了Redis管道来降低过滤器运行当中访问Redis次数 降低Redis并发量
        Pipeline pipeline = jedis.pipelined();
        try {
            for (long index : indexs) {
                pipeline.setbit(getRedisKey(where), index, true);
            }
            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pipeline.close();
        }
    }
    /**
     * 根据 key 获取 bitmap 下标 方法来自 guava
     */
    private long[] getIndexs(String key) {
        long hash1 = hash(key);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            long combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            result[i] = combinedHash % numBits;
        }
        return result;
    }
    /**
     * 获取一个hash值 方法来自guava
     */
    private long hash(String key) {
        Charset charset = Charset.forName("UTF-8");
        // 根据 murmur3_128 方法的到一个 128 位长度的 byte[]
        return Hashing.murmur3_128().hashObject(key, Funnels.stringFunnel(charset)).asLong();
    }

    private Jedis getJedis() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(200);
        config.setMaxTotal(1024);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        String host = "10.4.88.1", password = "lezhixing!@#$1qaz@WSX";
        int port = 6379, timeout = 3000;
        JedisPool jedisPool = new JedisPool(config, host, port, 3000, password);
        return  jedisPool.getResource();
    }

    private String getRedisKey(String where) {
        return redisKeyPrefix + where;
    }


    public void init(){
        this.jedis = this.getJedis();
    }

    public void setExpectedInsertions(long expectedInsertions) {
        this.expectedInsertions = expectedInsertions;
    }

    public void setFpp(double fpp) {
        this.fpp = fpp;
    }

    public void setRedisKeyPrefix(String redisKeyPrefix) {
        this.redisKeyPrefix = redisKeyPrefix;
    }
}
