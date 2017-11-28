package test.client;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import msg.RoomMessage.PlayType;

import com.wk.logic.enm.BankerMode;
import com.wk.logic.enm.PType;
import com.wk.logic.enm.RoundType;

public class PlayTypeFrame extends ApplicationFrame {

	public PlayTypeFrame(String title) {
		super(title);
	}

	private static final long serialVersionUID = 1L;
	private final ButtonGroup bankerbg = new ButtonGroup();
	private final ButtonGroup pTypebg = new ButtonGroup();
	private final ButtonGroup roundbg = new ButtonGroup();

	public void getcreateRoomBox(Box box) {
		Box createRoomBox = new Box(BoxLayout.X_AXIS);
		for (BankerMode area : BankerMode.values()) {
			JRadioButton jRadioButton = new JRadioButton(area.name());
			bankerbg.add(jRadioButton);
			createRoomBox.add(jRadioButton);
		}
		bankerbg.getElements().nextElement().setSelected(true);
		for (PType area : PType.values()) {
			JRadioButton jRadioButton = new JRadioButton(area.name());
			pTypebg.add(jRadioButton);
			createRoomBox.add(jRadioButton);
		}
		pTypebg.getElements().nextElement().setSelected(true);
		for (RoundType area : RoundType.values()) {
			JRadioButton jRadioButton = new JRadioButton(area.name());
			roundbg.add(jRadioButton);
			createRoomBox.add(jRadioButton);
		}
		roundbg.getElements().nextElement().setSelected(true);
		box.add(createRoomBox);
	}

	public int getBankerMode() {
		return ((BankerMode) getSelect(BankerMode.values(), bankerbg))
				.getType();
	}

	public static Enum<?> getSelect(Enum<?>[] enums, ButtonGroup bg) {
		int count = 0;
		for (Enumeration<AbstractButton> e = bg.getElements(); e
				.hasMoreElements(); count++)
			if (e.nextElement().isSelected()) {
				break;
			}
		return enums[count];
	}

	public PlayType getPlayType() {
		return PlayType.newBuilder().setBankerMode(this.getBankerMode())
				.setPType(this.getPType()).setRound(this.getRoundType())
				.build();
	}

	private int getPType() {
		return ((PType) getSelect(PType.values(), pTypebg)).getType();
	}

	private int getRoundType() {
		return ((RoundType) getSelect(RoundType.values(), roundbg)).getType();
	}

}
