package com.jeck.tools.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWriteTool {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private int num = 0;

    public void read() {//读的方法
        int cnt = 0;
        while (cnt++ < 3) {
            try {
                readLock.lock();
                System.out.println(Thread.currentThread().getId() + " start to read");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getId() + " reading," + num);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
        }
    }

    public void write() {//写的方法
        int cnt = 0;
        while (cnt++ < 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                writeLock.lock();
                System.out.println(Thread.currentThread().getId() + " start to write");
                Thread.sleep(1000);
                num = (int) (Math.random() * 10);
                System.out.println(Thread.currentThread().getId() + " write," + num);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        }
    }
}


class ReadThread extends Thread {
    private ReadWriteTool readTool;

    public ReadThread(ReadWriteTool readTool) {
        this.readTool = readTool;
    }

    public void run() {
        readTool.read();
    }
}

class WriteThread extends Thread {
    private ReadWriteTool writeTool;

    public WriteThread(ReadWriteTool writeTool) {
        this.writeTool = writeTool;
    }

    public void run() {
        writeTool.write();
    }
}


public class ReadWriteLockDemo {
    public static void main(String[] args) {
        ReadWriteTool tool = new ReadWriteTool();
        for (int i = 0; i < 3; i++) {
            new ReadThread(tool).start();
            new WriteThread(tool).start();
        }
    }
}