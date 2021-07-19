package com.jeck.tools.lambda;

/**
 * @author yuanjk
 * @version 21/7/2
 */
class Hello {
    public Runnable r = new Runnable() {
        public void run() {
            System.out.println(this);
            System.out.println(toString());
        }
    };

    public String toString() {
        return "Hello's custom toString()";
    }
}

class Hello2 {
    public Runnable r = new Runnable() {
        public void run() {
            System.out.println(Hello2.this);
            System.out.println(Hello2.this.toString());
        }
    };

    public String toString() {
        return "Hello2's custom toString()";
    }
}

class Hello3 {
    public Runnable r = () -> {
        System.out.println(this);
        System.out.println(toString());
    };

    public String toString() {
        return "Hello3's custom toString()";
    }
}

public class InnerClassExamples {
    public static void main(String... args) {
        Hello h = new Hello();
        h.r.run();

        Hello2 h2 = new Hello2();
        h2.r.run();

        Hello3 h3 = new Hello3();
        h3.r.run();
    }
}