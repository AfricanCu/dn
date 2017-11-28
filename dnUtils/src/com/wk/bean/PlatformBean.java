package com.wk.bean;

import java.util.Date;

/**
 * 数据统计
 * @author ems
 *
 */
public class PlatformBean {

	private Date dataTime;//数据时间
	private int dau;//日活跃
	private int wau;//周活跃
	private int mau;//月活跃
	private int total;//总用户数
	private int register;//注册人数
	private int credit;//充值金额
	private int sumCredit;//充值总额
	
	public int getSumCredit() {
		return sumCredit;
	}
	public void setSumCredit(int sumCredit) {
		this.sumCredit = sumCredit;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Date getDataTime() {
		return dataTime;
	}
	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}
	public int getDau() {
		return dau;
	}
	public void setDau(int dau) {
		this.dau = dau;
	}
	public int getWau() {
		return wau;
	}
	public void setWau(int wau) {
		this.wau = wau;
	}
	public int getMau() {
		return mau;
	}
	public void setMau(int mau) {
		this.mau = mau;
	}
	
	
	
}
