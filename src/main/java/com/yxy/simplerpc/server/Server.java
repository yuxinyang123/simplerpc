package com.yxy.simplerpc.server;

public interface Server {
    public void stop();

    public void start();

    public void register();

    public boolean isRunning();

    public int getPort();

}
