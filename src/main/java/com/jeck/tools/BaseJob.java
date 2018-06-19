/**
 * @author Jeck
 * 
 * Create on 2018年6月4日 下午5:31:56
 */
package com.jeck.tools;

/**
 * 
 * 
 * 
 */
public abstract class BaseJob
{
	private String name;

	/**
	 * @param name
	 */
	public BaseJob(String name)
	{
		this.name = name;
		System.out.println(this.getClass().getSimpleName());
	}

}
