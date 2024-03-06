package com.example.excelwithcode.controller;


import com.example.excelwithcode.model.ProductsModel;
import com.example.excelwithcode.model.UsersModel;
import com.example.excelwithcode.service.ProductsService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductsController {
    private final ProductsService productsService;
    @Autowired
    public  ProductsController(ProductsService productsService){
        this.productsService = productsService;
    }
    // read all
    @GetMapping("products")
    public String getAll(Model model){
        List<ProductsModel> productModels = productsService.getAllProduct();
        model.addAttribute("productModels", productModels);
        return "Products/index";
    }
    // create form
    @GetMapping("products/api/create")
    public String ShowFormCreate(Model model){
        ProductsModel productsModel = new ProductsModel();
        model.addAttribute("productsModel",productsModel);
        return "Products/create";
    }

    //update form
    @GetMapping("/products/api/edit/{id}")
    public String ShowFormUpdate(@PathVariable Long id, Model model){
        ProductsModel productsModel = productsService.getProductById(id);
        model.addAttribute("productsModel", productsModel);
        return "Products/update";
    }
    //delete form
    @GetMapping("/products/api/delete/{id}")
    public String ShowFormDelete(@PathVariable Long id, Model model){
        ProductsModel productsModel= productsService.getProductById(id);
        model.addAttribute("productsModel", productsModel);
        return "Products/delete";
    }

    //create
    @PostMapping( "products/api/create" )
    public ResponseEntity<ProductsModel> createProduct(ProductsModel productsModel){
        try{

            ProductsModel productsModel1 = new ProductsModel();
            productsModel1.setProduct_name(productsModel.getProduct_name());
            productsModel1.setPrice(productsModel.getPrice());
            productsModel1.setQuality(productsModel.getQuality());

            productsService.setProduct(productsModel1);
            return new ResponseEntity<>( productsModel1, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/products/api/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model){
        try {
            productsService.deleteProducts(id);
            model.addAttribute("message", "User deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "Products/index";

    }

    @PostMapping("/products/api/update/{id}")
    public ResponseEntity<ProductsModel> updateUser(@PathVariable Long id, ProductsModel productsModel) {
        try {
            ProductsModel existingProduct = productsService.getProductById(id);

            if (existingProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            existingProduct.setProduct_name(productsModel.getProduct_name());
            existingProduct.setQuality(productsModel.getQuality());
            existingProduct.setPrice(productsModel.getPrice());


            productsService.updateProduct(existingProduct);

            return new ResponseEntity<>(existingProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
