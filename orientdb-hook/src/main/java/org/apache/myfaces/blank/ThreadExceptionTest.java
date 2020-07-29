package org.apache.myfaces.blank;

public class ThreadExceptionTest {

    class MyRun implements Runnable {

        @Override
        public void run() {
            System.out.println("current thread name: [" + Thread.currentThread().getId() + "]");
            try {
                Thread.sleep(3 * 1000);



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
