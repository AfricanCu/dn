package com.wk.gm.bean;

import java.util.Collection;
import java.util.Date;

import com.wk.enun.ChargeStatus;
import com.wk.enun.DistrictType;
import com.wk.enun.OperationType;

public class ProxyChargeBean {
	private String orderId;// 订单号
	/** 玩家id或者代理id */
	private String uid;
	/** 店铺id */
	private int invitation;
	private ChargeStatus chargeStatus;// 状态0待处理1处理
	private int platId;
	/** 最终获得多少钻石 **/
	private int diamond;
	/** 花费多少钱 **/
	private String money;
	/** 充值时间 */
	private Date chargeTime;
	private Date orderTime;
	/** 充值操作 */
	private OperationType operation;
	/**区域*/
	private DistrictType district;

	public ProxyChargeBean() {
		super();
	}

	public ProxyChargeBean(String orderId, String uid, int invitation,
			ChargeStatus chargeStatus, int platId, int diamond, String money,
			Date chargeTime, Date orderTime, OperationType operation,
			DistrictType district) {
		super();
		this.orderId = orderId;
		this.uid = uid;
		this.invitation = invitation;
		this.chargeStatus = chargeStatus;
		this.platId = platId;
		this.diamond = diamond;
		this.money = money;
		this.chargeTime = chargeTime;
		this.orderTime = orderTime;
		this.operation = operation;
		this.district = district;
	}

	public void setStatus(int status) {
		this.chargeStatus = ChargeStatus.getEnum(status);
	}

	public int getStatus() {
		return chargeStatus.getType();
	}

	public ChargeStatus getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(ChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getUid() {
		return uid;
	}

	public int getInvitation() {
		return invitation;
	}

	public int getPlatId() {
		return platId;
	}

	public int getDiamond() {
		return diamond;
	}

	public String getMoney() {
		return money;
	}

	public Date getChargeTime() {
		return chargeTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setInvitation(int invitation) {
		this.invitation = invitation;
	}

	public void setPlatId(int platId) {
		this.platId = platId;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public void setOperation(OperationType operation) {
		this.operation = operation;
	}

	public OperationType getOperationType() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = OperationType.getEnum(operation);
	}

	public int getOperation() {
		return operation.getType();
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

}
