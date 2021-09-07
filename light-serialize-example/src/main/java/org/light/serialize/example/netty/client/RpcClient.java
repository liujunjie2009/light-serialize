package org.light.serialize.example.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.light.serialize.example.netty.protocol.RpcDecoder;
import org.light.serialize.example.netty.protocol.RpcEncoder;
import org.light.serialize.example.netty.protocol.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * RpcClient
 *
 * @author Alex
 */
public class RpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(RpcClient.class);

    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);

    private final EventLoopGroup eventloopGroup = new NioEventLoopGroup(1);
    private volatile Channel channel = null;

    private final String host;
    private final int port;
    private ScheduledFuture<?> checkFuture = null;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.checkFuture = scheduledExecutor.scheduleWithFixedDelay(() -> {
            synchronized (this) {
                try {
                    if (channel != null && !channel.isActive()) {
                        connect();
                    }
                    // Heartbeat
                    channel.writeAndFlush(new RpcRequest(0, null));
                } catch (Throwable e) {
                    LOG.warn("RpcClient heartbeat error", e);
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * connect rpc server
     */
    public synchronized void connect() {
        Bootstrap b = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(eventloopGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline cp = ch.pipeline();
                        cp.addLast(new RpcEncoder());
                        // cp.addLast(new LengthFieldBasedFrameDecoder(131072, 0, 4, 0, 0));
                        cp.addLast(new RpcDecoder());
                        cp.addLast(new RpcClientHandler(RpcClient.this));
                    }
                });
        try {
            ChannelFuture f = b.connect(host, port);
            this.channel = f.sync().channel();
            this.channel.closeFuture().addListener((ChannelFuture future) -> {
            });
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * close and release resources
     */
    public synchronized void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        eventloopGroup.shutdownGracefully();
        checkFuture.cancel(false);
        channel = null;
        checkFuture = null;
    }

    /**
     * sendRequest
     */
    public void sendRequest(RpcRequest request) {
        if (!isAble()) {
            throw new IllegalStateException();
        }

        channel.writeAndFlush(request);
    }

    public boolean isAble() {
        return channel != null && channel.isActive();
    }

}
