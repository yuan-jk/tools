package com.jeck.tools.lambda;

/**
 * @author yuanjk
 * @version 21/7/2
 */
public class ClosuresEffectivelyTest {
    public static void main(String... args) {
        StringBuilder message = new StringBuilder();
        Runnable r = () -> System.out.println(message);
        // “effectively final,” meaning that it’s final in all but name
        //        message = new StringBuilder();
        message.append("Howdy, ");
        message.append("world!");
        r.run();
    }
}
