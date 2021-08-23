package com.jihao.service;

import com.github.pagehelper.PageInfo;
import com.jihao.pojo.ProductInfo;
import com.jihao.pojo.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {

    //显示全部商品
    List<ProductInfo> getAll();

    //分页功能
    PageInfo splitPage(int pageNum,int pageSize);

    int save(ProductInfo info);

    //按主键ID查询的方法
    ProductInfo getByID(int pId);

    //更新商品
    int update(ProductInfo info);

    //删除商品
    int delete(int pid);

    //批量删除商品
    int deleteBatch(String []pids);

    //多条件的商品查询
    List<ProductInfo>  selectCondition(ProductInfoVo vo);

    //多条件查询分页
    public PageInfo splitPageVo(ProductInfoVo vo,int pageSize);
}
