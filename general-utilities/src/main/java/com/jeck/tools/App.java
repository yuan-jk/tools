package com.jeck.tools;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.text.html.parser.Entity;

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

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException
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
		out.println(stringEqualTest.st);

		// out.print(3 / 0);

		ClassInitalization ci = new ClassInitalization();

		// ClassInitalization ci = ClassInitalization.class.newInstance();
		// String stt = String.class.newInstance();

		String stt = "测试\uD835\uDD6B";
		out.println(stt);
		out.println(stt.length());

		out.println(stt.substring(0, 1));

		out.println(stt.substring(0, 2));

		out.println(stt.substring(0, 3));

		TreeSet<String> ts = new TreeSet<>();

		ts.add("20180520");
		ts.add("20180501");
		ts.add("20180510");
		ts.add("20180531");
		ts.add("20180531");
		ts.add("20170531");
		int m = ts.size();
		for (int i = 0; i < m; i++)
		{
			out.println("size=" + ts.size());
			out.println("first=" + ts.pollFirst());
		}
		out.println("first=" + ts.pollFirst());
		out.println("last=" + ts.pollLast());

		out.println(1 << 3);
		out.println(-1 << 3);

		out.println(32 >> 3);
		out.println(-32 >> 3);

		out.println(32 >>> 3);
		out.println(-32 >>> 3);

		out.println(32 >> 0);
		out.println(-32 >> 0);

		List<String> lt = new ArrayList<>();
		lt.add("20180520");
		lt.add("20180501");
		lt.add("20180510");
		lt.add("20180531");
		lt.add("20180531");
		lt.add("20170531");

		out.println(lt);
		Collections.sort(lt);
		out.println(lt);
		Collections.sort(lt, Collections.reverseOrder());
		out.println(lt);

		out.println("----\n" + String.CASE_INSENSITIVE_ORDER.compare("20180606", "20180508"));

		Double d1 = 1d;

		double d2 = 1d;

		if (d1 == d2)
		{
			out.println("d1==d2");
		} else
		{
			out.println("d1!=d2");
		}

		Set<Double> set = new HashSet<>();
		set.add(1d);
		set.add(3d);
		set.add(5d);

		if (set.contains(d1))
		{
			out.println("set contain d1");
		} else
		{
			out.println("set not contain d1");
		}

		if (set.contains(d2))
		{
			out.println("set contain d2");
		} else
		{
			out.println("set not contain d2");
		}

		out.println(set);


    }
}
