package com.example.redis.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.redis.dao.redis.SerializeUtil;
import com.example.redis.dao.redis.Worker;
import com.example.redis.entity.Product;
import com.example.redis.entity.Response;
import com.example.redis.entity.Order;
import com.example.redis.mapper.OrderMapper;
import com.example.redis.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SeckillController {

    private List<Long> productIds = new ArrayList<Long>();

    @Autowired
    SeckillMapper seckillMapper;
    @Autowired
    OrderMapper orderMapper;


    @RequestMapping("/product/init")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public Response init(HttpServletRequest request, @RequestBody Product product) {
        Response res = new Response();
        Jedis jedis = new Jedis();


        product = seckillMapper.getOne(product.getId());
        if (product == null) {
            res.setCode(0);
            res.setData("无效id");
            return res;
        }

        if (jedis.get("product:id:" + product.getId())!=null){
            res.setCode(0);
            res.setData("已经初始化过了");
            return res;
        }

        jedis.set("product:id:" + product.getId(), SerializeUtil.toHexString(SerializeUtil.serialize(product)));
        jedis.set("product:num:"+product.getId(),String.valueOf(product.getNum()));
        for (int i = 1; i <= product.getNum(); i++) {
            jedis.lpush("list:product:" + product.getId(), "product:" + i);
        }

        Worker worker = new Worker(product.getId(),orderMapper,seckillMapper);
        worker.start();

        res.setCode(0);
        res.setData("初始化成功");

        return res;
    }


    @RequestMapping("/product/show")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public Response say(HttpServletRequest request) {
        Response res = new Response();
        Jedis jedis = new Jedis();
        String resId = request.getParameter("id");
        long id = 0L;
        try {
            id = Long.parseLong(resId);
        }catch (NumberFormatException e){
            res.setCode(0);
            res.setData("错误的ID");
            return res;
        }

        Product product = (Product) SerializeUtil.unserialize(SerializeUtil.toByteArray(jedis.get("product:id:" + id)));

        if (product==null){
            res.setCode(0);
            res.setData("错误ID");
            return res;
        }

        if (product.getStatus() == 0 && new Date().compareTo(product.getStartTime()) >= 0){
            product.setStatus(1);
        }
        if (new Date().compareTo(product.getEndTime()) >= 0){
            product.setStatus(2);
        }

        System.out.println(product);
        product.setNum(jedis.llen("list:product:" + product.getId()));

//        jedis.set("list:product:" + product.getId(),SerializeUtil.toHexString(SerializeUtil.serialize(product)));


        res.setCode(0);
        res.setData(product);
        return res;
    }

    @RequestMapping("/product/create")
    public Response say1(HttpServletRequest request, @RequestBody JSONObject json) throws AWTException {
        Response res = new Response();
        Jedis jedis = new Jedis();
        long id = Long.parseLong(json.getString("id"));
        Product p = (Product) SerializeUtil.unserialize(SerializeUtil.toByteArray(jedis.get("product:id:" + id)));

        if (new Date().compareTo(p.getStartTime()) < 0) {
            res.setData("时间未到");
            res.setCode(0);
            return res;
        }
        if (new Date().compareTo(p.getEndTime()) >= 0) {
            res.setData("时间已过");
            res.setCode(0);
            return res;
        }

        if (jedis.smembers("set:users:"+id).contains(json.getString("userId"))){
            res.setCode(0);
            res.setData("不能重复购买");
            return res;
        }

        String proId = jedis.rpop("list:product:"+id);

        if (proId==null){
            res.setCode(0);
            res.setData("抢完了");
            return res;
        }
        jedis.lpush("list:order:"+id,json.getString("userId"));
        jedis.sadd("set:users:"+id,json.getString("userId"));

        res.setCode(0);
        res.setData("抢购成功");
        jedis.del("lock");
        return res;
    }


}
