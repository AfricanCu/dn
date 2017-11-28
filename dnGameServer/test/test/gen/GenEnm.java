package test.gen;

import com.wk.logic.enm.Bunko;
import com.wk.logic.enm.ChatType;
import com.wk.logic.enm.GameState;
import com.wk.logic.enm.GuildGTGType;
import com.wk.logic.enm.MemberDisType;
import com.wk.logic.enm.RoundType;
import com.wk.logic.enm.SwType;
import com.wk.logic.enm.TimesRule;
import com.wk.server.logic.item.ItemTemplate;
import com.wk.server.logic.room.BetTimesType;
import com.wk.server.logic.room.NiuType;
import com.wk.server.logic.room.RoomTaskType;

public class GenEnm extends GenInnerEnm {
	public static void main(String[] args) throws Exception {
		innerMsgId(Bunko.values());
		innerMsgId(ChatType.values());
		innerMsgId(GameState.values());
		innerMsgId(RoundType.values());

		innerMsgId(RoomTaskType.values());
		innerMsgId(BetTimesType.values());
		innerMsgId(NiuType.values());

		innerMsgId(SwType.values());
		innerMsgId(TimesRule.values());
		innerMsgId(ItemTemplate.values());
		innerMsgId(MemberDisType.values());
		innerMsgId(GuildGTGType.values());
		System.err.println("--------GenMsg------------");
		GenMsg.msgId();
	}
}
