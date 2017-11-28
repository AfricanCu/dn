package test.client.util;

import java.util.HashMap;
import java.util.List;

import com.wk.util.ReadUtil;
import com.wk.util.TemplateCheckAbs;

/**
 * 游戏配置
 * 
 * @author ems
 *
 */
public class NoticeTextTemplate implements TemplateCheckAbs {

	private static HashMap<Integer, NoticeTextTemplate> configTemplate;

	public static void explain(String string) throws Exception {
		List<NoticeTextTemplate> tmp = ReadUtil.explainCsvData(string,
				NoticeTextTemplate.class, true);
		HashMap<Integer, NoticeTextTemplate> map = new HashMap<>();
		for (NoticeTextTemplate tp : tmp) {
			map.put(tp.getValue(), tp);
		}
		configTemplate = map;
	}

	public static String getNoticeText(int id) {
		NoticeTextTemplate xx = configTemplate.get(id);
		if (xx == null) {
			return String.format("未加入错误提示！code：%s", id);
		}
		return xx.getText();
	}

	private int value;
	private String text;

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	@Override
	public void check() throws Exception {
	}

}
