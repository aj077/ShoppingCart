package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Customer;
import com.example.entity.Product;
import com.example.helper.FileUploadHelper;
import com.example.repo.CommentsRepo;
import com.example.repo.CustomerRepo;
import com.example.repo.ProductRepo;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	ProductRepo prdRepo;
	
	@Autowired
	CommentsRepo commRepo;
	
	@Autowired
	CustomerRepo cRepo;

	@Autowired
	FileUploadHelper fileUploadHelper;
	
	@Autowired
	ServletContext context;
	
	@GetMapping("/home")
	public String getHome() {
		return "/admin/admin-home";
	}
	
	@GetMapping("/products-list")
	public String getProductsList(Model model) {
		List<Product> products = prdRepo.findAll();
		model.addAttribute("products",products);
		return "/admin/admin-products-list";
	}
	
	@GetMapping("/add-product")
	public String addProduct(Model model) {
		Product product = new Product();
		model.addAttribute("product",product);
		return "/admin/add-product-form";
	}
	
	@PostMapping("/save-product")
	public String saveProduct(@ModelAttribute Product product) {
		System.out.println("Product Name 1: "+product.getName());
		String path="";
		String fileName="";
		String ext="";
		try {
			fileName = product.getFile().getOriginalFilename();
			if(fileName.contains(".")) {
				ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	            fileName = fileName.substring(0,fileName.lastIndexOf("."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		prdRepo.save(product);
		path = context.getContextPath()+"/images/"+fileName+"-"+product.getId()+"."+ext;
		product.setImagepath(path);
		prdRepo.save(product);
		System.out.println("Product Name 2: "+product.getName());
		boolean b = fileUploadHelper.uploadFile(product.getFile(), product.getId());
		System.out.println("File saved: "+b);
		return "redirect:/admin/products-list";
	}
	
	@GetMapping("/view-product-details")
	public String viewDetails(@RequestParam("productId") int productId, Model model) {
		Optional<Product> opt = prdRepo.findById(productId);
		if(opt.isPresent()) {
			Product product = opt.get();
			model.addAttribute("product", product);
			model.addAttribute("title",product.getName());
			return "/admin/admin-product-details";
		}
		return "";
	}
	
	@GetMapping("/delete-product")
	public String deleteProduct(@RequestParam("productId") int productId) {
		Optional<Product> opt = prdRepo.findById(productId);
		if(opt.isPresent()) {
			prdRepo.deleteById(productId);
		}
		return "redirect:/admin/products-list";
	}
	
	@GetMapping("/customers-list")
	public String getCustomers(Model model) {
		List<Customer> customers = cRepo.findAll();
		model.addAttribute("customers", customers);
		return "/admin/admin-customers-list";
	}
	
	@GetMapping("/update-product")
	public String updateProduct(@RequestParam("productId") int productId, Model model) {
		Optional<Product> opt = prdRepo.findById(productId);
		if(opt.isPresent()) {
			model .addAttribute("product", opt.get());
			return "/admin/add-product-form";
		}
		return "";
	}
}
