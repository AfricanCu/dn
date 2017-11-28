package com.wk.gm.bean;

import java.util.Date;

import com.wk.enun.OperationType;

public class AgencyBillBean {
	
	private long id;
	private String orderId;
	private OperationType operation;
	private int agency;
	private float money;
	private float rebate;
	private Date createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	

	public OperationType getOperationType() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = OperationType.getEnum(operation);
	}
	public int getOperation(){
		return operation.getType();
	}
	public int getAgency() {
		return agency;
	}
	public void setAgency(int agency) {
		this.agency = agency;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public float getRebate() {
		return rebate;
	}
	public void setRebate(float rebate) {
		this.rebate = rebate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
