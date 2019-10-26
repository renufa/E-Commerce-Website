package com.e_commerce.backend.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.e_commerce.backend.files.FileUpload;
import com.e_commerce.backend.models.Product;
import com.e_commerce.backend.repositories.ProductRepository;

@RequestMapping(value = "/products")
@RestController
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	FileUpload fileUpload;
	@Autowired
	ProductRepository productRepository;
	public String pCode = null;

	@PostMapping(value = "/addFile")
	public ResponseEntity<?> addProductFile(@RequestParam("pFile") MultipartFile pFile) {
		LOGGER.info("From class ProductController ,method : addProductFile()");
		if ((pFile.getContentType().equals("image/jpeg") || pFile.getContentType().equals("image/jpg")
				|| pFile.getContentType().equals("image/png") || pFile.getContentType().equals("image/gif"))) {

			this.pCode = "P" + UUID.randomUUID().toString().substring(26).toUpperCase();

			try {
				FileUpload.productFileUpload(pFile, pCode);
			} catch (IOException e) {
				return ResponseEntity.badRequest().body(null);
			}
			LOGGER.info(this.pCode);
			LOGGER.info("From class ProductController ,method : addProductFile(),Image uploaded");
			return ResponseEntity.ok().body(" success file upload ");
		} else {
			LOGGER.info("From class ProductController ,method : addProductFile(), File not an image that is rejected");
			return ResponseEntity.badRequest().body(null);

		}

	}

	@PostMapping(value = "/addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		LOGGER.info("From class ProductController ,method : addProduct()");

		if (!(this.pCode.equals(null))) {

			LOGGER.info("piCode : " + this.pCode);
			product.setName(product.getName());
            product.setBrand(product.getBrand());
			product.setDescription(product.getDescription());
			product.setPrice(product.getPrice());
			product.setDiscount(product.getDiscount());
			product.setColor(product.getColor());
			product.setQuantity(product.getQuantity());
			product.setpCode(this.pCode);
			product.setCategory(product.getCategory());
			

			productRepository.save(product);

			LOGGER.info("pCode : " + this.pCode);
			this.pCode = null;
			product.setId(null);

		}
		return ResponseEntity.ok().body("Operation success !");

	}

	@GetMapping(value = "/getAllProducts")
	public ResponseEntity<List<Product>> getAllProduct() {

		LOGGER.info("From class ProductController ,method : getAllProducts()");

		List<Product> products = productRepository.findAll();
		return ResponseEntity.ok().body(products);

	}

}
