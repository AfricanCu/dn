package com.wk.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.net.I.InnerCsConnectAbs;
import com.wk.engine.net.handler.ByteBufToInnerIoMessageDecoder;
import com.wk.engine.net.handler.MessageToByteBufEncoder;

public class SocketUtil {

	/**
	 * 创建一个内部用客户端，，同步方式
	 * 
	 * @param host
	 * @param port
	 * @param inbound
	 * @return
	 * @throws Exception
	 */
	public static Channel createInnerSocketClientSync(String host, int port,
			final InnerCsConnectAbs csConnectI) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap(); // #1
			b.group(group); // #2
			b.channel(NioSocketChannel.class)
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
											new ByteBufToInnerIoMessageDecoder());
									p.addLast("frameEncoder",
											new MessageToByteBufEncoder());
									p.addLast("handler", csConnectI);// 处理入境消息
								}
							});
			ChannelFuture f = b.connect().sync();
			return f.channel();
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			group.shutdownGracefully();
			return null;
		}
	}

	/**
	 * 获取一行数据
	 * 
	 * @param host
	 * @param port
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public static String readLine(String host, int port, byte[] b) {
		try {
			// 客户端
			// 1、创建客户端Socket，指定服务器地址和端口
			Socket socket = new Socket(host, port);
			// 2、获取输出流，向服务器端发送信息
			OutputStream os = socket.getOutputStream();// 字节输出流
			os.write(b);
			os.flush();
			socket.shutdownOutput();
			// 3、获取输入流，并读取服务器端的响应信息
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String info = br.readLine();
			// 4、关闭资源
			os.close();
			is.close();
			br.close();
			socket.close();
			return info;
		} catch (IOException e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取字节数组
	 * 
	 * 协议 长度（short）+字节数组
	 * 
	 * @param host
	 * @param port
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(String host, int port, byte[] b) {
		try {
			// 客户端
			// 1、创建客户端Socket，指定服务器地址和端口
			Socket socket = new Socket(host, port);
			// 2、获取输出流，向服务器端发送信息
			OutputStream os = socket.getOutputStream();// 字节输出流
			os.write(b);
			os.flush();
			socket.shutdownOutput();
			// 3、获取输入流，并读取服务器端的响应信息
			InputStream is = socket.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(is);
			short length = dataInputStream.readShort();
			byte[] rb = new byte[length];
			dataInputStream.readFully(rb);
			// 4、关闭资源
			os.close();
			is.close();
			socket.close();
			return rb;
		} catch (IOException e) {
			LoggerService.getPlatformLog().error(
					"host:" + host + ",port:" + port, e);
			return null;
		}
	}

}
