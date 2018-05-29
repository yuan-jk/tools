package com.jeck.tools.kvtest;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.text.html.parser.Entity;

import com.jeck.tools.StringEqualTest;

/**
 * Hello world!
 *
 */
public class App 
{

	private String st = "outer class variable";
	private static String sst = "outer static class variable";

	public static class InnerClass
	{
		public void print()
		{
			System.out.println("inner class variable");
			System.out.println(sst);
		}
	}

    public static void main( String[] args )
    {
		String userDir = System.getProperty("user.dir");

		Properties properties = System.getProperties();
		for (Entry<Object, Object> entry : properties.entrySet())
		{
			// System.out.println("key=" + entry.getKey() + ", value=" +
			// entry.getValue());
		}

		System.out.println(userDir);

		String path1 = userDir;
		File file = new File(path1);
		System.out.println("path1=" + path1 + ", isFile=" + file.isFile() + ", isDirectory=" + file.isDirectory());
		String path2 = userDir + "/src";
		file = new File(path2);
		System.out.println("path2=" + path2 + ", isFile=" + file.isFile() + ", isDirectory=" + file.isDirectory());
		String path3 = userDir + "/pom.xml";
		file = new File(path3);
		System.out.println("path3=" + path3 + ", isFile=" + file.isFile() + ", isDirectory=" + file.isDirectory());
		String path4 = userDir + "/tmp/";
		file = new File(path4);
		System.out.println("path4=" + path4 + ", isFile=" + file.isFile() + ", isDirectory=" + file.isDirectory());
        
		Map<String, Integer> map = new HashMap<>();
		map.put(null, 0);
		map.put("a", 1);

		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
			System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
		}

		map = new ConcurrentHashMap<>();

		PrintStream out = System.out;

		String st = "a.bcdef.g";

		out.println(st.length());

		out.println(st.lastIndexOf("."));

		out.println(st.indexOf("b"));

		out.println(st.charAt(st.indexOf("c")));

		out.println(st.charAt(3));

		out.println(st.substring(st.indexOf("b"), st.length()));

		StringEqualTest stringEqualTest = new StringEqualTest();
		// out.println(stringEqualTest.st);

		StringEqualTestExtrend stringEqualTestExtrend = new StringEqualTestExtrend();
		stringEqualTestExtrend.tm(stringEqualTestExtrend);

    }
}
