package org.light.serialize.example.netty.protocol;

/**
 * RpcResponse
 * 
 * @author Alex
 */
public class RpcResponse {
    private long requestId;
    private boolean pong;
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }

    public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isPong() {
        return pong;
    }

    public void setPong(boolean pong) {
        this.pong = pong;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId=" + requestId +
                ", pong=" + pong +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
