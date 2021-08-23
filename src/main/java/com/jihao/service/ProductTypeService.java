package com.jihao.service;

import com.jihao.pojo.ProductType;
import org.springframework.stereotype.Service;

import java.util.List;


public interface  ProductTypeService {
    List<ProductType> getAll();
}
