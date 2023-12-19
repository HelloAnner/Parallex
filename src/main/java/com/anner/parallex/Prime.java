package com.anner.parallex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author Anner
 * @since 11.0
 * Create on 2023/12/19
 * <p>
 * 计算2~15485863间的质数（也就是1000000个质数)
 */
public class Prime {

    public static void main(String[] args) {
        ForkJoinPool joinPool = ForkJoinPool.commonPool();
        List<Integer> primeList = Collections.synchronizedList(new ArrayList<>());
        int count = joinPool.invoke(new PrimeTask(primeList, 2, 1548586300));
        Collections.sort(primeList);
        System.out.println("count is " + count);
    }

    private static class PrimeTask extends RecursiveTask<Integer> {
        private final int start;

        private final int end;

        private final List<Integer> ansList;

        // 计算的最小范围
        private final static int THREAD_HOLDER = 100;

        public PrimeTask(List<Integer> ansList, int start, int end) {
            this.ansList = ansList;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int count = 0;
            if (end - start <= THREAD_HOLDER) {
                for (int i = start; i < end; i++) {
                    if (isPrime(i)) {
                        ansList.add(i);
                        count++;
                    }
                }
                return count;
            }
            int mid = start + (end - start) / 2;
            PrimeTask sub1 = new PrimeTask(ansList, start, mid);
            PrimeTask sub2 = new PrimeTask(ansList, mid + 1, end);

            sub1.fork();

            return sub2.compute() + sub1.join();
        }


        private boolean isPrime(int num) {
            for (int i = 2; i <= (int) Math.sqrt(num); i++) {
                if (num % i == 0) return false;
            }
            return true;
        }
    }
}
