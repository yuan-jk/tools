/**
 * @author Jeck
 * 
 * Create on 2018年1月8日 下午4:17:54
 */
package com.jeck.tools.kvtest;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.PoolEntry;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 
 * 
 * 
 */
public class HttpMethods
{
	public static Logger logger = Logger.getLogger(HttpMethods.class);

	private static Boolean isProxy = false;
	private static int CommonHttpclientTimeOut = 2000;

	private static CloseableHttpClient closeableHttpClient = CommonHttpClient.getHttpClient();

	private static void config(HttpRequestBase httpRequestBase)
	{
		// 设置Header等
		httpRequestBase.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");

		if (isProxy)
		{
			// HttpHost proxy = new HttpHost("10.1.2.2", 21);
			HttpHost proxy = new HttpHost("10.1.3.110", 21);
			// 配置请求的超时设置
			RequestConfig requestConfig = RequestConfig
					.custom()
					.setConnectionRequestTimeout(CommonHttpclientTimeOut)
					.setConnectTimeout(CommonHttpclientTimeOut)
					.setSocketTimeout(CommonHttpclientTimeOut)
					.setProxy(proxy)
					.build();
			httpRequestBase.setConfig(requestConfig);
		} else
		{
			RequestConfig requestConfig = RequestConfig
					.custom()
					.setConnectionRequestTimeout(CommonHttpclientTimeOut)
					.setConnectTimeout(CommonHttpclientTimeOut)
					.setSocketTimeout(CommonHttpclientTimeOut)
					.build();
			httpRequestBase.setConfig(requestConfig);
		}

	}

	/**
	 * @param httpRequestBase
	 * @param headerMap 首部
	 */
	@SuppressWarnings("unused")
	private void config(HttpRequestBase httpRequestBase, Map<String, String> headerMap)
	{
		// 设置Header等
		for (Map.Entry<String, String> entry : headerMap.entrySet())
		{
			httpRequestBase.setHeader(entry.getKey(), entry.getValue());
		}

		if (isProxy)
		{
			// HttpHost proxy = new HttpHost("10.1.2.2", 21);
			HttpHost proxy = new HttpHost("10.1.3.110", 21);
			// 配置请求的超时设置
			RequestConfig requestConfig = RequestConfig
					.custom()
					.setConnectionRequestTimeout(CommonHttpclientTimeOut)
					.setConnectTimeout(CommonHttpclientTimeOut)
					.setSocketTimeout(CommonHttpclientTimeOut)
					.setProxy(proxy)
					.build();
			httpRequestBase.setConfig(requestConfig);
		} else
		{
			RequestConfig requestConfig = RequestConfig
					.custom()
					.setConnectionRequestTimeout(CommonHttpclientTimeOut)
					.setConnectTimeout(CommonHttpclientTimeOut)
					.setSocketTimeout(CommonHttpclientTimeOut)
					.build();
			httpRequestBase.setConfig(requestConfig);
		}

	}

	public void getUrl() throws URISyntaxException
	{
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost("www.google.com")
				.setPath("/search")
				.setParameter("q", "httpclient")
				.setParameter("btnG", "Google Search")
				.setParameter("aq", "f")
				.setParameter("oq", "")
				.build();
	}

