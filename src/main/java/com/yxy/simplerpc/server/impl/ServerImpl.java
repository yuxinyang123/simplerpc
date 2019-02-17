package com.yxy.simplerpc.server.impl;

import com.yxy.simplerpc.server.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements Server {

    private static ExecutorService excutor;

    private static boolean isRunning = false;

    private static int port;

    public ServerImpl(int port, int threadNums) {
        this.port = port;
        excutor = Executors.newFixedThreadPool(threadNums);
    }

    public void stop() {
        isRunning = false;
        excutor.shutdown();
    }

    public void start() {

    }

    public void register() {

    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }
}
