/**
 * @author Jeck
 * 
 * Create on 2018年5月27日 上午12:16:27
 */
package com.jeck.tools.searchers;

import java.util.Comparator;

/**
 * 
 * 一种在有序数组中查找某一特定元素的搜索算法，时间复杂度是O(logN)
 * 
 */
public class BinarySearcher
{
	public static <T extends Comparable<T>> int search(T[] x, T key)
	{
		return search(x, 0, x.length - 1, key);
	}

	// 使用循环实现的二分查找
	public static <T> int search(T[] x, T key, Comparator<T> comp)
	{
		int low = 0;
		int high = x.length - 1;
		while (low <= high)
		{
			int mid = (low + high) >>> 1;
			int cmp = comp.compare(x[mid], key);
			if (cmp < 0)
			{
				low = mid + 1;
			} else if (cmp > 0)
			{
				high = mid - 1;
			} else
			{
				return mid;
			}
		}
		return -1;
	}

	// 使用递归实现的二分查找
	private static <T extends Comparable<T>> int search(T[] x, int low, int high, T key)
	{
		if (low <= high)
		{
			int mid = low + ((high - low) >> 1);
			if (key.compareTo(x[mid]) == 0)
			{
				return mid;
			} else if (key.compareTo(x[mid]) < 0)
			{
				return search(x, low, mid - 1, key);
			} else
			{
				return search(x, mid + 1, high, key);
			}
		}
		return -1;
	}
}
