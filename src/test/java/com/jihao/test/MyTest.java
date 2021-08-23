package com.jihao.test;

import com.jihao.mapper.ProductInfoMapper;
import com.jihao.pojo.ProductInfo;
import com.jihao.pojo.ProductInfoExample;
import com.jihao.pojo.vo.ProductInfoVo;
import com.jihao.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class MyTest {

    @Autowired
    ProductInfoMapper productInfoMapper;


    @Test
    public void testMD5(){
    String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }

   @Test
    public void selectCondition(){
        ProductInfoVo vo = new ProductInfoVo();
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        list.forEach(ProductInfo -> System.out.println(ProductInfo));
    }
}
