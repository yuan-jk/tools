package com.jeck.tools.chars;

import java.util.Comparator;

public class CharTest {

    public static void main(String[] args) {
//        digitTest();
//        stringCodePoint();

        int[] nums1 = {1, 2};
        int[] nums2 = {3, 4};
        System.out.println(findMedianSortedArrays(nums1, nums2));
    }


    public static void digitTest() {
        System.out.println(Character.digit('3', 2));
        System.out.println(Character.digit('3', 10));
        System.out.println(Character.digit('a', 16));
        System.out.println(Character.digit('A', 16));

        System.out.println(Character.digit(3, 2));
        System.out.println(Character.digit(3, 10));

        Comparator<Integer> com = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

    }

    public static void stringCodePoint() {
        String s = "aAbAcC袁";
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i) + "=" + s.codePointAt(i));
        }
    }


    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len = nums1.length + nums2.length;
        int fistIdx = 0;
        if (len % 2 == 0) {
            fistIdx = len / 2;
        } else {
            fistIdx = (len + 1) / 2;
        }

        int idx1 = 0;
        int idx2 = 0;

        int curr = 0;
        for (int i = 0; i < fistIdx; i++) {
            if (idx1 == nums1.length) {
                curr = nums2[idx2];
                idx2++;
                continue;
            }

            if (idx2 == nums2.length) {
                curr = nums1[idx1];
                idx1++;
                continue;
            }

            if (nums1[idx1] <= nums2[idx2]) {
                curr = nums1[idx1];
                idx1++;
            } else {
                curr = nums2[idx2];
                idx2++;
            }
        }


        double rs = curr;
        System.out.println("current =" + rs);
        System.out.println("idx1=" + idx1 + ", idx2=" + idx2);

        System.out.println("length=" + len);
        System.out.println("length % 2=" + (len % 2));

        if (len % 2 == 0) {
            if (idx1 == nums1.length) {
                rs = (curr + nums2[idx2]) / 2.0;
                return rs;
            }

            if (idx2 == nums2.length) {
                rs = (curr + nums1[idx1]) / 2.0;
                return rs;
            }

            rs = (curr + Math.min(nums1[idx1], nums2[idx2])) / 2.0;
        }
        return rs;
    }

}
