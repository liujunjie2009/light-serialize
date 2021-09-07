package org.light.serialize.example.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.light.serialize.example.netty.protocol.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * RpcClientHandler
 * 
 * @author Alex
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
	
    private static final Logger LOG = LoggerFactory.getLogger(RpcClientHandler.class);

    private RpcClient client;


    public RpcClientHandler(RpcClient client) {
		super();
		this.client = client;
	}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info("rpc client channel active", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOG.info("rpc client channel inactive", ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        LOG.info("read response:{}", response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			LOG.warn("rpc client caught exception", cause.getMessage());
		} else {
			LOG.error("rpc client caught exception", cause);
		}
        ctx.close();
    }


}
