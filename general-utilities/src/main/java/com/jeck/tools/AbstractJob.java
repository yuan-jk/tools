/**
 * @author Jeck
 * 
 * Create on 2018年6月4日 下午5:31:32
 */
package com.jeck.tools;

/**
 * 
 * 
 * 
 */
public class AbstractJob extends BaseJob
{
	private String concreteName;

	/**
	 * @param concreteName
	 */
	public AbstractJob(String concreteName)
	{
		// super(this.getClass().getSimpleName());
		super(concreteName);
		this.concreteName = concreteName;
		System.out.println(this.getClass().getSimpleName());
	}

	public static void main(String[] args)
	{
		AbstractJob ab = new AbstractJob("test");
	}

}
