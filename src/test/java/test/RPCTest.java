package test;


import com.yxy.simplerpc.client.Client;
import com.yxy.simplerpc.client.impl.ClientImpl;
import com.yxy.simplerpc.server.Server;
import com.yxy.simplerpc.server.impl.ServerImpl;
import com.yxy.simplerpc.servercenter.ServerCenter;
import com.yxy.simplerpc.servercenter.impl.ServerCenterImpl;
import com.yxy.simplerpc.service.HelloService;
import com.yxy.simplerpc.service.impl.HelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RPCTest {
    public static void main(String[] args) throws InterruptedException {


        new Thread(() -> {
            Server server = new ServerImpl(new InetSocketAddress(6666), new InetSocketAddress(8088), 2);
            server.register(HelloService.class, HelloServiceImpl.class);
            server.start();
        }).start();


        try {
            Client client = new ClientImpl( new InetSocketAddress(7777));
            HelloService helloService = client.getResult(HelloService.class);
            System.out.println(helloService.sayHello());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServerCenter(){

    }
}
