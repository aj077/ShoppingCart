package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Cart;
import com.example.entity.CartItems;
import com.example.entity.Customer;
import com.example.entity.Product;
import com.example.repo.CartItemsRepo;
import com.example.repo.CartRepo;
import com.example.repo.CustomerRepo;
import com.example.repo.ProductRepo;

@Controller
public class CartController {

	@Autowired
	ProductRepo prdRepo;
	
	@Autowired
	CustomerRepo custRepo;
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	CartItemsRepo cartItemsRepo;
	
	@GetMapping("/view-cart")
	public String viewCart(Principal principal, Model model) {
		try {
			if(principal != null) {
				String cust_email = principal.getName();
				Customer customer = custRepo.findByEmail(cust_email);
				Cart cart = customer.getCart();
				List<CartItems> cartItems = cart.getCartItems();
				model.addAttribute("cart", cart);
				model.addAttribute("cartItems", cartItems);
				return "/cart/cart-home";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@GetMapping("/add-cart")
	public String addProductInCart(@RequestParam("productId") int productId, Model model, Principal principal) {
		Optional<Product> optional = prdRepo.findById(productId);
		Product product = null;
		if(optional.isPresent()) {
			product = optional.get();
		}
		
		String cust_email = principal.getName();
		Customer customer = custRepo.findByEmail(cust_email);
		Cart cart = null;
		boolean productPresentInCart = false;
		try {
			cart = customer.getCart();
			if(cart != null) {
				List<CartItems> cartItems = cart.getCartItems();
				for(CartItems item : cartItems) {
					Product productFromCartItems = item.getProduct(); 
					if(productFromCartItems.getId() == productId) {
						item.setQuantity(item.getQuantity()+1);
						productPresentInCart = true;
					}
					
				}
				cartItemsRepo.saveAll(cartItems);
				if(productPresentInCart == false) {
					CartItems newCartItems = new CartItems();
					newCartItems.setProduct(product);
					newCartItems.setQuantity(1);
					newCartItems.setCart(cart);
					cartItemsRepo.save(newCartItems);
				}
				cartRepo.save(cart);
			}else {
				Cart newCart = new Cart();
				CartItems newCartItems = new CartItems();
				newCartItems.setProduct(product);
				newCartItems.setQuantity(1);
				newCartItems.setCart(cart);
				cartItemsRepo.save(newCartItems);
				newCart.setCustomer(customer);
				List<CartItems> newCartItemsList = new ArrayList<CartItems>();
				newCartItemsList.add(newCartItems);
				newCart.setCartItems(newCartItemsList);
				cartRepo.save(newCart);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("cart", cart);
 		return "redirect:/view-cart";
	}
	
	@GetMapping("/delete-cart")
	public String deleteProductFromCart(@RequestParam("productId") int productId, Model model, Principal principal) {
		Optional<Product> optional = prdRepo.findById(productId);
		Product product = null;
		if(optional.isPresent()) {
			product = optional.get();
		}
		String cust_email = principal.getName();
		Customer customer = custRepo.findByEmail(cust_email);
		Cart cart = customer.getCart();
		List<CartItems> cartItems = cart.getCartItems();
		for(CartItems cartItem : cartItems) {
			Product productFromCartItems = cartItem.getProduct();
			if(productId == productFromCartItems.getId()) {
				if(cartItem.getQuantity() > 0) {
					cartItem.setQuantity(cartItem.getQuantity()-1);
				}
				if(cartItem.getQuantity() == 0) {
					cartItemsRepo.deleteById(cartItem.getId());
					return "redirect:/view-cart";
				}
			}
		}
		cartItemsRepo.saveAll(cartItems);
		return "redirect:/view-cart";
	}
}
