package com.november.book;/*
 *
 **/

import com.november.book.model.Book;
import com.november.book.util.BookCodeUtil;
import org.junit.Test;
import sun.net.www.content.image.x_xpixmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test {
    @Test
    public void testEx(){
        for(int i=0;i<10000;i++) {
            BookCodeUtil.getBookCode();
        }

        List<Book> li=new ArrayList<>();
        li.add(new Book());




    }
}
