package com.sherlocky.interview.understandingthejvm.sof;

/**
 * 虚拟机栈和本地方法栈OOM测试
 * 
 * VM Args: -Xss128k
 * 
 * @author zhangcx
 * @date 2018-10-23
 */
public class JavaVMStackSOF {
    private int stackLength = 1;
    
    // 无出口的递归调用
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }
    
    public static void main(String[] args) throws Throwable {
        JavaVMStackSOF sof = new JavaVMStackSOF();
        try {
            sof.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length: " + sof.stackLength);
            throw e;
        }
    }
}
/**
运行结果：
stack length: 985
Exception in thread "main" java.lang.StackOverflowError
    at sof.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:16)
    at sof.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:16)
    at sof.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:16)
    ...
*/