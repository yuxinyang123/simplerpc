package com.yxy.simplerpc.server;

public interface Server {
    public void stop();

    public void start();

    public void register(Class classInterface,Class classImpl);

    public boolean isRunning();

    public int getPort();

}
