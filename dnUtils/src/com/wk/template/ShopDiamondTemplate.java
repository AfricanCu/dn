package com.wk.template;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.wk.I.EditableFile;
import com.wk.enun.DistrictType;
import com.wk.util.ReadUtil;
import com.wk.util.TemplateCheckAbs;

public class ShopDiamondTemplate implements TemplateCheckAbs {

	private static LinkedHashMap<Integer, ShopDiamondTemplate> shopMap;

	private static EditableFile edit = null;

	/**
	 * 解析静态数据
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static EditableFile init(String csvDir) throws Exception {
		if (edit == null) {
			edit = new EditableFile(new File(csvDir)) {
				public void run() throws Exception {
					List<ShopDiamondTemplate> shopList = ReadUtil
							.explainCsvData(getPath(),
									ShopDiamondTemplate.class, true);
					LinkedHashMap<Integer, ShopDiamondTemplate> hashMap = new LinkedHashMap<Integer, ShopDiamondTemplate>();
					for (ShopDiamondTemplate shopDiamondTemplate : shopList) {
						hashMap.put(shopDiamondTemplate.iD, shopDiamondTemplate);
					}
					shopMap = hashMap;
				}
			};
		}
		return edit;
	}

	private int iD;
	/** 区域 */
	private DistrictType district;
	/** 充值数量 */
	private int rechargeNum;
	/** 赠送数量 */
	private int giftNum;
	/** 花费RMB（单位：元） */
	private int spendNum;
	// //////////////////////////////
	/** 花费RMB（单位：元）xx.xx **/
	private String money;
	/** 花费RMB（单位：分） xx **/
	private String moneyInFen;
	/** 商品名 */
	private String subject;
	/** 内容 */
	private String body;
	/** 描述 */
	private String desc;
	/** 展示内容 */
	private String dis;
	/** 总共获得多少钻石 **/
	private int totalDiamond;

	public int getiD() {
		return iD;
	}

	public int getDistrict() {
		return district.getType();
	}

	public void setDistrict(int district) throws Exception {
		this.district = DistrictType.getEnum(district);
		if (this.district == null) {
			throw new Exception(String.format("找不到区域类型！district：%s", district));
		}
	}

	public DistrictType getDistrictType() {
		return district;
	}

	public String getMoney() {
		return money;
	}

	public String getMoneyInFen() {
		return moneyInFen;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public String getDesc() {
		return desc;
	}

	public String getDis() {
		return dis;
	}

	public int getTotalDiamond() {
		return totalDiamond;
	}

	public int getRechargeNum() {
		return rechargeNum;
	}

	public int getGiftNum() {
		return giftNum;
	}

	public int getSpendNum() {
		return spendNum;
	}

	public static ShopDiamondTemplate getShopDiamondTemplate(int pay_id) {
		return shopMap.get(pay_id);
	}

	public static List<ShopDiamondTemplate> listShopDiamondTemplate(int district) {
		List<ShopDiamondTemplate> templates = new ArrayList<ShopDiamondTemplate>();
		for (Integer key : shopMap.keySet()) {
			if (shopMap.get(key).getDistrict() == district) {
				templates.add(shopMap.get(key));
			}
		}

		return templates;
	}

	@Override
	public void check() throws Exception {
		this.totalDiamond = rechargeNum + giftNum;
		this.money = String.format("%.2f", this.spendNum * 1.0f);
		this.moneyInFen = String.valueOf(this.spendNum * 100);
		this.subject = String.format("%s+%s钻石", this.rechargeNum, this.giftNum);
		this.body = String.format("花费%s元", getMoney());
		this.desc = String.format("(%s+%s)钻石&nbsp</br>&nbsp&nbsp%s元",
				rechargeNum, giftNum, getMoney());
		this.dis = String.format("获得：%s+%s颗钻石</br>总计：%s颗钻石</br>应付：%s元",
				rechargeNum, giftNum, this.totalDiamond, getMoney());
	}

}
