package test;


import com.yxy.simplerpc.client.Client;
import com.yxy.simplerpc.client.impl.ClientImpl;
import com.yxy.simplerpc.server.Server;
import com.yxy.simplerpc.server.impl.ServerImpl;
import com.yxy.simplerpc.service.HelloService;
import com.yxy.simplerpc.service.impl.HelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RPCTest {
    public static void main(String[] args) {
        Server server = new ServerImpl(6666, 2);
        server.register(HelloService.class, HelloServiceImpl.class);
        server.start();

        try {
            Client client = new ClientImpl(new InetSocketAddress("localhost", 6666));
            HelloService helloService = client.getResult(HelloService.class);
//            System.out.println(helloService.sayHello());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
