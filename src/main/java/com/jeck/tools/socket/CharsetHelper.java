/**
 * @author Jeck
 * 
 * Create on 2018年5月26日 下午9:08:09
 */
package com.jeck.tools.socket;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public final class CharsetHelper
{
	private static final String UTF_8 = "UTF-8";
	private static CharsetEncoder encoder = Charset.forName(UTF_8).newEncoder();
	private static CharsetDecoder decoder = Charset.forName(UTF_8).newDecoder();

	private CharsetHelper()
	{
	}

	public static ByteBuffer encode(CharBuffer in) throws CharacterCodingException
	{
		return encoder.encode(in);
	}

	public static CharBuffer decode(ByteBuffer in) throws CharacterCodingException
	{
		return decoder.decode(in);
	}
}