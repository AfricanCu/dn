package com.wk.enun;

import com.wk.template.DistrictTemplate;

/**
 * 区域类型
 * 
 * @author ems
 *
 */
public enum DistrictType {
	/***/
	yuanjiang(1, "沅江市"),
	/***/

	;
	private final int type;
	private final String district;

	private DistrictType(int type, String district) {
		this.type = type;
		this.district = district;
	}

	public int getType() {
		return type;
	}

	public String getDistrict() {
		return district;
	}
	
	public DistrictTemplate getDistrictTemplate() {
		return DistrictTemplate.getDistinctTemplate(this);
	}

	// 自动生成开始
public static DistrictType getEnum(int type){
switch(type) {
case 1:
  return yuanjiang;
default:
  return null;
}
}// 自动生成结束
}