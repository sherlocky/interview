package com.sherlocky.interview.multithread.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * <p>使用Fork/Join 框架求10亿内数字的总和</p>
 * <p>
 *     Fork/Join 框架是 JDK 1.7 提供的并行执行任务框架，这个框架通过（递归）把问题划分(Fork)为子任务，然后并行的执行这些子任务，
 *     等所有的子任务都结束的时候，再合并(Join)最终结果的这种方式来支持并行计算编程。
 *     总体思想和MapReduce很像，是一个单机版的MapReduce实现方式。
 * </p>
 * <p>
 *     ForkJoinTask是一个抽象类，创建一个ForkJoin的任务，不需要去继承ForkJoinTask进行使用。
 *     它比传统的任务更加轻量，不再是 Runnable 的子类，提供 Fork/Join 方法用于分割任务以及聚合结果。
 *
 *     ForkJoin框架为我们提供了RecursiveAction和RecursiveTask。
 *
 *     我们只需要继承ForkJoin为我们提供的抽象类的其中一个并且实现其compute方法即可。
 *
 *     RecursiveTask在进行exec之后会使用一个result的变量进行接受返回的结果。而result返回结果类型是通过泛型进行传入。也就是说RecursiveTask执行后是有返回结果。
 *     RecursiveAction在exec后是不会保存返回结果，因此RecursiveAction与RecursiveTask区别在与RecursiveTask是有返回结果而RecursiveAction是没有返回结果。
 * </p>
 * @author: zhangcx
 * @date: 2019/12/4 11:09
 * @since:
 */
public class ForkJoinDemo extends RecursiveTask<Long> {
    /**
     * 拆分子任务的临界值 10万
     */
    private static Long critical = 100_000L;
    private Long start;
    private Long end;

    public ForkJoinDemo(Long start, Long end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long len = end - start;
        if (len < critical) {
            long sum = 0;
            // 个数小于临界值，直接计算
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }
        // 个数超过临界值需要拆分子任务
        long middle = (start + end) / 2;
        ForkJoinDemo left = new ForkJoinDemo(start, middle);
        // fork() 做的工作只有一件事，既是把分支任务推入当前工作线程的工作队列里
        left.fork();
        ForkJoinDemo right = new ForkJoinDemo(middle + 1, end);
        right.fork();
        /**
         * join() 的工作则复杂得多，也是它可以使得线程免于被阻塞的原因。
         * 1.检查调用 join() 的线程是否是 ForkJoinThread 线程。如果不是（例如 main 线程），则阻塞当前线程，等待任务完成。如果是，则不阻塞。
         * 2.查看任务的完成状态，如果已经完成，直接返回结果。
         * 3.如果任务尚未完成，但处于自己的工作队列内，则完成它。
         * 4.如果任务已经被其他的工作线程偷走，则窃取这个小偷的工作队列内的任务（以 FIFO 方式）执行，以期帮助它早日完成预 join 的任务。
         * 5.如果偷走任务的小偷也已经把自己的任务全部做完，正在等待需要 Join 的任务时，则找到小偷的小偷，帮助它完成它的任务。
         * 6.递归地执行第 5 步。
         */
        return left.join() + right.join();
    }
}
