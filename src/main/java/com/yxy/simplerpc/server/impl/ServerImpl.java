package com.yxy.simplerpc.server.impl;

import com.yxy.simplerpc.server.Server;
import com.yxy.simplerpc.server.ServerTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements Server {

    private static ExecutorService executor;

    private static Map<String, Class> serviceRegistry = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private static int port;


    public ServerImpl(int port, int threadNums) {
        ServerImpl.port = port;
        executor = Executors.newFixedThreadPool(threadNums);
    }

    public void stop() {
        executor.shutdown();
        isRunning = false;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(port));
            isRunning = true;
            while (true) {
                executor.execute(new ServerTask(server.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(Class classInterface, Class classImpl) {
        serviceRegistry.put(classInterface.getName(), classImpl);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    public static Map<String, Class> getServiceRegistry() {
        return serviceRegistry;
    }

}
