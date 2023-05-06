package com.k.service.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * 生成一个整型无重随机序列，无参构造使用数组序列生成，有参构造使用完全剩余系定理（效率高，包含生成质数序列）
 */
public class UniqueRandom {
    int maxValue;
    ArrayList<Integer> prime_num = new ArrayList<Integer>();// 质数序列用于查找\判断质数用
    Random rd = new Random();
    boolean isPrimeModel = false;

    UniqueRandom() {
    }

    UniqueRandom(int maxValue) {
        this.maxValue = maxValue + 1;
        createPrimeList();
        isPrimeModel = true;
        // for(Integer pm:prime_num)System.out.print(pm+",");
    }

    private void createPrimeList() {
        prime_num.add(2);
        prime_num.add(3);
        if (maxValue < 5) {
            return;
        }
        for (int i = 5; i < maxValue; i = i + 2) {
            int sqrt = sqrt(i);
            boolean isPrime = true;
            for (Integer pm : prime_num) {
                if (!isPrime || pm > sqrt) {
                    break;
                }
                isPrime = i % pm != 0;
            }
            if (isPrime) {
                prime_num.add(i);
            }
        }
    }

    private int sqrt(int value) {
        return (int) Math.ceil(Math.sqrt(new Integer(value).doubleValue()));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        int rate = 1000;
        int MAX = 1000 * rate;
        int SIZE = 1000 * rate;
        int NEED = 1000 * rate;
        UniqueRandom ur = new UniqueRandom(MAX);
        long start = System.nanoTime();
        int[] result = ur.randomList(NEED, SIZE);
        System.out.println(result.length);
        System.out.println(System.nanoTime() - start);
        // for(int i:result)System.out.print(i+",");
    }

    int[] randomList(int count, int size) {
        int[] result = new int[count];
        if (isPrimeModel) {
            if (size >= maxValue) {
                System.out.println("size Out of range! The maxValue is:" + maxValue);
                return null;
            }
            int rdn_prm_num_for_size = primeNumWith(size);
            int initpos = (size == count) ? 0 : rd.nextInt(size - count);// 随机点1
            for (int i = 0; i < count; i++) {
                result[i] = (int) ((initpos + i) * (long) rdn_prm_num_for_size % size);
            }
        } else {
            return getRandomSequence2(size, count);
        }
        return result;
    }

    private int primeNumWith(int size) {
        while (true) {
            int pn = prime_num.get(rd.nextInt(prime_num.size()));// 随机点2
            if (pn < size && size % pn == 0) {
                continue;
            }
            return pn;
        }
    }

    private int[] getRandomSequence2(int maxNum, int arrSize) {
        int[] sequence = new int[maxNum];
        for (int i = 0; i < maxNum; i++) {
            sequence[i] = i;
        }
        int[] output = new int[arrSize];
        int end = maxNum - 1;
        for (int i = 0; i < arrSize; i++) {
            int num = rd.nextInt(end + 1);
            output[i] = sequence[num];
            sequence[num] = sequence[end];
            end--;
        }
        return output;
    }

}
