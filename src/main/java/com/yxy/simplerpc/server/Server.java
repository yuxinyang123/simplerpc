package com.yxy.simplerpc.server;

import java.net.InetSocketAddress;

public interface Server {
    public void stop();

    public void start();

    public void register(Class classInterface,Class classImpl);

    public boolean isRunning();

    public InetSocketAddress getHostAddress();

    public InetSocketAddress getCenterAddress();

}
