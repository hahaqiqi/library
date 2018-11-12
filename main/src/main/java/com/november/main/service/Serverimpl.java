package com.november.main.service;

import org.springframework.stereotype.Service;

@Service(value = "Server")
public class Serverimpl implements Server {
    public String geta(){
        return "a";
    }
}
