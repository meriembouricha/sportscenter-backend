package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.model.BrandResponse;
import com.ecomerce.sportscenter.model.ProductResponse;
import com.ecomerce.sportscenter.model.TypeResponse;
import com.ecomerce.sportscenter.service.BrandService;
import com.ecomerce.sportscenter.service.ProductService;
import com.ecomerce.sportscenter.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    private final TypeService typeService;
    private final BrandService brandService;

    public ProductController(ProductService productService, TypeService typeService, BrandService brandService) {
        this.productService = productService;
        this.typeService = typeService;
        this.brandService = brandService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Integer productId){
        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping()


    public ResponseEntity<Page<ProductResponse>> getProducts(
            @PageableDefault(size = 10)Pageable pageable,
            @RequestParam(name="keyword", required = false) String keyword,
            @RequestParam(name="brandId", required = false) Integer brandId,
            @RequestParam(name="typeId", required = false) Integer typeId,
            @RequestParam(name="sort", defaultValue = "name") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ){
        Page<ProductResponse> productResponsePage;
        if(brandId!=null && typeId!=null && keyword!=null && !keyword.isEmpty()) {
            //search by brand, type and keyword
            List<ProductResponse> productResponses = productService.searchProductsByBrandTypeAndName(brandId, typeId, keyword);
            productResponsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        }
        else if(brandId!=null && typeId!=null) {
            //search by brand and type
            List<ProductResponse> productResponses = productService.searchProductsByBrandAndType(brandId, typeId);
            productResponsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        }
        else if(brandId!=null) {
            //search by brand
            List<ProductResponse> productResponses = productService.searchProductsByBrand(brandId);
            productResponsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        }
        else if(typeId!=null) {
            //search by type
            List<ProductResponse> productResponses = productService.searchProductsByType(typeId);
            productResponsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        }
        else if(keyword!=null && !keyword.isEmpty()){
            List<ProductResponse> productResponses = productService.searchProductsByName(keyword);
            productResponsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        }else{
            //If no search criteria, then retrieve based on sorting options
            Sort.Direction direction = "asc".equalsIgnoreCase(order)?Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sorting = Sort.by(direction, sort);

            productResponsePage = productService.getProducts(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting));
        }
        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }
    @GetMapping("/brands")
    public ResponseEntity<List<BrandResponse>> getBrands(){
        List<BrandResponse> brandResponses = brandService.getAllBrands();
        return new ResponseEntity<>(brandResponses, HttpStatus.OK);
    }
    @GetMapping("/types")
    public ResponseEntity<List<TypeResponse>> getTypes(){
        List<TypeResponse> typeResponses = typeService.getAllTypes();
        return new ResponseEntity<>(typeResponses, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam("keyword") String keyword){
        List<ProductResponse> productResponses = productService.searchProductsByName(keyword);
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody Product product) {
        ProductResponse productResponse = productService.createProduct(product);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Integer productId, @RequestBody Product product) {
        ProductResponse productResponse = productService.updateProduct(productId, product);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{productId}/increment")
    public ResponseEntity<Void> incrementProductQuantity(@PathVariable Integer productId, @RequestParam Integer amount) {
        productService.incrementQuantity(productId, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/decrement")
    public ResponseEntity<Void> decrementProductQuantity(@PathVariable Integer productId, @RequestParam Integer amount) {
        productService.decrementQuantity(productId, amount);
        return ResponseEntity.ok().build();
    }




}
