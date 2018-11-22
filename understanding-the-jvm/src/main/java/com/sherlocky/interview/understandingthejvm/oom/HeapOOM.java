package com.sherlocky.interview.understandingthejvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * java 堆内存溢出异常测试
 * 
 * VM Args: -verbose:gc -Xms20M -Xmx20M -XX:+HeapDumpOnOutOfMemoryError -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
        -verbose:gc 表示输出虚拟机中GC的详细情况
        -Xms20M -Xmx20M 将堆的最小值-Xms参数与最大值-Xmx参数设置一样，可避免堆自动扩展
        --XX:+HeapDumpOnOutOfMemoryError 表示当JVM发生OOM时，自动生成DUMP文件
        -XX:HeapDumpPath=${目录} 表示生成DUMP文件的路径，也可以指定文件名称
                            【可以使用Eclipse Memory Analyzer 打开堆转储快照文件(.hprof)，区分是内存泄露还是内存溢出】
        -Xmn10M 设置年轻代为10M
        -XX:NewRatio=4 表示年轻代（年轻代包括：Eden和两个Surivor）与年老代（年老代不包括持久代）的比值=1:4
        -XX:SurvivorRatio=8 表示年轻代中,Survivor与Eden的比值=1:8（这样在整个年轻代中，Surivivor 占10分之2）
 *
 *
 * @author zhangcx
 * @date 2018-10-23
 */
public class HeapOOM {

    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList();

        while (true) {
            list.add(new OOMObject());
        }
    }
}
/**
运行结果：
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid14708.hprof ...
Heap dump file created [28107585 bytes in 0.085 secs]
*/