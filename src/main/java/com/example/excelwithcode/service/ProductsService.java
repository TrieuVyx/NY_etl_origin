package com.example.excelwithcode.service;

import com.example.excelwithcode.model.ProductsModel;
import com.example.excelwithcode.model.UsersModel;
import com.example.excelwithcode.repository.ProductsRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    private final ProductsRepository productRepository;
    public ProductsService(ProductsRepository productRepository){
        this.productRepository =productRepository;
    }
    //all user
    public List<ProductsModel> getAllProduct(){
        return productRepository.findAll();
    }
    // id
    public ProductsModel getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public void setProduct(ProductsModel productsModel1) {
        productRepository.save(productsModel1);
    }
    //delete
    public void deleteProducts(Long id) throws ChangeSetPersister.NotFoundException {
        Optional<ProductsModel> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductsModel Product = optionalProduct.get();
            productRepository.delete(Product);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }
    //update
    public void updateProduct(ProductsModel productsModel){
        productRepository.save(productsModel);
    }


}
