package com.jery.ngsp.server.dirtyword.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 字符集合
 * 
 * @author ems
 *
 */
public class CharSet {
	private MyString string = null;
	private int index = 0;
	private final HashMap<Character, CharSet> nextCharSet = new HashMap<>();
	private final ArrayList<MyString> stringList = new ArrayList<>();
	private static final Comparator<MyString> comparator = new Comparator<MyString>() {
		public int compare(MyString o1, MyString o2) {
			return o1.getChars().length - o2.getChars().length;
		}
	};

	MyString matchingChars(char[] orgChars, int orgCharsIndex) {
		if (this.stringList.size() > 0) {
			for (MyString returnMyStr : this.stringList) {
				int _orgCharsIndex = orgCharsIndex;
				if (returnMyStr.getChars().length - (this.index + 1) > orgChars.length
						- (_orgCharsIndex + 1)) {
					continue;
				}
				boolean isMach = true;
				for (int j = this.index + 1; j < returnMyStr.getChars().length; j++) {
					if (returnMyStr.getChars()[j] != orgChars[(_orgCharsIndex + 1)]) {
						isMach = false;
						break;
					}
					_orgCharsIndex++;
				}
				if (isMach) {
					return returnMyStr;
				}
			}
			return null;
		}
		if (orgCharsIndex + 1 >= orgChars.length) {
			return this.string != null ? this.string : null;
		}
		CharSet _set = (CharSet) this.nextCharSet.get(Character
				.valueOf(orgChars[(orgCharsIndex + 1)]));
		if (_set == null) {
			if (this.string != null) {
				return this.string;
			}
			return null;
		}
		return _set.matchingChars(orgChars, orgCharsIndex + 1);
	}

	void addDirtyWord(MyString dirtyWord) {
		int nextIndex = this.index + 1;
		if (nextIndex >= dirtyWord.getChars().length) {
			this.string = dirtyWord;
			if (nextIndex >= DirtyWordsManagerImpl.DICTIONARY_DEPTH) {
				this.stringList.add(dirtyWord);
				Collections.sort(this.stringList, comparator);
			}
			return;
		}
		if (nextIndex >= DirtyWordsManagerImpl.DICTIONARY_DEPTH) {
			this.stringList.add(dirtyWord);
			Collections.sort(this.stringList, comparator);
			return;
		}
		Character nextChar = Character.valueOf(dirtyWord.getChars()[nextIndex]);
		CharSet _charSet = (CharSet) this.nextCharSet.get(nextChar);
		if (_charSet == null) {
			_charSet = new CharSet();
			_charSet.index = nextIndex;
			this.nextCharSet.put(nextChar, _charSet);
		}
		_charSet.addDirtyWord(dirtyWord);
	}
}