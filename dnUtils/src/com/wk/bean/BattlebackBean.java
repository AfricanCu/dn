package com.wk.bean;

import msg.BackMessage.BattleBackSm;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jery.ngsp.server.log.LoggerService;

/**
 * 战斗回放
 * 
 * @author ems
 *
 */
public class BattlebackBean {
	private int id;
	private byte[] data = SystemConstantsAbs.empltyBytes;

	public BattlebackBean() {
	}

	public BattlebackBean(int id, byte[] data) {
		super();
		this.id = id;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		if (data == null) {
			return;
		}
		this.data = data;
	}

	public BattleBackSm getBattleBackSm() {
		try {
			return BattleBackSm.newBuilder().mergeFrom(this.data).build();
		} catch (InvalidProtocolBufferException e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			return null;
		}
	}
}
