/**
 * @author Jeck
 * 
 * Create on 2018年1月8日 上午10:51:11
 */
package cn.com.mixdata.tools.kvtest;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * 
 * 
 * 
 */
public class UploadThread extends Thread
{
	private int initKeyIdx = 0;
	private int keyMax = 1;
	private String key = "key";
	private String sign = "";
	private CloseableHttpClient closeableHttpClient;
	private String url = "";

	private String value = "5487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e11534"
			+ "7976ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e1153479"
			+ "76ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976"
			+ "ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec"
			+ "23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec23e335487d6f967d2e45e115347976ec23"
			+ "e335487d6f967d2e45e115347976ec23e33";

	public UploadThread(int initKeyIdx, int keyMax, String sign, CloseableHttpClient closeableHttpClient)
	{
		this.initKeyIdx = initKeyIdx;
		this.keyMax = keyMax;
		this.sign = sign;
		this.closeableHttpClient = closeableHttpClient;
	}

	@Override
	public void run()
	{
		for (int i = initKeyIdx; i <= keyMax; i++)
		{

		}
	}

}
