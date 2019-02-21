package com.yxy.simplerpc.test;

import com.yxy.simplerpc.client.Client;
import com.yxy.simplerpc.client.impl.ClientImpl;
import com.yxy.simplerpc.server.Server;
import com.yxy.simplerpc.server.impl.ServerImpl;
import com.yxy.simplerpc.service.HelloService;
import com.yxy.simplerpc.service.impl.HelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Test {
    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new ServerImpl(8088, 2);
                server.register(HelloService.class, HelloServiceImpl.class);
                server.start();
            }
        }).start();


        try {
            Client client = new ClientImpl(new InetSocketAddress(8088));
            HelloService helloService = client.getResult(HelloService.class);
            System.out.println(helloService.sayHello());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            server.stop();
        }
    }
}
