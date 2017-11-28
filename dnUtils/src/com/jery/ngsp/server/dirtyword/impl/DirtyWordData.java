package com.jery.ngsp.server.dirtyword.impl;

import java.util.HashMap;
import java.util.HashSet;

public class DirtyWordData {
	/**
	 * 脏词字符
	 * 
	 * <字符索引，字符集合>
	 */
	private final HashMap<Character, CharSet> dirtyWordSet = new HashMap<>();
	/**
	 * 脏词字符 小写
	 */
	private final HashMap<Character, CharSet> dirtyWordSetForLowerCase = new HashMap<>();
	/**
	 * 敏感词
	 */
	private final HashMap<Character, CharSet> sensitiveWordSet = new HashMap<>();
	/**
	 * 敏感词 小写
	 */
	private final HashMap<Character, CharSet> sensitiveWordSetForLowerCase = new HashMap<>();
	/**
	 * 不可见字符 set
	 * 
	 */
	private final HashSet<Character> unseeCharactor = new HashSet<>();
	/**
	 * 最大不可见字符值
	 */
	private char maxUnseeCharValue;

	void addDirtyWord(MyString str) {
		CharSet charSet = this.dirtyWordSet.get(Character.valueOf(str
				.getChars()[0]));
		if (charSet == null) {
			charSet = new CharSet();
			this.dirtyWordSet.put(str.getChars()[0], charSet);
		}
		charSet.addDirtyWord(str);
		MyString lowerStr = str.toLowerCase();
		CharSet lowerCharSet = (CharSet) this.dirtyWordSetForLowerCase
				.get(Character.valueOf(lowerStr.getChars()[0]));
		if (lowerCharSet == null) {
			lowerCharSet = new CharSet();
			this.dirtyWordSetForLowerCase.put(
					Character.valueOf(lowerStr.getChars()[0]), lowerCharSet);
		}
		lowerCharSet.addDirtyWord(lowerStr);
	}

	void addSensitiveWord(MyString str) {
		CharSet charSet = (CharSet) this.sensitiveWordSet.get(Character
				.valueOf(str.getChars()[0]));
		if (charSet == null) {
			charSet = new CharSet();
			this.sensitiveWordSet.put(Character.valueOf(str.getChars()[0]),
					charSet);
		}
		charSet.addDirtyWord(str);
		MyString lowerStr = str.toLowerCase();
		CharSet lowerCharSet = (CharSet) this.sensitiveWordSetForLowerCase
				.get(Character.valueOf(lowerStr.getChars()[0]));
		if (lowerCharSet == null) {
			lowerCharSet = new CharSet();
			this.sensitiveWordSetForLowerCase.put(
					Character.valueOf(lowerStr.getChars()[0]), lowerCharSet);
		}
		lowerCharSet.addDirtyWord(lowerStr);
	}

	void addUnseeChar(Character unseeChar) {
		this.unseeCharactor.add(unseeChar);
		if (this.maxUnseeCharValue < unseeChar.charValue())
			this.maxUnseeCharValue = unseeChar.charValue();
	}

	boolean isUnseeChar(char cha) {
		if (cha > this.maxUnseeCharValue) {
			return false;
		}
		return this.unseeCharactor.contains(cha);
	}

	HashMap<Character, CharSet> getDirtyWordSet() {
		return this.dirtyWordSet;
	}

	HashMap<Character, CharSet> getDirtyWordSetForLowerCase() {
		return this.dirtyWordSetForLowerCase;
	}

	HashMap<Character, CharSet> getSensitiveWordSet() {
		return this.sensitiveWordSet;
	}

	HashMap<Character, CharSet> getSensitiveWordSetForLowerCase() {
		return this.sensitiveWordSetForLowerCase;
	}

	HashSet<Character> getUnseeCharactor() {
		return this.unseeCharactor;
	}

	char getMaxUnseeCharValue() {
		return this.maxUnseeCharValue;
	}
}