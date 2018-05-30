/**
 * @author Jeck
 * 
 * Create on 2018年5月29日 下午5:43:19
 */
package com.jeck.tools.algorithm;

import java.io.PrintStream;
import java.util.Stack;

/**
 * 
 * 
 * 
 */
public class ClimbStairs
{
	private static PrintStream out = System.out;

	public static void main(String[] args)
	{
		int[] ns =
		{ -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 20, 30 };
		for (int n : ns)
		{
			// out.println("n=" + n + ", sum=" + recursionCount(n));
		}

		// recursionPrintDetail(0, 0, 9);

		Stack<Integer> stt = new Stack<Integer>();

		buileT(stt, 3);

	}

	/**
	 * 递归实现n级楼梯有多少种走法
	 * @param n
	 * @return
	 */
	public static int recursionCount(int n)
	{
		int sum = 0;

		if (n == 1)
		{
			sum = 1;
		} else if (n == 2)
		{
			sum = 2;
		} else if (n == 3)
		{
			sum = 4;
		} else if (n > 3)
		{
			sum = recursionCount(n - 1) + recursionCount(n - 2) + recursionCount(n - 3);
		} else
		{
			sum = -1;
		}

		return sum;
	}

	public static int recursionPrintDetail(int si, int tp, int n)
	{
		if (n == 1)
		{
			si += 1;
			out.println(si + ": " + tp + "+" + 1 + ";");
			return si;
		} else if (n == 2)
		{
			si += 1;
			out.println(si + ": " + tp + "+" + 1 + "+" + 1 + ";");
			si += 1;
			out.println(si + ": " + tp + "+" + 2 + ";");
			return si;
		} else if (n == 3)
		{
			si += 1;
			out.println(si + ": " + tp + "+" + 1 + "+" + 1 + "+" + 1 + ";");
			si += 1;
			out.println(si + ": " + tp + "+" + 1 + "+" + 2 + ";");
			si += 1;
			out.println(si + ": " + tp + "+" + 2 + "+" + 1 + ";");
			si += 1;
			out.println(si + ": " + tp + "+" + 3 + ";");
			return si;
		} else if (n > 3)
		{
			si = recursionPrintDetail(si, 3, n - 3);
			si = recursionPrintDetail(si, 2, n - 2);
			si = recursionPrintDetail(si, 1, n - 1);
			return si;
		} else
		{
			return si;
		}

	}

	public static void buileT(Stack<Integer> stt, int N)
	{
		if (N >= 1)
		{
			stt.push(1);
			buileT(stt, N - 1);
			stt.pop();
		}
		if (N >= 2)
		{
			stt.push(2);
			buileT(stt, N - 2);
			stt.pop();
		}
		if (N >= 3)
		{
			stt.push(3);
			buileT(stt, N - 3);
			stt.pop();
		}
		if (N == 0)
		{
			for (int i : stt)
			{
				System.out.print("Step:" + i + "-->");
			}
			System.out.println("完成");
		}
	}



}
