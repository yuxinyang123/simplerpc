package com.yxy.simplerpc.client;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface Client {
    public<T> T getResult(Class<T> serviceClass) throws IOException;

}
