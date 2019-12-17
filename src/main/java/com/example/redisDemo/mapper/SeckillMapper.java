package com.example.redisDemo.mapper;

import com.example.redisDemo.entity.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SeckillMapper {

    @Insert("INSERT INTO product (name, startTime,num,money,status) VALUES (#{name}, #{startTime}, #{num}, #{money},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product product);

    @Select("select *from product")
    List<Product> select();

    @Select("select *from product where id = #{id}")
    Product getOne(long id);

    @Update("update product set status = #{status} where id = #{id}")
    void update(Product product);
}
