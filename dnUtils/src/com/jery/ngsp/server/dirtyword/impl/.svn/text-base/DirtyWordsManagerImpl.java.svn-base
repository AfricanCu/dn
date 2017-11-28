package com.jery.ngsp.server.dirtyword.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jery.ngsp.server.dirtyword.DirtyWordsManager;
import com.jery.ngsp.server.log.LoggerService;

public class DirtyWordsManagerImpl implements DirtyWordsManager {
	private static final DirtyWordsManagerImpl instance = new DirtyWordsManagerImpl();
	/** 索引深度 **/
	public static int DICTIONARY_DEPTH = 4;

	private DirtyWordData dirtyWordData = null;

	public static DirtyWordsManagerImpl getInstance() {
		return instance;
	}

	public static void main(String[] args) throws URISyntaxException {
		System.err.println(DirtyWordsManagerImpl.class.getResource(
				"./DirtyWords.xml").toURI());
	}

	public void loadData(String path) {
		try {
			DirtyWordData dirtyWordDataTmp = new DirtyWordData();
			HashSet<String> sensitiveWordsSet = new HashSet<>();
			HashSet<String> dirtyWordsSet = new HashSet<>();
			HashSet<Character> unseeCharSet = new HashSet<>();
			URL file = path == null ? this.getClass().getClassLoader()
					.getResource("DirtyWords.xml") : new File(path).toURI()
					.toURL();
			readDirtyWordsFile(file, sensitiveWordsSet, dirtyWordsSet,
					unseeCharSet);
			for (Character character : unseeCharSet) {
				dirtyWordDataTmp.addUnseeChar(character);
			}
			for (String str : dirtyWordsSet) {
				dirtyWordDataTmp.addDirtyWord(new MyString(str));
			}
			for (String str : sensitiveWordsSet) {
				dirtyWordDataTmp.addSensitiveWord(new MyString(str));
			}
			LoggerService.getPlatformLog().error(
					"------------------- 脏词表 加载成功 -------------------{}",
					dirtyWordsSet.size());
			this.dirtyWordData = dirtyWordDataTmp;
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(
					"------------------- 脏词表 加载失败 -------------------");
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			System.exit(1);
		}
	}

	public String checkDirtyWord(String content, boolean isIgnoreCase) {
		if (content == null || content.length() < 1) {
			return content;
		}
		content = replaceUnseeChar(content);
		char[] orgChars = content.toCharArray();
		HashMap<Character, CharSet> dirtyWordSet;
		char[] lowerChars;
		if (isIgnoreCase) {
			dirtyWordSet = this.dirtyWordData.getDirtyWordSetForLowerCase();
			lowerChars = content.toLowerCase().toCharArray();
		} else {
			dirtyWordSet = this.dirtyWordData.getDirtyWordSet();
			lowerChars = orgChars;
		}
		for (int i = 0; i < lowerChars.length; i++) {
			CharSet fset = (CharSet) dirtyWordSet.get(Character
					.valueOf(lowerChars[i]));
			if (fset == null) {
				continue;
			}
			MyString lookByShort = fset.matchingChars(lowerChars, i);
			if (lookByShort != null) {
				String str = lookByShort.getStr();
				return str;
			}
		}
		return null;
	}

	public String replaceDirtyWord(String content, String replaceWord,
			boolean isIgnoreCase) {
		if ((content == null) || (content.length() < 1)) {
			return content;
		}
		StringBuilder sb = new StringBuilder();
		content = replaceUnseeChar(content);
		char[] orgChars = content.toCharArray();
		HashMap<Character, CharSet> dirtyWordSet;
		char[] lowerChars;
		if (isIgnoreCase) {
			dirtyWordSet = this.dirtyWordData.getDirtyWordSetForLowerCase();
			lowerChars = new String(orgChars).toLowerCase().toCharArray();
		} else {
			dirtyWordSet = this.dirtyWordData.getDirtyWordSet();
			lowerChars = orgChars;
		}
		for (int i = 0; i < lowerChars.length; i++) {
			MyString replace = null;
			try {
				CharSet fset = (CharSet) dirtyWordSet.get(Character
						.valueOf(lowerChars[i]));
				if (fset == null) {
					if (replace == null) {
						sb.append(orgChars[i]);
					}
				} else
					replace = fset.matchingChars(lowerChars, i);
			} finally {
				if (replace == null) {
					sb.append(orgChars[i]);
				} else {
					sb.append(replaceWord);
					i += replace.getChars().length;
					i--;
				}
			}
		}
		return sb.toString();
	}

