package com.jihao.service.impl;

import com.jihao.mapper.ProductTypeMapper;
import com.jihao.pojo.ProductType;
import com.jihao.pojo.ProductTypeExample;
import com.jihao.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
      return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
