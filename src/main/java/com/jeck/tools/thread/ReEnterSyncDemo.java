package com.jeck.tools.thread;

class SyncReEnter implements Runnable {
    public synchronized void get() throws InterruptedException {
        System.out.print(Thread.currentThread().getId() + "\t");
        //在get方法里调用set
        Thread.sleep(3 * 1000);
        set();
    }

    public synchronized void set() {
        System.out.print(Thread.currentThread().getId() + "\t");
    }

    public void run() //run方法里调用了get方法
    {
        try {
            get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ReEnterSyncDemo {
    public static void main(String[] args) {
        SyncReEnter demo = new SyncReEnter();
        new Thread(demo).start();
        new Thread(demo).start();
    }
}