package com.anner.parallex;

import java.util.stream.LongStream;

/**
 * @author Anner
 * @since 11.0
 * Create on 2023/12/14
 */
public class ParallelSumStream {


    public static void main(String[] args) {
        LongStream.range(1L, 100L)
                .parallel()
                .peek(ParallelSumStream::printThreadName)
                .reduce(ParallelSumStream::printSum);
    }


    public static void printThreadName(long l) {
        String tName = Thread.currentThread().getName();
        System.out.println(tName + " offers:" + l);
    }


    public static long printSum (long i , long j ) {
        long sum = i + j;
        System.out.printf(
                "%s has: %d; plus: %d; result: %d\n",
                Thread.currentThread().getName(), i, j, sum
        );
        return sum;
    }
}
