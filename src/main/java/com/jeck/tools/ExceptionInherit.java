/**
 * @author Jeck
 * 
 * Create on 2018年5月26日 下午3:44:30
 */
package com.jeck.tools;

/**
 * 
 * 
 * 
 */
class Annoyance extends Exception
{
}

class Sneeze extends Annoyance
{
}

public class ExceptionInherit
{

	public static void main(String[] args)
			throws Exception
	{
		try
		{
			try
			{
				throw new Sneeze();
			} catch (Annoyance a)
			{
				System.out.println("Caught Annoyance");
				throw a;
			}
		} catch (Sneeze s)
		{
			System.out.println("Caught Sneeze");
			return;
		} finally
		{
			System.out.println("Hello World!");
		}
	}
}