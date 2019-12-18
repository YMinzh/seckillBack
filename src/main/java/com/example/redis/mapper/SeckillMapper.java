package com.example.redis.mapper;

import com.example.redis.entity.Product;
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

    @Update("update product set num = num - 1 where id = #{id}")
    void updateNum(long id);


}
