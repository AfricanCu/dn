package com.wk.server.logic.friend;

public abstract class HandlerResultI {
	/**
	 * 结果码
	 */
	private int code;

	/**
	 * 当前服/切服处理
	 * 
	 * @param handler
	 */
	protected abstract void handle();

	public void setCode(int code) {
		this.code = code;
		handleCode(this.code);
	}

	/**
	 * 处理结果码
	 * 
	 * @param code
	 */
	public abstract void handleCode(int code);

	public int getCode() {
		return code;
	}
}
