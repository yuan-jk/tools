/**
 * @author Jeck
 * 
 * Create on 2018年6月19日 下午2:19:35
 */
package com.jeck.tools.algorithm;

import java.util.Scanner;

public class PrimeFactorization
{

	public static void main(String[] args)
	{
		System.out.println("输入一个数进行分解：");
		Scanner s = new Scanner(System.in);
		factor(s.nextInt());
	}

	// 函数：进行分解质因数
	static void factor(int number)
	{
		if (isPrime(number))
		{
			System.out.print(number + "  ");
		} else
		{
			for (int i = 2; i < number; i++)
			{
				if (number % i == 0)
				{
					System.out.print(i + "  ");
					factor(number / i);
					break;
				}
			}
		}
	}

	// 函数：判断是不是素数
	static boolean isPrime(int number)
	{
		for (int i = 2; i < number; i++)
		{
			if (number % i == 0)
			{
				return false;
			}
		}
		return true;
	}
}