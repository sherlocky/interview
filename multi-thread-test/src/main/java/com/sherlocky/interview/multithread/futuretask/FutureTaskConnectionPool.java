package com.sherlocky.interview.multithread.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 对比 {@link SimpleConnectionPool}
 * 结合Future模式的实现分析：
 * <p>当3个线程都要创建连接的时候，如果只有一个线程执行createConnection方法创建一个连接，其它2个线程只需要用这个连接就行了。
 * 再延伸，把createConnection方法放到一个Callable的call方法里面，然后生成FutureTask。
 * 我们只需要让一个线程执行FutureTask的run方法，其它的线程只执行get方法就好了</p>
 *
 * 参考：<a href="http://www.cnblogs.com/cz123/p/7693064.html">彻底理解Java的Future模式</a>
 * @author: zhangcx
 * @date: 2018/11/26 17:02
 */
public class FutureTaskConnectionPool {
    private ConcurrentHashMap<String, FutureTask<Connection>> pool = new ConcurrentHashMap<String, FutureTask<Connection>>();

    public Connection getConnection(String key) throws ExecutionException, InterruptedException {
        FutureTask<Connection> connectionTask = pool.get(key);
        if (connectionTask != null) {
            return connectionTask.get();
        }
        Callable<Connection> callable = new Callable<Connection>() {
            @Override
            public Connection call() throws Exception {
                return createConnection();
            }
        };
        FutureTask<Connection> newTask = new FutureTask<>(callable);
        // putIfAbsent: 如果传入key对应的value已经存在，就返回存在的value，不进行替换。
        // 如果不存在，就添加key和value，返回null
        connectionTask = pool.putIfAbsent(key, newTask);
        if (connectionTask == null) {
            connectionTask = newTask;
            connectionTask.run();
        }
        return connectionTask.get();
        /**
         *  推演一遍：
         *  当3个线程同时进入else语句块时，各自都创建了一个FutureTask，但是ConcurrentHashMap只会加入其中一个。
         *  第一个线程执行pool.putIfAbsent方法后返回null，然后connectionTask被赋值，接着就执行run方法去创建连接，最后get。
         *  后面的线程执行pool.putIfAbsent方法不会返回null，就只会执行get方法。
         *
         * 在并发的环境下，通过FutureTask作为中间转换，成功实现了让某个方法只被一个线程执行。
         */
    }

    public Connection createConnection() {
        return new Connection();
    }

    class Connection {
    }
}
