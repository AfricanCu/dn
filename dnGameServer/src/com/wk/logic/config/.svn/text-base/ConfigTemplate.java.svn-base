package com.wk.logic.config;

import java.util.ArrayList;
import java.util.List;

import com.wk.bean.SystemConstantsAbs;
import com.wk.server.logic.room.GameTimesType;
import com.wk.server.logic.room.NiuType;
import com.wk.util.ReadUtil;
import com.wk.util.TemplateCheckAbs;

/**
 * 游戏配置
 * 
 * @author ems
 *
 */
public class ConfigTemplate implements TemplateCheckAbs {

	private static ConfigTemplate configTemplate;

	public static void explain(String string) throws Exception {
		ConfigTemplate tmp = ReadUtil.explainCsvData(string,
				ConfigTemplate.class, true).get(0);
		configTemplate = tmp;
	}

	public static ConfigTemplate getConfigTemplate() {
		return configTemplate;
	}

	private int iD;
	private int proxyCreateRoomMax;
	private int guildCreateRoomMax;
	private int guildMemberMax;
	private String notice = "";
	/** 随机牛几到牛几 */
	private NiuType[] niuToNiu;
	/** 输太多了，牛5-牛9的概率 **/
	private int fiveNineNiuPercent;

	public int getiD() {
		return iD;
	}

	public int getProxyCreateRoomMax() {
		return proxyCreateRoomMax;
	}

	public int getGuildCreateRoomMax() {
		return guildCreateRoomMax;
	}

	public int getGuildMemberMax() {
		return guildMemberMax;
	}

	public String getNotice() {
		return notice;
	}

	public void setGameMinusTooMuch(String gameMinusTooMuch) {
		GameTimesType.reset(gameMinusTooMuch);
	}

	public NiuType[] getNiuToNiu() {
		return niuToNiu;
	}

	public void setNiuToNiu(String niuToNiu) {
		String[] split = niuToNiu.split(SystemConstantsAbs.sharp_SEP);
		this.niuToNiu = new NiuType[] {
				NiuType.getEnum(Integer.parseInt(split[0])),
				NiuType.getEnum(Integer.parseInt(split[1])) };
		isNiuToNiu(NiuType.five);
	}

	public int getFiveNineNiuPercent() {
		return fiveNineNiuPercent;
	}

	/**
	 * 是不是牛5-牛9
	 * 
	 * @param niuType
	 * @return
	 */
	public boolean isNiuToNiu(NiuType niuType) {
		return niuType.getType() >= this.niuToNiu[0].getType()
				&& niuType.getType() <= this.niuToNiu[1].getType();
	}

	public boolean isDownNiu(NiuType niuType) {
		return niuType.getType() < this.niuToNiu[0].getType();
	}

	@Override
	public void check() throws Exception {
		// TODO Auto-generated method stub
	}

}
