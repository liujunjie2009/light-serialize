package org.light.serialize.example.netty;

import org.light.serialize.example.netty.client.RpcClient;
import org.light.serialize.example.netty.protocol.RpcRequest;

/**
 * @author Alex
 */
public class RpcApplication {

    private static final String HOST = "localhost";
    private static final int PORT = 7788;

    public static void main(String[] args) throws InterruptedException {
        new RpcProvider(PORT).export();

        RpcClient rpcClient = new RpcClient(HOST, PORT);
        rpcClient.connect();
        rpcClient.sendRequest(new RpcRequest(System.currentTimeMillis(), new Object[]{"hello rpc1"}));
        rpcClient.sendRequest(new RpcRequest(System.currentTimeMillis(), new Object[]{"hello rpc2"}));

        Thread.sleep(10000);
        System.exit(0);
    }
}
