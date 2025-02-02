package com.project.ecommerce.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.ecommerce.model.Product;
import com.project.ecommerce.repository.ProductRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepo repo;

	public List<Product> getAllProducts() {

		return repo.findAll();
	}

	public Product getProductById(int id) {

		return repo.findById(id).orElse(null);
	}

	public Product addProduct(Product product, MultipartFile imageFile) throws IOException {

		product.setImageName(imageFile.getOriginalFilename());
		product.setImageType(imageFile.getContentType());
		product.setImageData(imageFile.getBytes());
		return repo.save(product);

	}

	public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

	    Optional<Product> existingProduct = repo.findById(id);

	    if (existingProduct.isPresent()) {
	        Product updatedProduct = existingProduct.get();

	 
	        if (product.isAvailable() != null) updatedProduct.setAvailable(product.isAvailable());
	        if (product.getBrand() != null) updatedProduct.setBrand(product.getBrand());
	        if (product.getCategory() != null) updatedProduct.setCategory(product.getCategory());
	        if (product.getDescription() != null) updatedProduct.setDescription(product.getDescription());
	        if (product.getName() != null) updatedProduct.setName(product.getName());
	        if (product.getPrice() != null) updatedProduct.setPrice(product.getPrice());
	        if (product.getQuantity() != 0) updatedProduct.setQuantity(product.getQuantity());
	        if (product.getReleaseDate() != null) updatedProduct.setReleaseDate(product.getReleaseDate());


	        if (imageFile != null && !imageFile.isEmpty()) {
	            updatedProduct.setImageName(imageFile.getOriginalFilename());
	            updatedProduct.setImageType(imageFile.getContentType());
	            updatedProduct.setImageData(imageFile.getBytes());
	        }

	        return repo.save(updatedProduct);
	    } else {
	        throw new EntityNotFoundException("Product with ID " + id + " not found.");
	    }
	}


	public void deleteProduct(int id) {
		
		 repo.deleteById(id);
	}

	public List<Product> searchProducts(String keyword) {
		// TODO Auto-generated method stub
		return repo.searchProducts(keyword);
	}

}
