package com.jeck.tools.algorithm;

public class Test {

    public static void main(String[] args) {

    }


    public static ListNode addList(ListNode rs, ListNode node1, ListNode node2, boolean flag) {
        if (node1 == null) {
            if (node2 == null) {
                return rs;//1
            } else {
                ListNode tmp = new ListNode();
                if (flag) {
                    tmp.next = node2.next;
                    tmp.val = node2.val + 1;
                } else {
                    tmp = node2;
                }
                rs.next = tmp;//2
            }
        } else {
            if (node2 == null) {
                ListNode tmp = new ListNode();
                if (flag) {
                    tmp.next = node1.next;
                    tmp.val = node1.val + 1;
                } else {
                    tmp = node1;
                }

                rs.next = tmp;//3
            } else {

                int sum = node1.val + node2.val;
                ListNode tmp = new ListNode();

                tmp.val = sum % 10;

                if (sum >= 10) {
                    tmp.next = addList(tmp, node1.next, node2.next, true);
                } else {
                    tmp.next = addList(tmp, node1.next, node2.next, false);
                }

                rs.next = tmp;//4
            }
        }

        return rs;
    }


}
