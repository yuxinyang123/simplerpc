package com.yxy.simplerpc.service.impl;

import com.yxy.simplerpc.service.HelloService;

public class HelloServiceImpl implements HelloService {
    public String sayHello() {
        return "this is a rpc com.yxy.simplerpc.service!";
    }
}
