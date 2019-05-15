package com.sherlocky.interview.javase.interview.algorithm.snowflake;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <p>百度实现并开源的雪花算法版本：https://github.com/baidu/uid-generator</p>
 * <p>基于百度修改的雪花算法SpringBoot版本：https://gitee.com/sherlocky/springboot2-idgenerator</p>
 */

/**
 * 原始雪花算法简单描述： 
    + 最高位是符号位，始终为0，不可用。 
    + 41位的时间序列，精确到毫秒级，41位的长度可以使用69年。时间位还有一个很重要的作用是可以根据时间进行排序。 
    + 10位的机器标识，10位的长度最多支持部署1024个节点。 
    + 12位的计数序列号，序列号即一系列的自增id，可以支持同一节点同一毫秒生成多个ID序号，12位的计数序列号支持每个节点每毫秒产生4096个ID序号。
 */

/**
 * 雪花算法 （有小改动） 主键生成器
 * 可参考：https://blog.csdn.net/linghuanxu/article/details/78896317
 *
 * 整个ID的构成大概分为这么几个部分： 时间戳差值，机器编码，进程编码，序列号。 java的long是64位的，从左向右依次介绍是：
 * 时间戳差值，在我们这里占了42位（原算法占了41位）； 机器编码5位； 进程编码5位； 序列号12位。
 * 所有的拼接用位运算拼接起来，于是就基本做到了每个进程中不会重复了。
 *
 * 整体设计
 * 为了最大程度的减少配置，方便实用，这个模块，我设计成了单例模式。之所以没有直接使用static方法，还是希望可以控制整个模块的生命周期，但是，模块的初始化，我使用了static块，因为它没有任何依赖。
 * 有个static的nextId方法，可以直接获得下一个ID，这个方法是线程安全的。同时这个模块的使用就是这么简单粗暴，也不用配置bean。
 *
 * 其实，这个方案中，机器码和进程编码是可能相同的，只是概率比较小，就凑合着用吧。
 */
public class SnowflakeKeyWorker {
    /**
     * 起始的时间戳 作者写代码的时间戳（位数与实际时间戳不同，原始算法中时间戳占41位，该算法占42位。。）
     */
    private static final long TWEPOCH = 12888349746579L;

    // 机器标识位数
    private static final long WORKERIDBITS = 5L;
    // 数据中心标识位数
    private static final long DATACENTERIDBITS = 5L;

    // 毫秒内自增位数
    private static final long SEQUENCEBITS = 12L;
    // 机器ID偏左移12位
    private static final long WORKERIDSHIFT = SEQUENCEBITS;
    // 数据中心ID左移17位
    private static final long DATACENTERIDSHIFT = SEQUENCEBITS + WORKERIDBITS;
    // 时间毫秒左移22位
    private static final long TIMESTAMPLEFTSHIFT = SEQUENCEBITS + WORKERIDBITS + DATACENTERIDBITS;
    // sequence掩码，确保sequnce不会超出上限
    private static final long SEQUENCEMASK = -1L ^ (-1L << SEQUENCEBITS);
    // 上次时间戳
    private static long lastTimestamp = -1L;
    // 序列
    private long sequence = 0L;
    // 服务器ID
    private long workerId = 1L;

    /**
     * 如何确保不超出位数限制,我们拿workerId举个例子：this.workerId=workerId & workerMask;
     */
    /**
     * workerMask 是啥 ??? 它先执行的是-1L << workerIdBits，workerIdBits是5。这又是什么意思呢？
     * 注意，这是位运算，long用的是补码， -1L，就是64个1，这里使用-1是为了格式化所有位数 ===>
     * 11111...(省略54个1)...11111
     * <<是左移运算，-1L左移五位，低位补零，也就是左移空出来的会自动补0，于是就低位五位是0，其余是1。 ===>
     * 11111...(省略54个1)...00000
     * 然后^这个符号，是异或，也是位运算，位上相同则为0，不同则为1，和-1做异或，则把所有的0和1颠倒了一下。 ===>
     * 00000...(省略54个0)...11111
     * 
     * 这时候，我们再看，workerId & workerMask，与操作，两个位上都为1的才能唯一，否则为零，
     * workerMask高位都是0，所以，不管workerId高位是什么，都是0；
     * 而workerMask低位都是1，所以，不管workerId低位是什么，都会被保留，于是，我们就控制了workerId的范围。
     */
    private static long workerMask = -1L ^ (-1L << WORKERIDBITS);
    // 进程编码
    private long processId = 1L;
    private static long processMask = -1L ^ (-1L << DATACENTERIDBITS);
    private static SnowflakeKeyWorker keyWorker = null;

    static {
        keyWorker = new SnowflakeKeyWorker();
    }

    public static synchronized long nextId() {
        return keyWorker.getNextId();
    }

    private SnowflakeKeyWorker() {

        // 获取机器编码
        this.workerId = this.getMachineNum();
        // 获取进程编码
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        this.processId = Long.valueOf(runtimeMXBean.getName().split("@")[0]).longValue();

        // 避免编码超出最大值
        this.workerId = workerId & workerMask;
        this.processId = processId & processMask;
    }

    // 最后的异常
    /**
     * 这里，时间戳，保证了不通毫秒不同，然后机器编码进程编码保证了不同进程不通，
     * 再然后，序列，在同一毫秒内，如果获取第二个ID，则序列号+1，到下一毫秒后重置。至此，唯一性ok。
     * 
     * 但是，还有问题，序列号用完了怎么办？代码里的解决方案是，等到下一毫秒。
     */
    public synchronized long getNextId() {
        // 获取时间戳
        long timestamp = timeGen();
        // 如果时间戳小于上次时间戳则报错
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp)
                        + " milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果时间戳与上次时间戳相同
        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1，与sequenceMask确保sequence不会超出上限
            sequence = (sequence + 1) & SEQUENCEMASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID（原始算法中时间戳占41位，该算法占42位。。）
        return ((timestamp - TWEPOCH) << TIMESTAMPLEFTSHIFT) | (processId << DATACENTERIDSHIFT)
                | (workerId << WORKERIDSHIFT) | sequence;
    }

    /**
     * 再次获取时间戳直到获取的时间戳与现有的不同
     * 
     * @param lastTimestamp
     * @return 下一个时间戳
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取机器编码
     */
    private long getMachineNum() {
        long machinePiece;
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (e.hasMoreElements()) {
            NetworkInterface ni = e.nextElement();
            sb.append(ni.toString());
        }
        machinePiece = sb.toString().hashCode();
        return machinePiece;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        Long l1 = 12888349746579L;
        Long l2 = 1540779910617L;
        System.out.println(long2BinaryStr(l1));
        System.out.println(long2BinaryStr(l1 << 21));
        System.out.println(long2BinaryStr(l1 << 22));
        System.out.println(long2BinaryStr(l2));
        System.out.println(long2BinaryStr(l2 << 1));
        System.out.println(long2BinaryStr(l2 << 2));
        System.out.println(long2BinaryStr(l2 << 21));
        System.out.println(long2BinaryStr(l2 << 22));
    }

    // 长整型转二进制字符串（高位补0到64位）
    private static String long2BinaryStr(Long l) {
        return StringUtils.leftPad(Long.toBinaryString(l), 64, '0');
    }
}