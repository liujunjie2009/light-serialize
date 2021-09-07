package org.light.serialize.example.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.light.serialize.example.netty.RpcProvider;
import org.light.serialize.example.netty.protocol.RpcRequest;
import org.light.serialize.example.netty.protocol.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RpcHandler
 *
 * @author Alex
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServerHandler.class);

    private RpcProvider provider;
    private ThreadPoolExecutor executor;

    public RpcServerHandler(RpcProvider provider) {
        this.provider = provider;
        this.executor = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(65536),
                r -> new Thread(r, "Rpc-" + getClass().getSimpleName()));
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                try {
                    if (request.getMethodId() == 0) {// Heartbeat
                        response.setPong(true);
                    } else {
                        response.setResult(provider.processRequest(request));
                    }
                } catch (Throwable t) {
                    Throwable cause = t.getCause();
                    if (cause == null) {
                        response.setError(t.toString());
                    } else {
                        response.setError(cause.toString());
                    }

                    LOG.error("RPC Server handle request error", t);
                }

                ctx.writeAndFlush(response);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            LOG.warn("rpc server caught io exception[{}]", cause.getMessage());
        } else {
            LOG.error("rpc server caught exception", cause);
        }
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info("rpc server channel active", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOG.info("rpc server channel inactive", ctx.channel());
    }
}
