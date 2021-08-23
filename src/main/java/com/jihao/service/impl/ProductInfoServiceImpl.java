package com.jihao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jihao.mapper.ProductInfoMapper;
import com.jihao.pojo.ProductInfo;
import com.jihao.pojo.ProductInfoExample;
import com.jihao.pojo.vo.ProductInfoVo;
import com.jihao.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用pageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);
        //进行数据的封装，进行有条件的查询
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，进行降序排序
        example.setOrderByClause("p_id desc");
        //用集合取数据
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //进行数据转换,将list分装到pageinfo中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
       return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getByID(int pId) {
        return productInfoMapper.selectByPrimaryKey(pId);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] pids) {
        return productInfoMapper.deleteBatch(pids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //去取出集合前，必须要先设置pageHelper.startPage()属性
        PageHelper.startPage(vo.getPage(), pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list);
    }
}
