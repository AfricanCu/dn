import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.util.LRUMap;

public class TestLru {
	public static void main(String[] args) {
		LoggerService.initDef();
		Map<String, String> lruMap = Collections
				.synchronizedMap(new LRUMap<String, String>(2) {
					private static final long serialVersionUID = 1L;
					private java.util.Map.Entry<String, String> eldest;

					@Override
					protected boolean removeEldestEntry(
							java.util.Map.Entry<String, String> eldest) {
						boolean removeEldestEntry = super
								.removeEldestEntry(eldest);
						if (!removeEldestEntry) {
							LoggerService.getLogicLog().error("无老的移除！");
							return false;
						}
						if (eldest.getKey().equals("2")) {
							this.eldest = eldest;
							LoggerService.getLogicLog().error("=2,不能移除！");
						} else {
							LoggerService.getLogicLog().error("移除老的！{}",
									eldest.toString());
						}
						return true;
					}

					@Override
					public String put(String key, String value) {
						String put = super.put(key, value);
						if (eldest != null) {
							java.util.Map.Entry<String, String> tmp = eldest;
							eldest = null;
							this.put(tmp.getKey(), tmp.getValue());
						}
						return put;
					}
				});
		lruMap.put("1", "aaaa");
		lruMap.put("2", "bbbb");
		lruMap.put("1", "cccc");
		LoggerService.getLogicLog().error("size:{}", lruMap.size());
		lruMap.put("3", "dddd");
		LoggerService.getLogicLog().error("size:{}", lruMap.size());
		lruMap.put("4", "dddd");
		LoggerService.getLogicLog().error("size:{}", lruMap.size());
		for (Entry<String, String> xx : lruMap.entrySet()) {
			LoggerService.getLogicLog().error("{},{}", xx.getKey(),
					xx.getValue());
		}
	}
}
