package com.yxy.simplerpc.servercenter;

public interface ServerCenter {
    public void register(Class classInterface, Class classImpl);

    public void logout();
}
