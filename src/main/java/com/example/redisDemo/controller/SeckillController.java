package com.example.redisDemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.redisDemo.entity.Product;
import com.example.redisDemo.entity.Response;
import com.example.redisDemo.entity.Users;
import com.example.redisDemo.mapper.OrderMapper;
import com.example.redisDemo.mapper.SeckillMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.convert.RedisTypeMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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



    public static Product product(){
        Product pro = new Product();
        pro.setName("商品1");
        pro.setMoney(BigDecimal.valueOf(100));
        pro.setOriginPrice(BigDecimal.valueOf(1000));
        pro.setNum(5);
        pro.setId(1);

        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate2 = null;
        try {
            myDate2 = dateFormat2.parse("2019-12-16 17:10:01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date myDate1 = null;
        try {
            myDate1 = dateFormat2.parse("2019-12-30 22:36:01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pro.setStartTime(myDate2);
        pro.setEndTime(myDate1);

        pro.setStatus(0);

        return pro;
    }



    @RequestMapping("/product/seckill")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public Response say(HttpServletRequest request){
        List<Product> list = seckillMapper.select();

        Response res = new Response();
        Jedis jedis = new Jedis();

        for (Product product:list) {
            if(jedis.get(product.getId()+"num")!=null){
                product.setNum(product.getNum() - Integer.parseInt(jedis.get(product.getId()+"num")));

            }

            if(product.getStatus()==0&&new Date().compareTo(product.getStartTime())>=0){

                if (!productIds.contains(product.getId())){
                    product.setStatus(1);
                    seckillMapper.update(product);
                    productIds.add(product.getId());
                }
            }

            if (new Date().compareTo(product.getEndTime())>=0){
                if (productIds.contains(product.getId())){
                    product.setStatus(2);
                    seckillMapper.update(product);
                    productIds.remove(product.getId());

                    Users users = new Users();


                    Set<String> setValues = jedis.smembers(String.valueOf(product.getId()));
                    for (String user:setValues) {
                        users.setProId(product.getId());
                        users.setUsers(Long.parseLong(user));
                        orderMapper.insert(users);
                    }
                }

            }
        }

        res.setCode(0);
        res.setData(list);
        return res;
    }

    @RequestMapping("/product/getone")
    public Response say1(HttpServletRequest request, @RequestBody JSONObject json) throws AWTException {
        Response res = new Response();
        Jedis jedis = new Jedis();
        long id = Long.parseLong(json.getString("id"));
        Product p= seckillMapper.getOne(id);

        if(new Date().compareTo(p.getStartTime())<0){
            res.setData("时间未到");
            res.setCode(0);
            return res;
        }
        if(new Date().compareTo(p.getEndTime())>=0){
            res.setData("时间已过");
            res.setCode(0);
            return res;
        }

        jedis.setnx("lock","true");

        boolean isExist = jedis.sismember(String.valueOf(id), json.getString("userId"));

        if (isExist){
            res.setCode(0);
            res.setData("您已购买过");
            jedis.del("lock");
            return res;
        }
        

        String str = jedis.mget(p.getId()+"num").get(0);


        if (str!=null&&Integer.parseInt(str)>=p.getNum()){
            res.setCode(0);
            p.setStatus(2);
            if (productIds.contains(id)){
                productIds.remove(id);

                seckillMapper.update(p);
                Users users = new Users();


                Set<String> setValues = jedis.smembers(String.valueOf(id));
                for (String user:setValues) {
                    users.setProId(id);
                    users.setUsers(Long.parseLong(user));
                    orderMapper.insert(users);
                }
            }
            res.setData("已经抢空了");
            jedis.del("lock");
            return res;
        }
        jedis.sadd(String.valueOf(id), json.getString("userId"));

        jedis.incr(id+"num");


        res.setCode(0);
        res.setData("抢购成功");
        jedis.del("lock");
        return res;
    }


}
