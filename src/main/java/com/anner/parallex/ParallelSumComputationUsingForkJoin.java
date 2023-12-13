package com.anner.parallex;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author Anner
 * @since 11.0
 * Create on 2023/12/13
 */
public class ParallelSumComputationUsingForkJoin {
    private static final int[] LARGE_ARR = largeArr();

    private static final int LENGTH = LARGE_ARR.length;

    public static void main(String[] args) {
        RecursiveSumTask recursiveSumTask = new RecursiveSumTask(0,LENGTH,LARGE_ARR);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        long start = System.currentTimeMillis();
        long sum = forkJoinPool.invoke(recursiveSumTask);
        System.out.println("The sum is : "
                + sum
                + ", Time Taken by Parallel(Fork/Join) Execution: "
                + (System.currentTimeMillis() - start) + " millis");
    }

    private static  int[] largeArr() {
        return new Random().ints(900000000,10,1000).toArray();
    }

    static class RecursiveSumTask extends RecursiveTask<Long> {

        // 子任务计算的最小范围
        private static final int SEQUENTIAL_COMPUTE_QUEUE = 4000;

        private final int startIndex;

        private final int endIndex;

        private final int[] data;

        public RecursiveSumTask(int startIndex, int endIndex, int[] data) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.data = data;
        }

        @Override
        protected Long compute() {
            if (SEQUENTIAL_COMPUTE_QUEUE >= (endIndex -startIndex)) {
                long sum = 0;
                for (int i = startIndex; i <endIndex ; i++) {
                    sum += data[i];
                }
                return  sum;
            }

            // 分解任务
            int mid = startIndex + (endIndex - startIndex) / 2;
            RecursiveSumTask leftTask = new RecursiveSumTask(startIndex,mid,data);
            RecursiveSumTask rightTask = new RecursiveSumTask(mid,endIndex,data);
            leftTask.fork();

            long rightSum = rightTask.compute();
            long leftSum = leftTask.join();

            return  leftSum + rightSum;
        }
    }
}
