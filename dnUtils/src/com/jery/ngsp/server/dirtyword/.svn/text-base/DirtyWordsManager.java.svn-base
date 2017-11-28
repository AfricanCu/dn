package com.jery.ngsp.server.dirtyword;

public abstract interface DirtyWordsManager {
	/**
	 * 加载数据
	 * 
	 * @param path
	 *            如果为null 默认：./DirtyWords.xml
	 */
	public abstract void loadData(String path);

	/**
	 * 检测脏词
	 * 
	 * @param content
	 * @param isIgnoreCase
	 *            是否忽略大小写
	 * @return
	 */
	public abstract String checkDirtyWord(String content, boolean isIgnoreCase);

	/**
	 * 
	 * @param content
	 * @param replaceWord
	 * @param isIgnoreCase
	 * @return
	 */
	public abstract String replaceDirtyWord(String content, String replaceWord,
			boolean isIgnoreCase);

	public abstract String checkSensitiveWord(String content,
			boolean isIgnoreCase);

	public abstract String replaceSensitiveWord(String content,
			String replaceWord, boolean isIgnoreCase);

	/**
	 * 检测是否有不可见字符
	 * 
	 * @param paramString
	 * @return
	 */
	public abstract boolean checkUnseeChar(String content);

	/**
	 * 替换不可见字符
	 * 
	 * @param paramString
	 * @return
	 */
	public abstract String replaceUnseeChar(String content);
}