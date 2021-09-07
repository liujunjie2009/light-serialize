package org.light.serialize.example.netty.protocol;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rpc Request
 * 
 * @author Alex
 */
public class RpcRequest {
	
    private static final AtomicLong REQUEST_ID_SEED = new AtomicLong(0);
    private final long requestId;
    private long methodId;
   private Object[] params;

	public RpcRequest() {
		this.requestId = REQUEST_ID_SEED.getAndIncrement();
	}

	public RpcRequest(long methodId, Object[] params) {
		this.requestId = REQUEST_ID_SEED.getAndIncrement();
		this.methodId = methodId;
		this.params = params;
	}

	public long getRequestId() {
		return requestId;
	}

	public long getMethodId() {
		return methodId;
	}

	public void setMethodId(long methodId) {
		this.methodId = methodId;
	}

	@Override
	public String toString() {
		return "RpcRequest{" +
				"requestId=" + requestId +
				", methodId=" + methodId +
				", params=" + Arrays.toString(params) +
				'}';
	}
}