	public String checkSensitiveWord(String content, boolean isIgnoreCase) {
		if ((content == null) || (content.length() < 1)) {
			return content;
		}
		content = replaceUnseeChar(content);

		char[] orgChars = content.toCharArray();
		HashMap<Character, CharSet> dirtyWordSet;
		char[] lowerChars;
		if (isIgnoreCase) {
			dirtyWordSet = this.dirtyWordData.getSensitiveWordSetForLowerCase();
			lowerChars = new String(orgChars).toLowerCase().toCharArray();
		} else {
			dirtyWordSet = this.dirtyWordData.getSensitiveWordSet();
			lowerChars = orgChars;
		}
		for (int i = 0; i < lowerChars.length; i++) {
			CharSet fset = (CharSet) dirtyWordSet.get(Character
					.valueOf(lowerChars[i]));
			if (fset == null) {
				continue;
			}
			MyString lookByShort = fset.matchingChars(lowerChars, i);
			if (lookByShort != null) {
				String str = lookByShort.getStr();
				return str;
			}
		}
		return null;
	}

	public String replaceSensitiveWord(String content, String replaceWord,
			boolean isIgnoreCase) {
		if ((content == null) || (content.length() < 1)) {
			return content;
		}
		StringBuilder sb = new StringBuilder(128);
		content = replaceUnseeChar(content);
		char[] orgChars = content.toCharArray();
		HashMap<Character, CharSet> dirtyWordSet;
		char[] lowerChars;
		if (isIgnoreCase) {
			dirtyWordSet = this.dirtyWordData.getSensitiveWordSetForLowerCase();
			lowerChars = new String(orgChars).toLowerCase().toCharArray();
		} else {
			dirtyWordSet = this.dirtyWordData.getSensitiveWordSet();
			lowerChars = orgChars;
		}
		for (int i = 0; i < lowerChars.length; i++) {
			MyString replace = null;
			try {
				CharSet fset = (CharSet) dirtyWordSet.get(Character
						.valueOf(lowerChars[i]));
				if (fset == null) {
					sb.append(lowerChars[i]);
				} else
					replace = fset.matchingChars(lowerChars, i);
			} catch (Exception e) {
				LoggerService.getPlatformLog().error(e.getMessage(), e);
			} finally {
				if (replace == null) {
					sb.append(lowerChars[i]);
				} else {
					sb.append(replaceWord);
					i += replace.getChars().length;
					i--;
				}
			}
		}
		return sb.toString();
	}

	public boolean checkUnseeChar(String content) {
		if ((content == null) || (content.length() < 1)) {
			return false;
		}
		char[] c = content.toCharArray();
		for (int i = 0; i < c.length; i++)
			if (this.dirtyWordData.isUnseeChar(c[i])) {
				return true;
			}
		return false;
	}

	public String replaceUnseeChar(String content) {
		if ((content == null) || (content.length() < 1)) {
			return null;
		}
		char[] charArr = content.toCharArray();
		StringBuilder sb = new StringBuilder(128);
		for (int i = 0; i < charArr.length; i++) {
			if (!this.dirtyWordData.isUnseeChar(charArr[i])) {
				sb.append(charArr[i]);
			}
		}
		return sb.toString();
	}

	private void readDirtyWordsFile(URL url, HashSet<String> sensitiveWordsSet,
			HashSet<String> dirtyWordsSet, HashSet<Character> unseeCharSet)
			throws Exception {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(url);
		Element root = document.getRootElement();
		DICTIONARY_DEPTH = Integer.parseInt(root
				.elementTextTrim("dictionaryDepth"));
		Element tmpE = root.element("sensitiveWord");
		if (tmpE != null) {
			List<?> itemTemplateList = tmpE.elements("word");
			for (Iterator<?> it = itemTemplateList.iterator(); it.hasNext();) {
				sensitiveWordsSet.add(((Element) it.next()).getText());
			}
		}
		Element dirtyWordsEle = root.element("dirtyWords");
		if (dirtyWordsEle != null) {
			List<?> itemTemplateList = dirtyWordsEle.elements("word");
			for (Iterator<?> it = itemTemplateList.iterator(); it.hasNext();) {
				dirtyWordsSet.add(((Element) it.next()).getText());
			}
		}
		Element unseeCharsEle = root.element("unseeChars");
		if (unseeCharsEle != null) {
			List<?> itemTemplateList = unseeCharsEle.elements("char");
			for (Iterator<?> it = itemTemplateList.iterator(); it.hasNext();)
				unseeCharSet.add(Character.valueOf((char) Short
						.parseShort(((Element) it.next()).getText())));
		}
	}
}