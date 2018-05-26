/**
 * @author Jeck
 * 
 * Create on 2018年5月26日 下午1:36:16
 */
package cn.com.mixdata.tools;

import java.io.PrintStream;

/**
 * 
 * 
 * 
 */
public class StringReverse
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String input = "abcdef";

		PrintStream out = System.out;

		out.println(input + " " + reverse(input));

		input = "\uD835\uDD68abc\uD835\uDD6B";

		out.println(input + " " + reverse(input));

	}

	public static String reverse(String originStr)
	{

		if (originStr == null || originStr.codePointCount(0, originStr.length()) == 1)
			return originStr;

		int offset = 1;
		if (Character.isSurrogate(originStr.charAt(0)))
		{
			offset = 2;

		} else
		{
			offset = 1;
		}

		return reverse(originStr.substring(offset)) + String.valueOf(Character.toChars(originStr.codePointAt(0)));

	}

}
