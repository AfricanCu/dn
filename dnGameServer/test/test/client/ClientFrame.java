package test.client;

import io.netty.channel.Channel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.protobuf.MessageLiteOrBuilder;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.net.MessageManager;

/**
 *
 * @author Administrator
 */
public class ClientFrame extends PlayTypeFrame implements ItemListener,
		ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private static final List<MessageImpl> list = Arrays.asList(MessageImpl
			.values());
	static {
		Collections.sort(list, new Comparator<MessageImpl>() {
			public int compare(MessageImpl o1, MessageImpl o2) {
				return o1.getMsgId().getType() > o2.getMsgId().getType() ? 1
						: -1;
			}
		});
	}
	public static final ClientFrame clientFrame = new ClientFrame("good");
	private final JComboBox<Object> urlComboBox = new JComboBox<Object>(
			TestClient.getURLs().toArray());
	public final JComboBoxEx channelComboBox = new JComboBoxEx();
	private final JTextField logincode = new JTextField();
	/** 登陆code备份 */
	private String logincodeBak;
	private final JComboBox<MessageImpl> msgIdComboBox = new JComboBox<>(
			list.toArray(new MessageImpl[list.size()]));
	/** 5 */
	private final JTextField content = new JTextField();
	public final JLabel tips = new JLabel("?");
	private final JFormattedTextField sameTimeSendNum = new JFormattedTextField(
			new java.text.DecimalFormat("#0"));

	private final JButton send = new JButton("发送");
	/** sessionCode登录 */
	public final JCheckBox sessionCodeCheck = new JCheckBox("会话登录");
	/** 每次关闭之前老客户端 */
	private final JCheckBox closeClientCheck = new JCheckBox("关闭客户端");
	public final JCheckBox ranAreaCheck = new JCheckBox("随机一个区");
	public final JCheckBox autoPreparedCheck = new JCheckBox("自动准备");
	private final Integer[] areas = new Integer[] { 0, 1, 2, 3, 4, 5 };
	public final JComboBox<Integer> areaComboBox = new JComboBox<>(areas);
	/** 机器人计数 第几号 */
	public final JFormattedTextField count = new JFormattedTextField(
			new java.text.DecimalFormat("#0"));
	public final JLabel channelCount = new JLabel("0");
	public final JLabel roomIdLb = new JLabel("0");
	private final JButton btn = new JButton("关闭选中");
	private final JButton allCloseBtn = new JButton("全部关闭");
	private final JButton ranDom = new JButton("随机客户端");
	private String ran = null;

	public static class JComboBoxEx extends JComboBox<ChannelItem> {
		private static final long serialVersionUID = 1L;

		@Override
		public synchronized void addItem(ChannelItem item) {
			super.addItem(item);
		}

		@Override
		public synchronized void removeItem(Object anObject) {
			super.removeItem(anObject);
		}
	};

	private ClientFrame(String title) {
		super(title);
		initComponents();
		urlComboBox.addItemListener(this);
		channelComboBox.addItemListener(this);
		msgIdComboBox.addItemListener(this);
		areaComboBox.addItemListener(this);

		sessionCodeCheck.addItemListener(this);
		closeClientCheck.addItemListener(this);
		ranAreaCheck.addItemListener(this);
		autoPreparedCheck.addItemListener(this);
		sessionCodeCheck.setSelected(false);
		closeClientCheck.setSelected(false);

		btn.addActionListener(this);
		send.addActionListener(this);
		ranDom.addActionListener(this);
		allCloseBtn.addActionListener(this);

		sameTimeSendNum.addKeyListener(this);
		count.addKeyListener(this);
		msgIdComboBox.setSelectedItem(MessageImpl.CreateRoom);
		areaComboBox.setSelectedIndex(1);
		sameTimeSendNum.setText("1");
		count.setText("1");
	}

	private void initComponents() {
		JPanel jPanel = new JPanel();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		Box userNameBox = new Box(BoxLayout.X_AXIS);
		userNameBox.add(new JLabel("登陆code："));
		userNameBox.add(logincode);

		Box msgIdBox = new Box(BoxLayout.X_AXIS);
		msgIdBox.add(new JLabel("协议id："));
		msgIdBox.add(msgIdComboBox);

		Box contentBox = new Box(BoxLayout.X_AXIS);
		contentBox.add(new JLabel("内容："));
		contentBox.add(content);
		contentBox.add(tips);
		contentBox.add(Box.createHorizontalStrut(10));
		contentBox.add(new JLabel("发送msg数："));
		contentBox.add(sameTimeSendNum);

		Box box_choose = new Box(BoxLayout.X_AXIS);
		addJComponent(box_choose, send);

		Box checkBox = new Box(BoxLayout.X_AXIS);
		checkBox.add(sessionCodeCheck);
		checkBox.add(closeClientCheck);
		checkBox.add(ranAreaCheck);
		checkBox.add(autoPreparedCheck);
		checkBox.add(new JLabel("选区："));
		checkBox.add(areaComboBox);

		checkBox.add(new JLabel("robot："));
		checkBox.add(count);

		Box btnBoxs = new Box(BoxLayout.X_AXIS);
		addJComponent(btnBoxs, channelCount);
		addJComponent(btnBoxs, btn);
		addJComponent(btnBoxs, ranDom);
		addJComponent(btnBoxs, allCloseBtn);

		Box displayBox = new Box(BoxLayout.X_AXIS);
		displayBox.add(roomIdLb);

		Box box = new Box(BoxLayout.Y_AXIS);
		box.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		box.add(Box.createVerticalStrut(20));
		box.add(urlComboBox);
		box.add(Box.createVerticalStrut(20));
		box.add(channelComboBox);
		box.add(Box.createVerticalStrut(20));
		box.add(userNameBox);
		box.add(Box.createVerticalStrut(20));
		box.add(msgIdBox);
		box.add(Box.createVerticalStrut(20));
		box.add(contentBox);
		box.add(Box.createVerticalStrut(20));
		super.getcreateRoomBox(box);
		box.add(Box.createVerticalStrut(20));
		box.add(box_choose);
		box.add(Box.createVerticalStrut(20));
		box.add(checkBox);
		box.add(Box.createVerticalStrut(20));
		box.add(btnBoxs);
		box.add(Box.createVerticalStrut(20));
		box.add(displayBox);
		new RobotClient(box);
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(box);
		setContentPane(jPanel);
	}

	public static void addJComponent(Container container, JComponent button) {
		button.setFont(new Font("宋体", Font.PLAIN, 15));
		Dimension dimension = new Dimension(150, 20);
		button.setMinimumSize(dimension);
		button.setMaximumSize(dimension);
		button.setPreferredSize(dimension);
		button.setAlignmentX(CENTER_ALIGNMENT);
		container.add(button);
	}

	public String getLogincode() {
		return logincode.getText().trim();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		if (source == channelComboBox) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				ChannelItem item = ((ChannelItem) channelComboBox
						.getSelectedItem());
				if (item != null) {
					TestClient.setCurrentChannel(item.getChannel());
					roomIdLb.setText(String.format("房:%s,座:%s,庄:%s,状态:%s",
							MessageImpl.getChannel(item.getChannel(),
									TestClient.roomIdIndex), MessageImpl
									.getChannel(item.getChannel(),
											TestClient.seatIndex), MessageImpl
									.getChannel(item.getChannel(),
											TestClient.bankerSeetIndex),
							MessageImpl.getChannel(item.getChannel(),
									TestClient.gameStateIndex)));

					logincode.setText((String) MessageImpl.getChannel(
							item.getChannel(), TestClient.puidIndex));
					logincodeBak = (String) MessageImpl.getChannel(
							item.getChannel(), TestClient.puidIndex);
				}
			} else {
				TestClient.setCurrentChannel(null);
			}
		} else if (source == areaComboBox) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Integer item = (Integer) areaComboBox.getSelectedItem();
				LoggerService.getLogicLog().error("选择{}", item);
			}
		} else if (source == msgIdComboBox) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				MessageImpl item = (MessageImpl) msgIdComboBox
						.getSelectedItem();
				LoggerService.getLogicLog().error("选择{}", item);
				this.tips.setText("?" + item.getTips());
				this.content.setText(item.getDefaultContent().toString());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.btn) {
			if (!TestClient.list.isEmpty()) {
				ChannelItem selectedItem = (ChannelItem) channelComboBox
						.getSelectedItem();
				selectedItem.getChannel().close();
			}
		} else if (source == this.allCloseBtn) {
			if (!TestClient.list.isEmpty()) {
				for (Iterator<Channel> iterator = TestClient.list.keySet()
						.iterator(); iterator.hasNext();) {
					iterator.next();
					iterator.remove();
				}
			}
		} else if (source == this.send) {
			send();
		} else if (source == this.ranDom) {
			if (ran == null || !this.logincode.getText().contains("ranUser")) {
				ran = this.logincode.getText() + "ranUser";
			}
			int parseInt = Integer.parseInt(count.getText());
			this.logincode.setText(ran + parseInt);
			count.setText("" + ++parseInt);
			send();
		}
	}

	private void send() {
		synchronized (this) {
			MessageImpl msgId = (MessageImpl) this.msgIdComboBox
					.getSelectedItem();
			String content = this.content.getText().trim();
			if (msgId != null) {
				boolean isNewComer = TestClient.getCurrentChannel() == null
						|| logincodeBak == null
						|| !logincodeBak.equals(this.getLogincode());
				if (this.closeClientCheck.isSelected()
						&& TestClient.getCurrentChannel() != null) {
					TestClient.list.remove(TestClient.getCurrentChannel());
				}
				if (isNewComer) {
					TestClient.setCurrentChannel(TestClient.connect(
							this.getSelectURL(), this.getLogincode(),
							ChannelType.Normal));
				}
				msgId.sendMessage(
						Integer.parseInt(this.sameTimeSendNum.getText()),
						TestClient.getCurrentChannel(), content);
			}
		}
	}

	public String getSelectURL() {
		return (String) urlComboBox.getSelectedItem();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Object source = e.getSource();
		if (source == sameTimeSendNum || source == count) {
			JFormattedTextField target = (JFormattedTextField) source;
			String old = target.getText();
			JFormattedTextField.AbstractFormatter formatter = target
					.getFormatter();
			if (!old.equals("")) {
				if (formatter != null) {
					String str = target.getText();
					try {
						long page = (Long) formatter.stringToValue(str);
						target.setText(page + "");
					} catch (ParseException pe) {
						target.setText("1");// 解析异常直接将文本框中值设置为1
					}
				}
			}
		}
	}
}
