/**
 * @author Jeck
 * 
 * Create on 2018年5月25日 下午10:07:24
 */
package com.jeck.tools;

/**
 * 
 * 
 * 
 */
public class StringEqualTestExtrend extends StringEqualTest
{
	protected String std = "test";

	public static void main(String[] args)
	{
		String s1 = "Programming";
		String s2 = new String("Programming");
		String s3 = "Program" + "ming";

		System.out.println(s1 == s2);
		System.out.println(s1 == s3);
		System.out.println(s1 == s1.intern());

		StringEqualTest stringEqualTest = new StringEqualTest();

		System.out.println(stringEqualTest.st);

		StringEqualTestExtrend stringEqualTestExtrend = new StringEqualTestExtrend();

		System.out.println(stringEqualTestExtrend.std);

	}
}

