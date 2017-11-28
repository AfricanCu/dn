package com.wk.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wk.enun.DistrictType;
import com.wk.util.ReadUtil;
import com.wk.util.TemplateCheckAbs;

/**
 * 区域配置
 * 
 * @author ems
 *
 */
public class DistrictTemplate implements TemplateCheckAbs {
	private static Map<DistrictType, DistrictTemplate> distinctTemplateMap;

	public static void explain(String string) throws Exception {
		List<DistrictTemplate> xx = ReadUtil.explainCsvData(string,
				DistrictTemplate.class, true);
		Map<DistrictType, DistrictTemplate> map = new HashMap<DistrictType, DistrictTemplate>();
		for (DistrictTemplate x : xx) {
			x.districtType = DistrictType.getEnum(x.iD);
			if (x.districtType == null) {
				throw new Exception(String.format("找不到区域%s", x.iD));
			}
			map.put(x.districtType, x);
		}
		distinctTemplateMap = map;
	}

	public static DistrictTemplate getDistinctTemplate(DistrictType districtType) {
		return distinctTemplateMap.get(districtType);
	}

	private int iD;
	private int initDiamond;
	private String notice;
	// ///////////
	private DistrictType districtType;

	@Override
	public void check() throws Exception {
		this.districtType = DistrictType.getEnum(this.iD);
		if (this.districtType == null) {
			throw new Exception(String.format("找不到区域%s", this.iD));
		}
	}

	public int getiD() {
		return iD;
	}
	
	

	public int getInitDiamond() {
		return initDiamond;
	}

	public String getNotice() {
		return notice;
	}

}
