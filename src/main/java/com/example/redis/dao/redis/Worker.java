package com.example.redis.dao.redis;

import com.example.redis.entity.Order;
import com.example.redis.entity.Product;
import com.example.redis.entity.Response;
import com.example.redis.mapper.OrderMapper;
import com.example.redis.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import redis.clients.jedis.Jedis;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

@EnableAsync
public class Worker extends Thread{

//    @Autowired
//    OrderMapper orderMapper;
//    @Autowired
//    SeckillMapper seckillMapper;


    private boolean flag = true;
    private long id;
    private OrderMapper orderMapper;
    private SeckillMapper seckillMapper;



    @Async()
    public void watch() {
        Jedis jedis = new Jedis();
        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }



        Product product = (Product) SerializeUtil.unserialize(SerializeUtil.toByteArray(jedis.get("product:id:" + id)));
        while (flag) {
            String str = jedis.rpop("list:order:" + id);
            if (str == null) {
                r.delay(10000);
                if (new Date().compareTo(product.getEndTime())>0){
                    flag = false;
                }
                continue;
            }
            Order order = new Order();
            order.setProId(id);
            order.setUserId(Long.parseLong(str));
            orderMapper.insert(order);
            seckillMapper.updateNum(id);
        }
    }


    public Worker(long id,OrderMapper orderMapper,SeckillMapper seckillMapper) {
        this.id = id;
        this.orderMapper = orderMapper;
        this.seckillMapper = seckillMapper;
    }

    @Override
    public void run() {
        watch();
    }
}
