package com.jeck.tools.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 类<code>Doc</code>用于：TODO
 *
 * @author yuanjk
 * @version 1.0
 */
public class BasicOperations {

    public static class SomeClass {

    }

    public static void main(String[] args) throws FileNotFoundException {
        Kryo kryo = new Kryo();
        // ...
        Output output = new Output(new FileOutputStream("file.bin"));
        SomeClass someObject = new SomeClass();
        kryo.writeObject(output, someObject);
        output.close();
        // ...
        Input input = new Input(new FileInputStream("file.bin"));
        SomeClass sameObject = kryo.readObject(input, SomeClass.class);
        input.close();

    }
}
