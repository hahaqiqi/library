package com.november.book;/*
 *
 **/

import org.junit.Test;
import sun.net.www.content.image.x_xpixmap;

import java.util.ArrayList;
import java.util.List;

public class test {
    @Test
    public void testEx(){
        List<String> ss=new ArrayList<>();
        ss.add("1");
        String asads="";
        for(int i=0;i<ss.size();i++){
            if(i==0){
                asads=ss.get(0);
                continue;
            }
            asads=asads+","+ss.get(i);
        }
        System.out.println(asads);
    }
}
