package org.example.iotbay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Product;
import org.example.iotbay.dto.ProductDTO;
import org.example.iotbay.service.ProductService;
import org.example.iotbay.service.ProductServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.iotbay.dto.ProductDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Tag(name="Product", description = "Product API")
public class ProductController {
    private final ProductServiceImpl productService;

    @PostMapping("/create")
    @Operation(summary="Create a New Product", description = "Creates a new product with the provided data")
    @Parameters({
            @Parameter(name = "userId", description = "The ID of the staff requesting the deletion", example = "1"),
            @Parameter(name = "name", description = "The name of the product", example = "Galaxy Book Pro"),
            @Parameter(name = "description", description = "The description of the product", example = "samsung laptop"),
            @Parameter(name = "type", description = "The type of the product", example = "laptop"),
            @Parameter(name = "quantity", description = "The quantity of the product", example = "5000"),
            @Parameter(name = "price", description = "The price of the product", example = "1500"),
    })
    public ResponseEntity<Response> createProduct(@Validated @RequestBody Request request){
        return ResponseEntity.ok(productService.createProduct(request));
    }
    @GetMapping("")
    @Operation(summary = "Get Products List", description = "Retrieves a list of all products available in the database")
    public ResponseEntity<List<Product>> getProductsList(){
        return ResponseEntity.ok(productService.getProductsList());
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get Products by Name", description = "Retrieves a list of all products whose name contains the specified keyword")
    @Parameter(name = "keyword", description = "The keyword to search within product names", example = "Galaxy")    public ResponseEntity<List<Product>> getProductsListByName(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(productService.getProductsListByName(keyword));
    }

    @GetMapping("/by-type")
    @Operation(summary = "Get Products by Type", description = "Retrieves a list of all products whose type contains the specified keyword")
    @Parameter(name = "keyword", description = "The keyword to search within product types", example = "laptop")    public ResponseEntity<List<Product>> getProductsListByType(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(productService.getProductsListByType(keyword));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product Details", description = "Updates the specified product with the provided data")
    @Parameters({
            @Parameter(name = "userId", description = "The ID of the staff requesting the deletion", example = "1"),
            @Parameter(name = "id", description = "The unique identifier of the product to update", example = "1")
    })
    public ResponseEntity<Response> updateProduct(@PathVariable("id") Long id, @Validated @RequestBody Request request){
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product", description = "Deletes the specified product from the database")
    @Parameters({
            @Parameter(name = "id", description = "The unique identifier of the product to be deleted", example = "1"),
            @Parameter(name = "requestedBy", description = "The ID of the staff requesting the deletion", example = "2")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id, @RequestParam("requestedBy") Long userId){
        return ResponseEntity.ok(productService.deleteProduct(id, userId));
    }
}
