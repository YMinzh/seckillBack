package com.example.redis.mapper;

import com.example.redis.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO sec_order (proId, userId) VALUES (#{proId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);


}
