package com.wk.bean;

import io.netty.util.AttributeKey;

import java.util.Date;

import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.engine.net.I.CsConnectI;

/**
 * 静态变量抽象
 * 
 * @author ems
 *
 */
public class SystemConstantsAbs {
	/**
	 * 空字节数组对象
	 */
	public static final byte[] empltyBytes = new byte[] {};
	/**
	 * 客户端请求数据长度限制
	 */
	public static final short MAX_PACKAGE_LENGTH = 8196;
	/**
	 * 通道（channel） attrKey
	 */
	public static final AttributeKey<ChannelAttachmentAbs> CHANNEL_ATTR_KEY = AttributeKey
			.valueOf(CsConnectI.class.getName());
	/**
	 * accessToken 超时时间
	 */
	public static final long dayInMillis = 24 * 60 * 60 * 1000l;
	public static final long days_29_timeInMillis = 29 * dayInMillis;
	public static final long hours_2_timeInMillis = 2 * 60 * 60 * 1000l;
	/** 0时间 */
	public static final Date nullDate = new Date(0);
	/***/
	public static final String Double_SHARP_SEP = "##";
	/**
	 * redis 排行榜分隔符
	 */
	public static final String rank_SEP = "##";
	/**
	 * 无服务器id
	 */
	public static final int NoServerId = 0;
	/**
	 * 无房间id
	 */
	public static final int NoRoomId = 0;
	/**
	 * 无俱乐部id
	 */
	public static final int NoGuildId = 0;
	// public static void main(String[] args) {
	// System.err.println(nullDate);
	// }
	public static final String complete = "complete";
	/** 分号 **/
	public static final String fen_SEP = ";";
	/** 逗号 **/
	public static final String dot_SEP = ",";
	/** 井号 */
	public static final String sharp_SEP = "#";
	public static final long NoUid = 0;
	/** 结束 bitSet key的变量名 **/
	public static final String EndLoopStr = "bitSet";
	public static final int NoJob = 0;
	/** 无代理 **/
	public static final int NoAgency = 0;
	/** 麻将最多4人 */
	public static final int MajiangRoomMaxNum = 4;

}
