/**
 * @author Jeck
 * 
 * Create on 2018年6月7日 下午3:53:05
 */
package com.jeck.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * 
 * 
 */
public class CompartorTest
{
	static final class Person2
	{
		private int age;
		private String name;

		public Person2(String name, int age)
		{
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString()
		{
			return name + ":" + age;
		}

		public int getAge()
		{
			return age;
		}

		public void setAge(int age)
		{
			this.age = age;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		// getter and setter方法省略....
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Person2 p1 = new Person2("zzh", 18);
		Person2 p2 = new Person2("jj", 17);
		Person2 p3 = new Person2("qq", 19);
		List<Person2> list2 = new ArrayList<Person2>();
		list2.add(p1);
		list2.add(p2);
		list2.add(p3);
		System.out.println(list2);
		Collections.sort(list2, new Comparator<Person2>()
		{

			@Override
			public int compare(Person2 o1, Person2 o2)
			{
				if (o1 == null || o2 == null)
					return 0;
				return o1.getAge() - o2.getAge();
			}

		});
		System.out.println(list2);

		System.out.println(list2);
		Collections.sort(list2, new Comparator<Person2>()
		{

			@Override
			public int compare(Person2 o1, Person2 o2)
			{
				if (o1 == null || o2 == null)
					return 0;
				return o2.getAge() - o1.getAge();
			}

		});
		System.out.println(list2);

	}

}
