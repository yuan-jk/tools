/**
 * @author Jeck
 * 
 * Create on 2018年5月26日 下午11:42:18
 */
package com.jeck.tools.sorters;

/**
 * 
 * 
 * 
 */
public class BubbleSorter
{
	public <T extends Comparable<T>> void sort(T[] list)
	{
		boolean swap = true;
		for (int i = 1, len = list.length; i < len && swap; i++)
		{
			swap = false;
			for (int j = 0; j < len - i; j++)
			{
				T tmp = list[j];
				if (tmp.compareTo(list[j + 1]) > 0)
				{
					list[j] = list[j + 1];
					list[j + 1] = tmp;
					swap = true;
				}
			}
		}
	}
}
