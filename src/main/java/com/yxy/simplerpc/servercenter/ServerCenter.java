package com.yxy.simplerpc.servercenter;

import java.net.InetSocketAddress;

public interface ServerCenter {
    public void start();

    public void stop();

    public void registry(InetSocketAddress registryAddress,Class classInterface, Class classImpl);

    public void remove(InetSocketAddress registryAddress,Class classInterface, Class classImpl);

    public void listenClient(InetSocketAddress clientAddress);

    public void listenRegistry(InetSocketAddress registryAddress);

    public void listenLogout(InetSocketAddress logoutAddress);

    public void isRunning();
}
