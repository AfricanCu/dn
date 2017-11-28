package gm.server.logic.config;

import com.wk.template.DistrictTemplate;
import com.wk.template.ShopDiamondTemplate;

/**
 * 静态数据管理器
 * 
 * 
 * @author taohuiliang
 * @date 2013-2-27
 * @version v1.0
 */
public class StaticDataManager {
	/**
	 * 解析静态数据
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void init(String csvDir) throws Exception {
		ShopDiamondTemplate.init(csvDir + "ShopDiamond.csv").run();
		ServerTemplate.init(csvDir + "Server.csv");
		DistrictTemplate.explain(csvDir + "District.csv");
	}
}
