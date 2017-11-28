package com.wk.gm.bean;

import java.util.Date;

import com.wk.enun.OperationType;

public class AgencyBroadcast {

	private long id;
	private int agency;
	private OperationType operation;
	private int card;
	private float gains;
	private String nickName;
	private long uid;
	private String head;
	private Date createTime;
	private int zhi;
	private int dai;
	private int zong;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getAgency() {
		return agency;
	}
	public void setAgency(int agency) {
		this.agency = agency;
	}
	public int getOperation(){
		return operation.getType();
	}	
	public OperationType getOperationType() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = OperationType.getEnum(operation);
	}
	public int getCard() {
		return card;
	}
	public void setCard(int card) {
		this.card = card;
	}
	public float getGains() {
		return gains;
	}
	public void setGains(float gains) {
		this.gains = gains;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getZhi() {
		return zhi;
	}
	public void setZhi(int zhi) {
		this.zhi = zhi;
	}
	public int getDai() {
		return dai;
	}
	public void setDai(int dai) {
		this.dai = dai;
	}
	public int getZong() {
		return zong;
	}
	public void setZong(int zong) {
		this.zong = zong;
	}
	public AgencyBroadcast(int agency, OperationType operation,
			int card, float gains, String nickName, long uid, String head,
			Date createTime) {
		super();
		this.agency = agency;
		this.operation = operation;
		this.card = card;
		this.gains = gains;
		this.nickName = nickName;
		this.uid = uid;
		this.head = head;
		this.createTime = createTime;
	}
	public AgencyBroadcast() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
