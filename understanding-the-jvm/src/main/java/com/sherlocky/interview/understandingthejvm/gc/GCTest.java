package com.sherlocky.interview.understandingthejvm.gc;

/**
 * GC 测试
 *
 * <p>
 *     堆内存常见分配策略：
 * <ul>
 * <li>对象优先在eden区分配</li>
 * <li>大对象直接进入老年代</li>
 * <li>长期存活的对象将进入老年代</li>
 * </ul>
 * </p>
 *
 * <p>
 *     Minor GC和Full GC的不同
 * <ul>
 * <li>新生代GC（Minor GC）:指发生新生代的的垃圾收集动作，Minor GC非常频繁，回收速度一般也比较快。</li>
 * <li>老年代GC（Major GC/Full GC）:指发生在老年代的GC，出现了Major GC经常会伴随至少一次的Minor GC（并非绝对），Major GC的速度一般会比Minor GC的慢10倍以上。</li>
 * </ul>
 * </p>
 *
 *
 * VM Args: -XX:+PrintGCDetails
 *
 * @author: zhangcx
 * @date: 2019/3/7 13:54
 */
public class GCTest {
    public static void main(String[] args) {
        byte[] allocation1, allocation2;
        allocation1 = new byte[30900 * 1024];
        //allocation2 = new byte[20000 * 1024];
    }
}
/**运行结果：
 Heap
 PSYoungGen      total 57344K, used 35823K [0x0000000780400000, 0x0000000784400000, 0x00000007c0000000)
 eden space 49152K, 72% used [0x0000000780400000,0x00000007826fbea0,0x0000000783400000)
 from space 8192K, 0% used [0x0000000783c00000,0x0000000783c00000,0x0000000784400000)
 to   space 8192K, 0% used [0x0000000783400000,0x0000000783400000,0x0000000783c00000)
 ParOldGen       total 131072K, used 0K [0x0000000700c00000, 0x0000000708c00000, 0x0000000780400000)
 object space 131072K, 0% used [0x0000000700c00000,0x0000000700c00000,0x0000000708c00000)
 Metaspace       used 3215K, capacity 4496K, committed 4864K, reserved 1056768K
 class space    used 349K, capacity 388K, committed 512K, reserved 1048576K
*/
/** 取消 allocation2 初始化语句的注释后，运行结果：
 [GC (Allocation Failure) [PSYoungGen: 34840K->728K(57344K)] 34840K->31628K(188416K), 0.0135296 secs] [Times: user=0.00 sys=0.02, real=0.01 secs]
 Heap
 PSYoungGen      total 57344K, used 21219K [0x0000000780400000, 0x0000000787400000, 0x00000007c0000000)
 eden space 49152K, 41% used [0x0000000780400000,0x0000000781802f98,0x0000000783400000)
 from space 8192K, 8% used [0x0000000783400000,0x00000007834b6030,0x0000000783c00000)
 to   space 8192K, 0% used [0x0000000786c00000,0x0000000786c00000,0x0000000787400000)
 ParOldGen       total 131072K, used 30900K [0x0000000700c00000, 0x0000000708c00000, 0x0000000780400000)
 object space 131072K, 23% used [0x0000000700c00000,0x0000000702a2d010,0x0000000708c00000)
 Metaspace       used 3215K, capacity 4496K, committed 4864K, reserved 1056768K
 class space    used 349K, capacity 388K, committed 512K, reserved 1048576K
*/
/**
 简单解释一下为什么会出现这种情况：
 大多数情况下，对象在新生代中 eden 区分配。当 eden 区没有足够空间进行分配时，虚拟机将发起一次Minor GC.
 因为给allocation2分配内存的时候eden区内存几乎已经被分配完了，我们刚刚讲了当Eden区没有足够空间进行分配时，虚拟机将发起一次Minor GC.
 GC期间虚拟机又发现allocation1无法存入Survivor空间，所以只好通过【空间分配担保机制】把新生代的对象提前转移到老年代中去，
 老年代上的空间足够存放allocation1，所以不会出现Full GC。
 执行Minor GC后，后面分配的对象如果能够存在eden区的话，还是会在eden区分配内存。
 */
/**
虚拟机给每个对象一个对象年龄（Age）计数器。
    如果对象在 Eden 出生并经过第一次 Minor GC 后仍然能够存活，并且能被 Survivor 容纳的话，将被移动到 Survivor 空间中，
 并将对象年龄设为1.对象在 Survivor 中每熬过一次 MinorGC,年龄就增加1岁，当它的年龄增加到一定程度（默认为15岁），
 就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 -XX:MaxTenuringThreshold 来设置。

 为了更好的适应不同程序的内存情况，虚拟机不是永远要求对象年龄必须达到了某个值才能进入老年代，
 如果 Survivor 空间中相同年龄所有对象大小的总和大于 Survivor 空间的一半，
 年龄大于或等于该年龄的对象就可以直接进入老年代，无需达到要求的年龄。


 所有的 Minor GC 都会触发“全世界的暂停（stop-the-world）"

*/