package com.wk.server.logic.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wk.logic.config.ConfigTemplate;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.PType;
import com.wk.puke.Pai;

/**
 * 牛类型
 * 
 * @author ems
 */
public enum NiuType {
	/****/
	zero(0, "无牛", new int[] { 1, 1 }),
	/****/
	one(1, "牛1", new int[] { 1, 1 }),
	/****/
	two(2, "牛2", new int[] { 1, 2 }),
	/****/
	three(3, "牛3", new int[] { 1, 3 }),
	/****/
	four(4, "牛4", new int[] { 1, 4 }),
	/****/
	five(5, "牛5", new int[] { 1, 5 }),
	/****/
	six(6, "牛6", new int[] { 1, 6 }),
	/****/
	seven(7, "牛7", new int[] { 2, 7 }),
	/****/
	eight(8, "牛8", new int[] { 2, 8 }),
	/****/
	nine(9, "牛9", new int[] { 2, 9 }),
	/****/
	niuNiu(10, "牛牛", new int[] { 3, 10 }),
	/** 牌型类 **/
	shunZi(11, "顺子", new int[] { 5, 15 }),
	/** 牌型类 **/
	tongHua(12, "同花", new int[] { 5, 16 }),
	/** 牌型类 **/
	huLu(13, "葫芦", new int[] { 5, 17 }),
	/** 牌型类 **/
	wuXiaoNiu(14, "五小牛", new int[] { 5, 18 }),
	/** 牌型类 **/
	wuHuaNiu(15, "五花牛", new int[] { 5, 19 }),
	/** 牌型类 **/
	zhaDan(16, "炸弹", new int[] { 5, 20 }),
	/** 牌型类 **/
	tongHuaShun(17, "同花顺", new int[] { 5, 25 }), ;

	private final int type;
	private final String name;
	/** 见牛翻倍数 */
	private final int seeNiuTimesTimes;
	/** 经典斗牛倍数 */
	private final int pubBullTimes;

