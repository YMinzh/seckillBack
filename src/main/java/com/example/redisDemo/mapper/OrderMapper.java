package com.example.redisDemo.mapper;

import com.example.redisDemo.entity.Product;
import com.example.redisDemo.entity.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO users (proId, users) VALUES (#{proId}, #{users})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Users users);


}
