package com.example.redisDemo.writeMysql;

import com.example.redisDemo.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class Write {
    @Autowired
    SeckillMapper seckillMapper;

    public void write(){

    }

}