	private NiuType(int type, String name, int[] times) {
		this.type = type;
		this.name = name;
		this.pubBullTimes = times[0];
		this.seeNiuTimesTimes = times[1];
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getPubBullTimes() {
		return pubBullTimes;
	}

	public int getSeeNiuTimesTimes() {
		return this.seeNiuTimesTimes;
	}
	/**
	 * 计算牌型，仅计算 顺子 同花 葫芦 五小牛 五花牛 炸弹 同花顺 这七种类型
	 * @param pais
	 * @return
	 */
	/**
	 * 计算牌型类牛牛类型
	 * 
	 * @param pais
	 * @return
	 */
	public static NiuType calcPaiXing(List<Pai> pais) {
		pais = new ArrayList<>(pais);
		Collections.sort(pais);
		boolean tonghua = true;
		for (int index = 1; index < pais.size(); index++) {
			//如果存在有相邻两张花色不一样，就不是同花
			if (pais.get(index).getColor() != pais.get(index - 1).getColor()) {
				tonghua = false;
				break;
			}
		}
		boolean shunzi = true;
		for (int index = 1; index < pais.size(); index++) {
			//如果存在相应两张牌数字差不是1，则不是顺子
			if (pais.get(index).getNum() != pais.get(index - 1).getNum() + 1) {
				shunzi = false;
				break;
			}
		}
		//既是同花又是顺子，返回同花顺
		if (tonghua && shunzi) {
			return tongHuaShun;
		}
		if (shunzi)
			return shunZi;// 如果是顺子，，不可能还是炸弹，五花肉，五小牛，葫芦
		if (tonghua)
			return tongHua;// 如果是同花，，不可能还是炸弹，五花肉，五小牛，葫芦
		
		//因为被排序过，炸弹的话要么前四张相同，要么后四张相同。
		if ((pais.get(0).getNum() == pais.get(1).getNum()
				&& pais.get(1).getNum() == pais.get(2).getNum() && pais.get(2)
				.getNum() == pais.get(3).getNum())
				|| (pais.get(1).getNum() == pais.get(2).getNum()
						&& pais.get(2).getNum() == pais.get(3).getNum() && pais
						.get(3).getNum() == pais.get(4).getNum())) {
			return zhaDan;
		}
		boolean wuhuaniu = true;
		for (Pai pai : pais) {
			//遍历五张牌，如果有一张小于等于10就不是五花牛
			if (pai.getNum() <= 10) {
				wuhuaniu = false;
				break;
			}
		}
		if (wuhuaniu) {
			return wuHuaNiu;
		}
		boolean wuxiaoniu = true;
		int totalNum = 0;
		for (Pai pai : pais) {
			totalNum += pai.getNum();
			//如果五张牌点数和大于10就不是五小牛
			if (totalNum > 10) {
				wuxiaoniu = false;
				break;
			}
		}
		if (wuxiaoniu)
			return wuXiaoNiu;
		//葫芦是三张点数相同，外加一对
		if ((pais.get(0).getNum() == pais.get(1).getNum()
				&& pais.get(1).getNum() == pais.get(2).getNum() && pais.get(3)
				.getNum() == pais.get(4).getNum())
				|| (pais.get(0).getNum() == pais.get(1).getNum()
						&& pais.get(2).getNum() == pais.get(3).getNum() && pais
						.get(3).getNum() == pais.get(4).getNum())) {
			return huLu;
		}
		return null;
	}

	/** 组成牛 */
	public static final int niuArrays[][] = new int[][] { { 0, 1, 2, 3, 4 },
			{ 0, 1, 3, 2, 4 }, { 0, 1, 4, 2, 3 }, { 0, 2, 3, 1, 4 },
			{ 0, 2, 4, 1, 3 }, { 0, 3, 4, 1, 2 }, { 1, 2, 3, 0, 4 },
			{ 1, 2, 4, 0, 3 }, { 1, 3, 4, 0, 2 }, { 2, 3, 4, 0, 1 } };

	/**
	 * 计算牛
	 * 
	 * @param pais
	 * @return [牛几，哪几个拼成牛（无牛为空）]
	 */
	public static Object[] calcNiuType(List<Pai> pais) {
		Object obj[] = new Object[2];
		NiuType niuType = NiuType.calcPaiXing(pais);
		int[] niuIndexs = null;
		if (niuType == null)
			for (int[] array : niuArrays) {
				int point = pais.get(array[0]).getPoint()
						+ pais.get(array[1]).getPoint()
						+ pais.get(array[2]).getPoint();
				if (point == 10 || point == 20 || point == 30) {
					niuIndexs = new int[] { array[0] + 1, array[1] + 1,
							array[2] + 1 };
					int tniu = pais.get(array[3]).getPoint()
							+ pais.get(array[4]).getPoint();
					if (tniu == 20 && point == 30) {
						niuType = NiuType.niuNiu;
					} else {
						niuType = NiuType.getEnum(tniu > 10 ? tniu - 10 : tniu);
					}
					break;
				}
			}
		if (niuType == null) {
			niuType = NiuType.zero;
		}
		obj[0] = niuType;
		obj[1] = niuIndexs;
		return obj;
	}

	/**
	 * 计算是否只是牛5-9
	 * 
	 * @param pais
	 * @return 如果是返回[牛几，哪几个拼成牛] 否则返回空
	 */
	public static Object[] isFiveNineNiu(List<Pai> pais) {

		NiuType niuType = NiuType.calcPaiXing(pais);
		if (niuType != null) {
			return null;
		}
		int[] niuIndexs = null;
		if (niuType == null)
			for (int[] array : niuArrays) {
				int point = pais.get(array[0]).getPoint()
						+ pais.get(array[1]).getPoint()
						+ pais.get(array[2]).getPoint();
				if (point == 10 || point == 20 || point == 30) {
					niuIndexs = new int[] { array[0] + 1, array[1] + 1,
							array[2] + 1 };
					int tniu = pais.get(array[3]).getPoint()
							+ pais.get(array[4]).getPoint();
					if (tniu == 20 && point == 30) {
						niuType = NiuType.niuNiu;
					} else {
						niuType = NiuType.getEnum(tniu > 10 ? tniu - 10 : tniu);
					}
					break;
				}
			}
		if (niuType == null) {
			niuType = NiuType.zero;
		}
		if (ConfigTemplate.getConfigTemplate().isNiuToNiu(niuType)) {
			return new Object[] { niuType, niuIndexs };
		}
		return null;
	}

	// 自动生成开始
	public static NiuType getEnum(int type) {
		switch (type) {
		case 0:
			return zero;
		case 1:
			return one;
		case 2:
			return two;
		case 3:
			return three;
		case 4:
			return four;
		case 5:
			return five;
		case 6:
			return six;
		case 7:
			return seven;
		case 8:
			return eight;
		case 9:
			return nine;
		case 10:
			return niuNiu;
		case 11:
			return shunZi;
		case 12:
			return tongHua;
		case 13:
			return huLu;
		case 14:
			return wuXiaoNiu;
		case 15:
			return wuHuaNiu;
		case 16:
			return zhaDan;
		case 17:
			return tongHuaShun;
		default:
			return null;
		}
	}// 自动生成结束

	public int getTimes(PType pType) {
		switch (pType) {
		case PUB_DULL:
			return this.pubBullTimes;
		case SEE_NIU_TIMES:
			return this.seeNiuTimesTimes;
		default:
			NTxt.println("未实现！");
			return 0;
		}
	}

}