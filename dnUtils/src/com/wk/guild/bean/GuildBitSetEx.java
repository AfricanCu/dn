package com.wk.guild.bean;

import java.util.BitSet;

import com.wk.engine.util.KeyValueDbCache;

/** 这个不能写成非静态内部类 */
public class GuildBitSetEx extends BitSet {
	private static final long serialVersionUID = 1L;
	protected GuildBean guildBean;

	public GuildBitSetEx(int nbits, GuildBean guildBean) {
		super(nbits);
		this.guildBean = guildBean;
	}

	public void set(int bitIndex) {
		super.set(bitIndex);
		if (this.guildBean.dbCache != KeyValueDbCache.emptyGuildDbCache) {
			this.guildBean.dbCache.put(this.guildBean.id, guildBean);
		}
	};

	@Override
	public void clear() {
		// LoggerService.getLogicLog().error("clear!!!!!");
		super.clear();
	}
}