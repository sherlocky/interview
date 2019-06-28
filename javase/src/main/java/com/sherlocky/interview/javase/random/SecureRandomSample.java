package com.sherlocky.interview.javase.random;

import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>在安全应用场景，随机数应该使用安全的随机数。密码学意义上的安全随机数，要求必须保证其不可预测性。所以在需要频繁生成随机数，或者安全要求较高的时候，不要使用Random，因为其生成的值其实是可以预测的。</p>
 *
 * <p>可以直接使用真随机数产生器产生的随机数。或者使用真随机数产生器产生的随机数做种子，输入密码学安全的伪随机数产生器产生密码学安全随机数。</p>
 *
 * - 非物理真随机数产生器有：
 * <ul>
 * <li>Linux操作系统的/dev/random设备接口</li>
 * <li>Windows操作系统的CryptGenRandom接口</li>
 * </ul>
 * - 密码学安全的伪随机数产生器,包括JDK的java.security.SecureRandom等。
 *
 * <p>如果不是安全场景的随机数，使用Random就好</p>
 *
 * @author: zhangcx
 * @date: 2019/5/24 10:38
 */
public class SecureRandomSample {

    public static void main(String[] args) {
        System.out.println("################## Random");
        testRandom();
        System.out.println("################## SecureRandom");
        testSecureRandom();
    }

    /**
     * Random类中实现的随机算法是伪随机，也就是有规则的随机。
     * 在进行随机时，随机算法的起源数字称为种子数(seed)，在种子数的基础上进行一定的变换，从而产生需要的随机数字。
     *
     * 相同种子数的Random对象，相同次数生成的随机数字是完全相同的。
     * 也就是说，两个种子数相同的Random对象，生成的随机数字完全相同
     */
    private static void testRandom() {
        /**
         * 【推荐】 避免 Random 实例被多线程使用，虽然共享该实例是线程安全的，但会因竞争同一 seed 导致的性能下降。
         *
         * 说明：Random 实例包括 java.util.Random 的实例或者 Math.random()的方式。
         *
         * 【正例】：在 JDK7 之后，可以直接使用 API ThreadLocalRandom，而在 JDK7 之前，需要编码保证每个线程持有一个实例。
         */
        Random random1 = new Random(10000);
        Random random2 = new Random(10000);

        for (int i = 0; i < 5; i++) {
            System.out.println(random1.nextInt() + " = " + random2.nextInt());
        }
    }

    public static void testSecureRandom() {
        // 无需手动设置种子
        SecureRandom random = new SecureRandom();
        // 要想保证得到安全的随机数，需要使用真随机数产生器产生的随机数做种子。
        // 如果指定了当前系统时间作为种子，替代系统默认随机源，在同一毫秒连续调用，则得到的随机数则是相同的。。
        /**
         * 而系统默认的随机源：
         * 取决于 $JAVA_HOME/jre/lib/security/java.security 配置中的
         * securerandom.source 属性，例如jdk1.8中该配置为：securerandom.source=file:/dev/random
         */
        // 使用无参构造函数实例化 SecureRandom，在大多数系统中，默认的算法是“nativePRNG”，从/dev/random获取随机数。
        // 如果系统出现了熵源不足，获取随机数阻塞的问题，可以做进一步优化

        // 1.nextInt 无参时范围：0 ~ 2^32
        System.out.println(random.nextInt());
        System.out.println(random.nextInt(100));;

        // 2.nextBytes
        byte[] bs = new byte[20];
        random.nextBytes(bs);
        for (byte b : bs) {
            System.out.print(b + " | ");
        }
        System.out.println();

        /**
         * 3.generateSeed
         * 可以使用generateSeed方法，来获取一个随机的byte数组，这个数组中的数通常可以用来做其他随机生成器的种子
         */
        byte[] seed = random.generateSeed(20);
        for (byte s : seed) {
            System.out.print(s + " | ");
        }
        System.out.println();

        // 4.SecureRandom.getSeed 静态方法
        byte[] seed2 = SecureRandom.getSeed(20);
        for (byte s : seed2) {
            System.out.print(s + " | ");
        }
        System.out.println();
    }
}