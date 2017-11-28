package test.client;

import io.netty.util.internal.ThreadLocalRandom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.wk.util.TimeTaskUtil;

public class RobotClient implements ActionListener, KeyListener {
	private final static int quan = 100000;
	private final static String robotName = "robot";
	private final static AtomicInteger count = new AtomicInteger(0);
	// 0
	private final JTextField periodNum = new JTextField("100", 10);
	private final JTextField periodClientNum = new JTextField("100", 10);
	// 1

	private final JTextField perSecondNum = new JTextField("1", 10);
	// 2

	private final JTextField msgPeriodNum = new JTextField("2", 10);;
	private final JTextField playTimeNum = new JTextField("" + 60 * 30, 10);
	private final JTextField offLineRateNum = new JTextField("3", 10);
	private final JButton connectBtn = new JButton("启动");
	private final JButton closeBtn = new JButton("结束");
	private final JTabbedPane tab = new JTabbedPane();
	private final List<JTextField> checkNumList = new ArrayList<>();

	public void registerKeyListener(JTextField... field) {
		this.checkNumList.addAll(Arrays.asList(field));
		for (JTextField f : field)
			f.addKeyListener(this);
	}

	public RobotClient(Box box) {
		registerKeyListener(periodNum, periodClientNum, perSecondNum);
		JPanel ranLogP = new JPanel();
		ranLogP.add(new JLabel("时间（单位：秒）："));
		ranLogP.add(periodNum);
		ranLogP.add(new JLabel("客户端数："));
		ranLogP.add(periodClientNum);

		JPanel perSecLogP = new JPanel();
		perSecLogP.add(new JLabel("每秒登录数："));
		perSecLogP.add(perSecondNum);

		JPanel gameP = new JPanel();
		gameP.add(new JLabel("游戏类型："));


		tab.add(ranLogP, "客户端时间段随机登录");
		tab.add(perSecLogP, "客户端每秒登录");
		tab.add(gameP, "游戏机器人");

		registerKeyListener(msgPeriodNum, playTimeNum, offLineRateNum);

		Box clientSetBox = new Box(BoxLayout.X_AXIS);
		clientSetBox.add(new JLabel("消息发送间隔（单位：秒）："));
		clientSetBox.add(msgPeriodNum);
		clientSetBox.add(new JLabel("运行时间（秒）："));
		clientSetBox.add(playTimeNum);
		clientSetBox.add(new JLabel("断线权重("));
		clientSetBox.add(offLineRateNum);
		clientSetBox.add(new JLabel("/" + quan + ")"));

		connectBtn.addActionListener(this);
		closeBtn.addActionListener(this);

		Box btnBox = new Box(BoxLayout.X_AXIS);
		btnBox.add(connectBtn);
		btnBox.add(closeBtn);

		box.add(Box.createVerticalStrut(20));
		box.add(tab);
		box.add(Box.createVerticalStrut(20));
		box.add(clientSetBox);
		box.add(Box.createVerticalStrut(20));
		box.add(btnBox);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectBtn) {
			ClientFrame.clientFrame.ranAreaCheck.setSelected(true);
			ClientFrame.clientFrame.ranAreaCheck.setEnabled(false);
			int periodInMillis = Integer.parseInt(periodNum.getText()) * 1000;
			int periodClientNumber = Integer
					.parseInt(periodClientNum.getText());

			int perSecondNumber = Integer.parseInt(perSecondNum.getText());

			int msgPeriodNumber = Integer.parseInt(this.msgPeriodNum.getText());
			int playTimeInMillis = Integer.parseInt(this.playTimeNum.getText()) * 1000;
			int offLineRate = Integer.parseInt(offLineRateNum.getText());
			int selectedIndex = this.tab.getSelectedIndex();
			final String url = ClientFrame.clientFrame.getSelectURL();
			switch (selectedIndex) {
			case 0:
				if (periodInMillis > 0 && periodClientNumber > 0) {
					for (int i = 0; i < periodClientNumber; i++) {
						TimeTaskUtil.getTaskmanager().submitOneTimeTask(
								new Runnable() {

									@Override
									public void run() {
										TestClient.connect(url, robotName
												+ count.getAndIncrement(),
												ChannelType.Normal);
									}
								},
								ThreadLocalRandom.current().nextInt(
										periodInMillis), TimeUnit.MILLISECONDS);
					}
				}
				break;
			case 1:
				if (perSecondNumber > 0) {
					for (int i = 0; i < perSecondNumber; i++) {
						TimeTaskUtil.getTaskmanager().submitOneTimeTask(
								new Runnable() {

									@Override
									public void run() {
										TestClient.connect(url, robotName
												+ count.getAndIncrement(),
												ChannelType.Normal);
									}
								}, 1, TimeUnit.SECONDS);
					}
				}
				break;
			case 2:
				break;
			}
		} else if (e.getSource() == closeBtn) {
			ClientFrame.clientFrame.ranAreaCheck.setEnabled(true);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (checkNumList.contains(arg0.getSource())) {
			JTextField field = (JTextField) arg0.getSource();
			field.setText(field.getText().replaceAll("\\D", ""));
		}
		if (offLineRateNum == arg0.getSource()) {
			if (!offLineRateNum.getText().equals("")) {
				int logoutRan = Integer.parseInt(offLineRateNum.getText());
				if (logoutRan > 32 || logoutRan < 0) {
					logoutRan = 0;
					offLineRateNum.setText(logoutRan + "");
				}
			}
		}
	}

}