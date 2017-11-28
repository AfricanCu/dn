package com.jery.ngsp.server.dirtyword.impl;

class MyString {
	/**
	 * 
	 */
	private final String str;
	/**
	 * 
	 */
	private final char[] chars;
	/**
	 * 
	 */
	private final String lowerStr;
	/**
	 * 
	 */
	private final char[] lowerChars;

	MyString(String str) {
		this.str = str;
		this.chars = this.str.toCharArray();
		this.lowerStr = this.str.toLowerCase();
		this.lowerChars = this.lowerStr.toCharArray();
	}

	MyString toLowerCase() {
		MyString lowerCaseMS = new MyString(this.str.toLowerCase());
		return lowerCaseMS;
	}

	String getStr() {
		return this.str;
	}

	char[] getChars() {
		return this.chars;
	}

	String getLowerStr() {
		return this.lowerStr;
	}

	char[] getLowerChars() {
		return this.lowerChars;
	}
}