package gm.server.logic.continuation;

import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;

public class ContinuationProcessor<K, BAK extends MessageLite, V extends ContinuationListenerExImpl<K, BAK>> {
	/**
	 * 数据等待消息返回队列
	 * 
	 * [玩家id,ContinuationListenerImpl]
	 */
	private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

	/**
	 * 移出队列
	 * 
	 * @param key
	 * @param bak
	 *            返回的消息
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key
	 */
	public V remove(K key, BAK bak) {
		V remove = map.remove(key);
		if (remove == null) {
			LoggerService.getLogicLog().error("没有移除成功！{}", key);
		} else {
			LoggerService.getLogicLog().error("移除成功！{}", key);
		}
		if (remove != null && bak != null) {
			remove.onDone(bak);
		}
		return remove;
	}

	/**
	 * 是否还在队列中
	 * 
	 * @param key
	 * @return true if and only if some key maps to the value argument in this
	 *         table as determined by the equals method; false otherwise
	 */
	public boolean contains(K key) {
		return map.contains(key);
	}

	/**
	 * 加入队列
	 * 
	 * @param key
	 * @param value
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key
	 */
	public V put(K key, V value) {
		V put = map.put(key, value);
		if (value != null) {
			value.suspend();
		}
		return put;
	}

}
