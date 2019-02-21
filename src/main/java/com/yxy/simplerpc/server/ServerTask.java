package com.yxy.simplerpc.server;

import com.yxy.simplerpc.server.impl.ServerImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Objects;

public class ServerTask implements Runnable {

    private Socket socket;

    public ServerTask(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {

            ois = new ObjectInputStream(socket.getInputStream());
            String serviceName = ois.readUTF();
            String methodName = ois.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
            Object[] parameters = (Object[]) ois.readObject();

            //FIXME 临时实现
            Class serviceClass = ServerImpl.getServiceRegistry().get(serviceName);
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serviceClass.newInstance(), parameters);

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(ois)) {
                try {
                    ois.close();
                    ois = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(oos)) {
                try {
                    oos.close();
                    oos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (Objects.nonNull(socket)) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
