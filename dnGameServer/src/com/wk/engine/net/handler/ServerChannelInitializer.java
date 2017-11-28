package com.wk.engine.net.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.TimeUnit;

import com.wk.engine.codec.ByteBufToIoMessageDecoder;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.ServerConfigAbs;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	public ServerChannelInitializer() {
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (ServerConfigAbs.isHeartBeat())
			p.addLast(
					"idleStateHandler",
					new IdleStateHandlerEx(ServerConfig
							.getHeartBeatTimeInSecond(), 0, 0, TimeUnit.SECONDS));
		p.addLast("log", new LoggingHandler(LogLevel.TRACE));
		p.addLast("protobufDecoder", new ByteBufToIoMessageDecoder());
		p.addLast("frameEncoder", new MessageToByteBufEncoder());
		p.addLast("innerHandler", new InnerMsgInboundHandler());// 处理入境消息
		p.addLast("handler", new MsgInboundHandler());// 处理入境消息
		p.addLast("duplexHandler", SimpleChannelDuplexHandler.getInstance());

	}
}
