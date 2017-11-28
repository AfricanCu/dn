package gm.server.logic.continuation;

import gm.server.logic.config.ServerTemplate;
import msg.InnerMessage.KickBusToGsbk;

import com.wk.bean.NTxtAbs;

public class KickProcessor extends
		ContinuationProcessor<Long, KickBusToGsbk, KickContinuationListener> {

	private static final KickProcessor instance = new KickProcessor();

	public static KickProcessor getInstance() {
		return instance;
	}

	/**
	 * 加入踢人队列
	 * 
	 * @param lastServerT
	 * @param key
	 * @param value
	 * @return 是否加入成功
	 */
	public int putKickUser(ServerTemplate lastServerT, long key,
			KickContinuationListener value) {
		if (this.contains(key)) {
			return NTxtAbs.KICK_PUT_CONTAINS;
		}
		if (lastServerT.checkClient() != null) {
			this.put(key, value);
			return NTxtAbs.SUCCESS;
		}
		return NTxtAbs.KICK_NOT_CONNECTED;
	}

	public void kickbk(KickBusToGsbk genMessageLite) {
		this.remove(genMessageLite.getUid(), genMessageLite);
	}
}
