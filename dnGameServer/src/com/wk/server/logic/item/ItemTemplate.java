package com.wk.server.logic.item;

/**
 * 道具模板
 * 
 * @author ems
 *
 */
public enum ItemTemplate {
	/**
	 * 蓝钻
	 */
	Diamond_ID(1);

	private int id;

	private ItemTemplate(int id) {
		this.id = id;
	}

	public int getType() {
		return id;
	}

	// 自动生成开始
public static ItemTemplate getEnum(int type){
switch(type) {
case 1:
  return Diamond_ID;
default:
  return null;
}
}// 自动生成结束
}