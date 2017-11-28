package com.wk.I;

import java.io.File;

/**
 * 可编辑的文件
 * 
 * @author Administrator
 *
 */
public abstract class EditableFile {
	private final String path;

	public EditableFile(File file) throws Exception {
		super();
		this.path = file.getCanonicalPath();
	}

	public String getPath() {
		return path;
	}

	/**
	 * 重新加载。。。
	 * 
	 * @throws Exception
	 */
	public abstract void run() throws Exception;

}
