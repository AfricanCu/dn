package test.client;

import io.netty.channel.Channel;

import java.util.HashMap;

public class ChannelItem {
	private Channel channel;

	public ChannelItem(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		HashMap<String, Object> hashMap = channel.attr(TestClient.MAP_Attr)
				.get();
		return hashMap.get(TestClient.uidIndex) + " "
				+ hashMap.get(TestClient.puidIndex) + " "
				+ hashMap.get(TestClient.nicknameIndex) + " "
				+ hashMap.get(TestClient.roomIdIndex) + " "
				+ channel.toString();

	}

	public Channel getChannel() {
		return channel;
	}

}