package com.yxy.simplerpc.server.impl;

import com.yxy.simplerpc.server.Server;
import com.yxy.simplerpc.server.ServerTask;
import com.yxy.simplerpc.util.SocketUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements Server {

    private static ExecutorService executor;

    private static Map<String, Class> serviceRegistry = new HashMap<>();

    private static boolean isRunning = false;

    private InetSocketAddress hostAddress;

    private InetSocketAddress centerAddress;

    private Class classImpl;


    public ServerImpl(InetSocketAddress centerAddress,InetSocketAddress address, int threadNums) {
        this.hostAddress = address;
        this.centerAddress = centerAddress;
        executor = Executors.newFixedThreadPool(threadNums);
    }

    public void stop() {
        executor.shutdown();
        isRunning = false;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            server.bind(hostAddress);
            isRunning = true;
            while (true) {
                executor.execute(new ServerTask(server.accept(),classImpl));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(Class classInterface, Class classImpl) {
        this.classImpl = classImpl;
//        serviceRegistry.put(classInterface.getName(), classImpl);
        Socket socket = SocketUtil.startSocket(centerAddress);
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));){
            bw.write(classInterface.getName());
            bw.newLine();
            bw.write(hostAddress.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public InetSocketAddress getHostAddress() {
        return hostAddress;
    }

    @Override
    public InetSocketAddress getCenterAddress() {
        return centerAddress;
    }

    public static Map<String, Class> getServiceRegistry() {
        return serviceRegistry;
    }

}
