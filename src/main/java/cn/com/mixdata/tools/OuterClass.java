/**
 * @author Jeck
 * 
 * Create on 2018年5月21日 下午6:04:57
 */
package cn.com.mixdata.tools;

import cn.com.mixdata.tools.App.InnerClass;

/**
 * 
 * 
 * 
 */
public class OuterClass
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		App app = new App();

		// InnerClass ic = app.new InnerClass();
		InnerClass ic = new InnerClass();
		ic.print();

	}

}
