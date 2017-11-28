package test.client;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSetExChannel extends
		ConcurrentHashMap<Channel, Object> {

	private static final long serialVersionUID = 1L;

	private void closeXx(Channel remove) {
		if (remove == null) {
			return;
		}
		remove.close();
		ClientFrame.clientFrame.channelCount.setText(this.size() + "");
		ClientFrame.clientFrame.channelComboBox.removeItem(MessageImpl
				.getChannel(remove, TestClient.item_index));
	}

	public Object remove(Object key) {
		Object remove = super.remove(key);
		closeXx((Channel) key);
		return remove;
	};

	public Object put(Channel key, Object value) {
		if (key == null) {
			return null;
		}
		Object add = super.put(key, value);
		ClientFrame.clientFrame.channelCount.setText(this.size() + "");
		ChannelItem item = (ChannelItem) MessageImpl.getChannel(key,
				TestClient.item_index);
		ClientFrame.clientFrame.channelComboBox.addItem(item);
		ClientFrame.clientFrame.channelComboBox.setSelectedItem(item);
		return add;
	}
}