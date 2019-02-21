package com.yxy.simplerpc.client.impl;

import com.yxy.simplerpc.client.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class ClientImpl implements Client {

    private Socket socket = new Socket();

    public ClientImpl(InetSocketAddress address) throws IOException {
        socket.connect(address);
    }

    @Override
    public <T> T getResult(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),new Class<?>[]{serviceInterface} , new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ObjectInputStream ois = null;
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());

                    oos.writeUTF(serviceInterface.getName());
                    oos.writeUTF(method.getName());
                    oos.writeObject(method.getParameterTypes());
                    oos.writeObject(args);

                    ois = new ObjectInputStream(socket.getInputStream());
                    return ois.readObject();

                } finally {
                    if (Objects.nonNull(ois)) {
                        ois.close();
                        ois = null;
                    }
                    if (Objects.nonNull(oos)) {
                        oos.close();
                        oos = null;
                    }
                    if (Objects.nonNull(socket)) {
                        socket.close();
                        socket = null;
                    }
                }
            }
        });
    }

}
