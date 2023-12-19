package com.anner.parallex;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author Anner
 * @since 11.0
 * Create on 2023/12/19
 */
public class Fibonacci {


    private static class FibonacciTask extends RecursiveTask<Long> {

        private long n;

        public FibonacciTask(long n) {
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (n<=1) {
                return  n;
            }

            FibonacciTask sub1 = new FibonacciTask(n-1);
            sub1.fork();

            FibonacciTask sub2 = new FibonacciTask(n-2);

            return sub2.compute() + sub1.join();
        }
    }


    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        System.out.println(pool.invoke(new FibonacciTask(100L)));
    }
}
