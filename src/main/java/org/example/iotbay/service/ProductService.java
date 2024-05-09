package org.example.iotbay.service;

import org.example.iotbay.domain.Product;
import org.example.iotbay.dto.ProductDTO;
import org.example.iotbay.dto.ProductDTO.Response;

import java.util.List;
import java.util.Set;

import static org.example.iotbay.dto.ProductDTO.*;

public interface ProductService {
    Response createProduct(Request request);
    List<Product> getProductsList();

    List<Product> getProductsListByName(String name);
    List<Product> getProductsListByType(String type);


    Response updateProduct(Long id,Request request);

    String deleteProduct(Long id, Long userId);
}
