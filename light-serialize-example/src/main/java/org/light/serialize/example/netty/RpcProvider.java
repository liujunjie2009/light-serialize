package org.light.serialize.example.netty;

import org.light.serialize.example.netty.protocol.RpcRequest;
import org.light.serialize.example.netty.server.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RpcProvider
 * 
 * @author Alex
 */
public class RpcProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(RpcProvider.class);

	private final int port;
	private final RpcServer server;

	public RpcProvider(int port) {
		this.port = port;
		this.server = new RpcServer(this);
	}
	

    /**
     * process rpc request
     */
    public Object processRequest(RpcRequest request) throws Throwable {
		logger.info("processRequest: {}", request);
        return request;
    }

	public int getPort() {
		return port;
	}

	public void export() {
		server.start();
	}


}
