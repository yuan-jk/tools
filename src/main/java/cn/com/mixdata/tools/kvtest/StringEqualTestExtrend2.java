/**
 * @author Jeck
 * 
 * Create on 2018年5月25日 下午10:07:24
 */
package cn.com.mixdata.tools.kvtest;

import cn.com.mixdata.tools.StringEqualTest;

/**
 * 
 * 
 * 
 */
public class StringEqualTestExtrend2 extends StringEqualTest
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

		// System.out.println(stringEqualTest.st);

		StringEqualTestExtrend2 stringEqualTestExtrend = new StringEqualTestExtrend2();

		System.out.println(stringEqualTestExtrend.std);

	}

	public void tm(StringEqualTestExtrend2 stringEqualTestExtrend)
	{
		System.out.println(stringEqualTestExtrend.st);
		System.out.println(st);
	}
}

