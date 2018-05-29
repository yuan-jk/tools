package com.jeck.tools.kvtest;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

/**
 * HttpClient工具类
 *
 * @return
 */
public class CommonHttpClient
{
	public static Logger logger = Logger.getLogger(CommonHttpClient.class);

	private static CloseableHttpClient closeableHttpClient = null;

	private static int MaxTotalConn = 200;
	private static int PerRouteConn = 20;

	/**
	 * 
	 * 
	 * @return
	 */
	public static CloseableHttpClient getHttpClient()
	{
		if (closeableHttpClient == null)
		{
			closeableHttpClient = createHttpClient(MaxTotalConn, PerRouteConn);
		}
		return closeableHttpClient;
	}

	public static CloseableHttpClient getHttpClient(int MaxTotalConn, int PerRouteConn)
	{
		if (closeableHttpClient == null)
		{
			closeableHttpClient = createHttpClient(MaxTotalConn, PerRouteConn);
		}
		return closeableHttpClient;
	}


	/**
	 * 创建CloseableHttpClient
	 * @param maxTotal 最大连接数
	 * @param maxPerRoute 
	 * @return
	 */
	private static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute)
	{
		// 连接池设置
		// socket设置
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", plainsf)
				.register("https", sslsf)
				.build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 连接数量设置
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler()
		{
			@Override
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context)
			{
				if (executionCount >= 5)
				{// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException)
				{// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException)
				{// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException)
				{// 超时
					return false;
				}
				if (exception instanceof UnknownHostException)
				{// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException)
				{// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException)
				{// SSL握手异常
					return false;
				}

				HttpClientContext clientContext = HttpClientContext
						.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				// treat those request methods defined as idempotent by RFC-2616
				// as safe to retry automatically:
				// GET, HEAD, PUT, DELETE, OPTIONS, and TRACE
				if (!(request instanceof HttpEntityEnclosingRequest))
				{
					return true;
				}
				return false;
			}
		};

		// 设置重定向处理方式，限制重定向
		RedirectStrategy redirectStrategy = new RedirectStrategy()
		{
			@Override
			public boolean isRedirected(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) throws ProtocolException
			{

				return false;
			}

			@Override
			public HttpUriRequest getRedirect(HttpRequest arg0, HttpResponse arg1, HttpContext arg2)
					throws ProtocolException
			{

				return null;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler)
				.setRedirectStrategy(redirectStrategy)
				.build();

		return httpClient;
	}

	/**
	 * 关闭httpClient释放资源
	 */
	public static void release()
	{
		if (closeableHttpClient != null)
		{
			try
			{
				closeableHttpClient.close();
			} catch (IOException e)
			{
				logger.error("关闭httClient失败！", e);
			}
		}
	}

}