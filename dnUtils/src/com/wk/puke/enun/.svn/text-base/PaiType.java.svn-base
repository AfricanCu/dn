package com.wk.puke.enun;

import java.util.ArrayList;

import com.wk.puke.Pai;

public enum PaiType {
	/***/
	fang(1, "方"),
	/***/
	mei(2, "梅"),
	/***/
	hong(3, "红"),
	/***/
	hei(4, "黑");
	/** ID **/
	private final int type;
	/** ***/
	private final String name;
	private final ArrayList<Pai> flowerPaiList;
	private final ArrayList<Pai> noFlowerPaiList;

	public ArrayList<Pai> getFlowerPaiList() {
		return flowerPaiList;
	}

	public ArrayList<Pai> getNoFlowerPaiList() {
		return noFlowerPaiList;
	}

	private PaiType(int type, String name) {
		this.type = type;
		this.name = name;
		flowerPaiList = new ArrayList<>();
		noFlowerPaiList = new ArrayList<>();
		for (int tnum = 1; tnum <= 13; tnum++) {
			flowerPaiList.add(new Pai(tnum, this));
			if (tnum <= 10) {
				noFlowerPaiList.add(new Pai(tnum, this));
			}
		}
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	// 自动生成开始
	public static PaiType getEnum(int type) {
		switch (type) {
		case 1:
			return fang;
		case 2:
			return mei;
		case 3:
			return hong;
		case 4:
			return hei;
		default:
			return null;
		}
	}// 自动生成结束

	public Pai getNum(int num) {
		return this.flowerPaiList.get(num - 1);
	}
}