package com.wk.user.bean;

import java.util.BitSet;

import com.wk.engine.util.KeyValueDbCache;

/** 这个不能写成非静态内部类 */
public class UserBitSetEx extends BitSet {
	private static final long serialVersionUID = 1L;
	protected UserBean userBean;

	public UserBitSetEx(int i, UserBean userBean) {
		super(i);
		this.userBean = userBean;
	}

	public void set(int bitIndex) {
		super.set(bitIndex);
		if (this.userBean.dbCache != KeyValueDbCache.emptyUserDbCache) {
			this.userBean.dbCache.put(this.userBean.uid, userBean);
		}
	};

	@Override
	public void clear() {
		// LoggerService.getLogicLog().error("clear!!!!!");
		super.clear();
	}
};