	/**
	 * 设置post参数
	 *
	 * @param httpost
	 * @param params
	 */
	private static void setPostParams(HttpPost httpost, Map<String, Object> params)
	{
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet)
		{
			nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
		try
		{
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * POST请求URL获取内容
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String post(String url, Map<String, Object> params) throws Exception
	{
		HttpPost httppost = new HttpPost(url);
		config(httppost);
		setPostParams(httppost, params);
		CloseableHttpResponse response = null;
		try
		{
			response = closeableHttpClient.execute(httppost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			try
			{
				if (response != null)
					response.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * GET请求URL获取内容
	 *
	 * @param url
	 * @return
	 */
	public static String[] get(String url)
	{
		String[] rs = new String[2];
		HttpGet httpget = new HttpGet(url);
		config(httpget);
		CloseableHttpResponse response = null;
		try
		{
			response = closeableHttpClient.execute(httpget, HttpClientContext.create());
			String status = "" + response.getStatusLine().getStatusCode();
			rs[0] = status;
			HttpEntity entity = response.getEntity();
			// String result = EntityUtils.toString(entity, "gb2312");
			String result = EntityUtils.toString(entity, "utf8");
			// String result = EntityUtils.toString(entity);
			rs[1] = result;
			EntityUtils.consume(entity);
			return rs;
		} catch (IOException e)
		{
			logger.error("Connection error: " + url);
			logger.error(e);
		} finally
		{
			try
			{
				if (response != null)
					response.close();
			} catch (IOException e)
			{
				logger.error(e);
			}
		}
		return rs;
	}

	/**
	 * @param url 请求网址
	 * @param charset 网页编码字符集
	 * @return [StatusCode, html]
	 */
	public static String[] get(String url, String charset)
	{
		String[] rs = new String[2];
		HttpGet httpget = new HttpGet(url);
		config(httpget);
		CloseableHttpResponse response = null;
		try
		{
			response = closeableHttpClient.execute(httpget, HttpClientContext.create());
			String status = "" + response.getStatusLine().getStatusCode();
			rs[0] = status;
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, charset);
			rs[1] = result;
			EntityUtils.consume(entity);
			return rs;
		} catch (IOException e)
		{
			logger.error("Connection error: " + url);
			logger.error(e);
		} finally
		{
			try
			{
				if (response != null)
					response.close();
			} catch (IOException e)
			{
				logger.error(e);
			}
		}
		return rs;
	}

	

	// RequestConfig requestConfig = RequestConfig.custom().;

	public static void main(String[] args)
	{
		PrintStream out = System.out;

		String st = "\uD835\uDD6B";
		out.println(st);
		out.println("st.length=" + st.length());

		char ch0 = st.charAt(0);

		out.println("st.charAt(0)=" + ch0);

		char ch1 = st.charAt(1);
		out.println("st.charAt(1)=" + ch1);

		char ch2 = '\uDD6B';
		out.println("\uDD6B=" + ch2);

		String sst = "abc" + st + "def" + st + "ghi";
		out.println(sst);
		out.println("sst.length()=" + sst.length());

		out.println("sst.charAt(2)=" + sst.charAt(2));
		out.println("sst.codePointAt(2)=" + sst.codePointAt(2));
		out.println("sst.codePointAt(2)=" + Integer.toHexString(sst.codePointAt(2)));

		out.println("sst.charAt(3)=" + sst.charAt(3));
		out.println("sst.codePointAt(3)=" + sst.codePointAt(3));
		out.println("sst.codePointAt(3)=" + Integer.toHexString(sst.codePointAt(3)));

		out.println("sst.charAt(4)=" + sst.charAt(4));
		out.println("sst.codePointAt(4)=" + Integer.toHexString(sst.codePointAt(4)));

		out.println("sst.charAt(7)=" + sst.charAt(7));
		out.println("sst.codePointAt(7)=" + Integer.toHexString(sst.codePointAt(7)));

		out.println("sst.charAt(8)=" + sst.charAt(8));
		out.println("sst.codePointAt(8)=" + Integer.toHexString(sst.codePointAt(8)));

		out.println("sst.charAt(9)=" + sst.charAt(9));
		out.println("sst.codePointAt(9)=" + Integer.toHexString(sst.codePointAt(9)));

		out.println("sst.offsetByCodePoints(0, 2)=" + sst.offsetByCodePoints(0, 2));
		out.println("sst.offsetByCodePoints(0, 3)=" + sst.offsetByCodePoints(0, 3));
		out.println("sst.offsetByCodePoints(0, 4)=" + sst.offsetByCodePoints(0, 4));

		out.println("sst.offsetByCodePoints(3, 1)=" + sst.offsetByCodePoints(3, 1));
		out.println("sst.offsetByCodePoints(3, 2)=" + sst.offsetByCodePoints(3, 2));
		out.println("sst.offsetByCodePoints(4, 1)=" + sst.offsetByCodePoints(4, 1));

		out.println(sst.indexOf(120171));

		out.println("sst.substring(3, 4)=" + sst.substring(3, 4));
		out.println("sst.substring(3, 5)=" + sst.substring(3, 5));
	}

}
