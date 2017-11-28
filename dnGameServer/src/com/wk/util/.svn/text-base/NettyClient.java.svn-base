package com.wk.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import com.wk.engine.net.handler.MessageToByteBufEncoder;

public class NettyClient extends SocketUtil {

	/**
	 * 
	 * 创建一个外部用客户端，，同步方式
	 * 
	 * @param host
	 * @param port
	 * @param inbound
	 * @return
	 * @throws Throwable
	 */
	public static Channel createOuterSocketClientSync(String host, int port,
			final SimpleChannelInboundHandler<?> inbound) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup(2);
		try {
			Bootstrap b = new Bootstrap(); // #1
			b.group(group)
					// #2
					.channel(NioSocketChannel.class)
					// #3
					.option(ChannelOption.RCVBUF_ALLOCATOR,
							new AdaptiveRecvByteBufAllocator(64, 1024, 65536))
					.remoteAddress(new InetSocketAddress(host, port)) // #4
					.handler(new ChannelInitializer<SocketChannel>() { // #5
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ChannelPipeline p = ch.pipeline();
									p.addLast(
											"protobufDecoder",
											new ClientByteBufToIoMessageDecoder());
									p.addLast("frameEncoder",
											new MessageToByteBufEncoder());
									p.addLast("handler", inbound);// 处理入境消息
								}
							});
			ChannelFuture f = b.connect().sync();
			if (f.isDone() && f.isSuccess())
				return f.channel();
			else {
				throw new Exception(f.cause());
			}
		} catch (Exception e) {
			group.shutdownGracefully().sync();
			e.printStackTrace();
			throw e;
		}
	}
}
