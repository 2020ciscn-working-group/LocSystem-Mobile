package com.example.myapplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.jar.Attributes;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class owner {
     protected tcpsocket tcp;
     protected owner_date date;
     public owner(){
         //uuid.clockSequence();
         tcp=new tcpsocket("tcpchanel");
         tcp.start();
         date=new owner_date(tcp);
         date.start();
     }

}

