package org.example.iotbay.service;

import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Product;
import org.example.iotbay.domain.User;
import org.example.iotbay.dto.ProductDTO;
import org.example.iotbay.exception.ResourceNotFoundException;
import org.example.iotbay.exception.UnauthorizedAccessException;
import org.example.iotbay.repository.ProductRepository;
import org.example.iotbay.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.example.iotbay.dto.ProductDTO.*;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public boolean isStaff(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedAccessException("User is not authorized as staff member"));

        return Objects.equals(user.getRole(), "STAFF");
    }
    @Override
    public Response createProduct(Request request) {
        if (!isStaff(request.getUserId())){
            throw new UnauthorizedAccessException("User is not authorized as staff member");
        }
        Product product = modelMapper.map(request, Product.class);
        Product createdProduct = productRepository.save(product);
        return modelMapper.map(createdProduct, Response.class);
    }

    @Override
    public List<Product> getProductsList() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsListByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);

    }

    @Override
    public List<Product> getProductsListByType(String type) {
        return productRepository.findByTypeContainingIgnoreCase(type);

    }

    @Override
    public Response updateProduct(Long id, Request request) {
        if (!isStaff(request.getUserId())){
            throw new UnauthorizedAccessException("User is not authorized as staff member");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setType(request.getType());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        productRepository.save(product);
        return modelMapper.map(product, Response.class);
    }

    @Override
    public String deleteProduct(Long id, Long userId) {
        if (!isStaff(userId)){
            throw new UnauthorizedAccessException("User is not authorized as staff member");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        productRepository.delete(product);
        return "Product successfully deleted";
    }
}
