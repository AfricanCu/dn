package com.wk.engine.util;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

/**
 * PROTOBUF 工具类
 * 
 * @author Administrator
 *
 */
public class ProtobufUtils {

	private static final boolean HAS_PARSER;

	static {
		boolean hasParser = false;
		try {
			// MessageLite.getParsetForType() is not available until protobuf
			// 2.5.0.
			MessageLite.class.getDeclaredMethod("getParserForType");
			hasParser = true;
		} catch (Throwable t) {
			// Ignore
		}

		HAS_PARSER = hasParser;
	}

	/**
	 * 获取message的protobuf对象
	 * 
	 * @param <T>
	 * @param msg
	 * @param protobuf
	 * @return
	 * @throws InvalidProtocolBufferException
	 */
	public static <T extends MessageLite> T getProtobuf(byte[] msg, T protobuf)
			throws InvalidProtocolBufferException {
		MessageLite parseFrom;
		if (HAS_PARSER) {
			parseFrom = protobuf.getParserForType().parseFrom(msg);
		} else {
			parseFrom = protobuf.newBuilderForType().mergeFrom(msg).build();
		}
		return (T) parseFrom;
	}

	public static <T extends MessageLite> T getProto(byte[] array, T protobuf)
			throws InvalidProtocolBufferException {
		MessageLite parseFrom;
		if (HAS_PARSER) {
			parseFrom = protobuf.getParserForType().parseFrom(array);
		} else {
			parseFrom = protobuf.newBuilderForType().mergeFrom(array).build();
		}
		return (T) parseFrom;
	}
}
