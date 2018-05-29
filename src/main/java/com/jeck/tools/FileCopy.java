/**
 * @author Jeck
 * 
 * Create on 2018年5月26日 下午4:32:24
 */
package com.jeck.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * 
 * 
 */
public class FileCopy
{
	public static void fileCopy(String source, String target) throws IOException
	{
		try (InputStream in = new FileInputStream(source))
		{
			try (OutputStream out = new FileOutputStream(target))
			{
				byte[] buffer = new byte[4096];
				int bytesToRead;
				while ((bytesToRead = in.read(buffer)) != -1)
				{
					out.write(buffer, 0, bytesToRead);
				}
			}
		}
	}

	public static void fileCopyNIO(String source, String target) throws IOException
	{
		try (FileInputStream in = new FileInputStream(source))
		{
			try (FileOutputStream out = new FileOutputStream(target))
			{
				FileChannel inChannel = in.getChannel();
				FileChannel outChannel = out.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(4096);
				while (inChannel.read(buffer) != -1)
				{
					buffer.flip();
					outChannel.write(buffer);
					buffer.clear();
				}
			}
		}
	}
}
