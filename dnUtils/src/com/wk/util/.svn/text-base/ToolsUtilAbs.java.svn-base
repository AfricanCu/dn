package com.wk.util;

public class ToolsUtilAbs {

	/**
	 * 邀请码前面加0转字符串
	 */
	public static String invitationToString(int invitation){
		String str=String.format("%06d", invitation);
		return str;
	}
	
	/**
	 * 邀请码前面去转INT
	 */
	public static int invitationToInt(String invitation){
		int i=Integer.parseInt(invitation);
		return i;
	}
	
	public static void main(String[] args) {
		System.out.println(invitationToString(1));
		System.out.println(invitationToInt(invitationToString(1)));
	}
}
