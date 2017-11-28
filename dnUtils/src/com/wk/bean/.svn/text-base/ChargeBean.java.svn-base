package com.wk.bean;

import java.sql.Timestamp;
import java.util.Date;

import com.wk.enun.ChargeStatus;
import com.wk.enun.DistrictType;

public class ChargeBean {
	/** 订单号 */
	private String orderId;
	/** 玩家唯一id */
	private long username;
	/** 区域 */
	private DistrictType district = DistrictType.yuanjiang;
	/** 代理邀请码 */
	private int agency;
	/** 充值钻石数 */
	private int diamond;
	/** 金额 */
	private String money;
	/** 苹果回执 **/
	private String apple_receipt;
	/** 状态0待处理1处理 */
	private ChargeStatus status;
	/** 充值时间 */
	private Date chargeTime;
	private Timestamp updateTime;

	public ChargeBean() {
		super();
	}

	public ChargeBean(String orderId, long username, DistrictType districtType,
			int agency, int diamond, String money, String apple_receipt,
			ChargeStatus status, Date chargeTime, Timestamp updateTime) {
		super();
		this.orderId = orderId;
		this.username = username;
		this.district = districtType;
		this.agency = agency;
		this.diamond = diamond;
		this.money = money;
		this.apple_receipt = apple_receipt;
		this.status = status;
		this.chargeTime = chargeTime;
		this.updateTime = updateTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public long getUsername() {
		return username;
	}

	public int getDistrict() {
		return district.getType();
	}

	public void setDistrict(int district) {
		this.district = DistrictType.getEnum(district);
	}

	public DistrictType getDistrictType() {
		return district;
	}

	public int getAgency() {
		return agency;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public String getApple_receipt() {
		return apple_receipt;
	}

	public void setStatus(int status) {
		this.status = ChargeStatus.getEnum(status);
	}

	public int getStatus() {
		return status.getType();
	}

	public ChargeStatus getChargeStatus() {
		return status;
	}

	public Date getChargeTime() {
		return chargeTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
}
