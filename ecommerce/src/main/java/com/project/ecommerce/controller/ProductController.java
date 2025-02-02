package com.project.ecommerce.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ecommerce.model.Product;
import com.project.ecommerce.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
	}

	@GetMapping("/product/{Id}")
	public ResponseEntity<Product> getProductById(@PathVariable int Id) {

		Product product = service.getProductById(Id);
		if (product != null)
			return new ResponseEntity<>(product, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
		try {
			Product product1 = service.addProduct(product, imageFile);
			return new ResponseEntity<>(product1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/product/{prodId}/image")
	public ResponseEntity<byte[]> getImageByProductId(@PathVariable int prodId) {
		Product product = service.getProductById(prodId);

		if (product == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageFile = product.getImageData();

		
		if (imageFile == null || imageFile.length == 0) {
			return ResponseEntity.notFound().build();
		}

		String imageType = product.getImageType();
		if (imageType == null || imageType.isEmpty()) {
			imageType = "image/jpeg"; 
		}

		return ResponseEntity.ok().contentType(MediaType.valueOf(imageType)).body(imageFile);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,
			@RequestPart MultipartFile imageFile) {
		Product product1 = null;
		try {
			product1 = service.updateProduct(id, product, imageFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		if (product1 != null)
			return new ResponseEntity<>("Updated", HttpStatus.OK);
		else
			return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id){
		
		Product product = service.getProductById(id);
		
		if (product != null) {
			service.deleteProduct(id);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("Failed to Delete", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword)
	{
		List<Product> products  = service.searchProducts(keyword);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}

}
