package org.light.serialize.example.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.light.serialize.example.netty.RpcProvider;
import org.light.serialize.example.netty.protocol.RpcDecoder;
import org.light.serialize.example.netty.protocol.RpcEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rpc Server
 * 
 * @author Alex
 */
public class RpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServer.class);

    private int port;
    private RpcProvider provider;
    private EventLoopGroup accepterGroup = null;
    private EventLoopGroup workGroup = null;

    public RpcServer(RpcProvider provider) {
    	this.provider = provider;
        this.port = provider.getPort();
		this.accepterGroup = new NioEventLoopGroup(1, (Runnable r) -> new Thread(r, "RpcServer-Accepter"));
		this.workGroup = new NioEventLoopGroup(4, (Runnable r) -> new Thread(r, "RpcServer-Worker"));
    }

    /**
     * start
     */
    public void start() {
    	try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(accepterGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    // .addLast(new LengthFieldBasedFrameDecoder(131072, 0, 4, 0, 0))
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcServerHandler(provider));
                        }
                    })
    				.option(ChannelOption.SO_RCVBUF, 1024 * 8)
    				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
    				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.bind(port).sync();
            LOG.info("Rpc Server started on port {}", port);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
    }

	/**
	 * stop
	 */
	public void stop() {
		accepterGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}

}
