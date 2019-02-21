package com.yxy.simplerpc.client;

import java.net.InetSocketAddress;

public interface Client {
    public<T> T getResult(Class<T> serviceClass);

}
