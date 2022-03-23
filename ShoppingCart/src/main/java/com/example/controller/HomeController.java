package com.example.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Cart;
import com.example.entity.Comments;
import com.example.entity.Customer;
import com.example.entity.Product;
import com.example.repo.CommentsRepo;
import com.example.repo.CustomerRepo;
import com.example.repo.ProductRepo;

@Controller
public class HomeController {
	
	@Autowired
	ProductRepo prdRepo;
	
	@Autowired
	CommentsRepo cmmRepo;
	
	@Autowired
	CustomerRepo cRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/")
	public String getHome(Model model) {
		List<Product> products = prdRepo.findAll();
		model.addAttribute("products",products);
		return "home";
	}
	
	@GetMapping("/view-product")
	public String viewProduct(@RequestParam("productId") int productId, Model model) {
		Optional<Product> opt = prdRepo.findById(productId);
		if(opt.isPresent()) {
			Product product = opt.get();
			model.addAttribute("product", product);
			model.addAttribute("title",product.getName());
			return "product-details";
		}
		return "";
	}
	
	@GetMapping("/add-comments")
	public String addComments(@RequestParam("productId") int productId, Model model) {
		Comments comments = new Comments();
		Optional<Product> opt = prdRepo.findById(productId);
		if(opt.isPresent()) {
			Product product = opt.get();
			model.addAttribute("product", product);
			model.addAttribute("title",product.getName());
			model.addAttribute("comments", comments);
			return "add-comment-form";
		}
		return "redirect:/";
	}
	
	@PostMapping("/submit-comments")
	public String submitComments(@ModelAttribute("comments") Comments comments, @RequestParam("productId") int productId) {
		Optional<Product> opt = prdRepo.findById(productId);
		if(opt.isPresent()) {
			Product product = opt.get();
			comments.setProduct(product);
			cmmRepo.save(comments);
		}
		return "redirect:/";
	}
	
	@GetMapping("/signup")
	public String getSignup(Model model) {
		Customer cust = new Customer();
		model.addAttribute("customer", cust);
		return "signup-form";
	}
	
	@PostMapping("/submit")
	public String submitForm(@ModelAttribute("customer") Customer cust, Model model) {
		System.out.println("Customer Email: "+cust.getEmail());
		Customer customer = cRepo.findByEmail(cust.getEmail());
		if(customer == null) {
			Date date = new Date();
			Cart cart = new Cart();
			cart.setCustomer(cust);
			cust.setCart(cart);
			cust.setCustomerSince(date);
			cust.setRole("ROLE_CUSTOMER");
			cust.setPassword(encoder.encode(cust.getPassword()));
			cRepo.save(cust);
			return "redirect:/";
		}
		else {
			String error = "*Email Already used by another Customer";
			model.addAttribute("error", error);
			return "signup-form";
		}
	
		
	}

}
