package com.example.redis.writeMysql;

import com.example.redis.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
public class Write {
    @Autowired
    SeckillMapper seckillMapper;

    public void write(){

    }

}
