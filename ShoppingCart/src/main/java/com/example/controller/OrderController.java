package com.example.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Cart;
import com.example.entity.CartItems;
import com.example.entity.Customer;
import com.example.entity.Order;
import com.example.entity.OrderItems;
import com.example.entity.Product;
import com.example.repo.CartItemsRepo;
import com.example.repo.CartRepo;
import com.example.repo.CustomerRepo;
import com.example.repo.OrderItemsRepo;
import com.example.repo.OrderRepo;
import com.example.repo.ProductRepo;

@Controller
public class OrderController {
	
	@Autowired
	ProductRepo prdRepo;
	
	@Autowired
	CustomerRepo custRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	OrderItemsRepo orderItemsRepo;
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
	CartItemsRepo cartItemsRepo;

	@GetMapping("/order")
	public String addToOrder(@RequestParam("productId") int productId, Model model, Principal principal) {
		Date date = new Date();
		String currDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
		Optional<Product> optional = prdRepo.findById(productId);
		Product product = null;
		if(optional.isPresent()) {
			product = optional.get();
		}
		String cust_email = principal.getName();
		Customer customer = custRepo.findByEmail(cust_email);
		Order order = new Order();
		OrderItems orderItems =  new OrderItems();
		Cart cart = customer.getCart();
		List<Order> ordersList = customer.getOrdersList();
		List<CartItems> cartItems = cart.getCartItems();
		List<OrderItems> orderItemsList = new ArrayList<OrderItems>();
		int prdQty = 0;
		int cartItemId = -1;
		BigDecimal prdPrice = null;
		for(CartItems item : cartItems) {
			if(productId == item.getProduct().getId()) {
				orderItems.setProduct(item.getProduct());
				orderItems.setQuantity(item.getQuantity());
				orderItems.setOrder(order);
				prdQty = item.getQuantity();
				prdPrice = item.getProduct().getPrice();
				cartItemId = item.getId();
			}
		}
		if(ordersList != null && !ordersList.isEmpty()) {
			ordersList.add(order);
		}
		else {
			ordersList = new ArrayList<Order>();
			ordersList.add(order);
		}
		orderItemsList.add(orderItems);
		order.setCustomer(customer);
		order.setAmount(BigDecimal.valueOf(prdPrice.intValueExact() * prdQty));
		order.setOrderItems(orderItemsList);
		order.setDate(currDate);
		customer.setOrdersList(ordersList);
		orderRepo.save(order);
		orderItemsRepo.save(orderItems);
		custRepo.save(customer);
		model.addAttribute("order", order);
		return "/order/order-home";
	}
	
	@GetMapping("/order-all")
	public String orderAll(@RequestParam("cartId") int cartId, Principal principal, Model model) {
		String cust_email = principal.getName();
		Customer customer = custRepo.findByEmail(cust_email);
		Cart cart = cartRepo.getById(cartId);
		List<CartItems> cartItemsList = cart.getCartItems();
		Date date = new Date();
		String currDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
		Order order = new Order();
		List<Order> ordersList = customer.getOrdersList();
		List<OrderItems> orderItemsList = new ArrayList<OrderItems>();
		List<BigDecimal> prdPriceList = new ArrayList<BigDecimal>();
		List<Integer> prdQtyList = new ArrayList<Integer>();
		for(CartItems item : cartItemsList) {
			OrderItems orderItems = new OrderItems();
			orderItems.setProduct(item.getProduct());
			orderItems.setQuantity(item.getQuantity());
			orderItems.setOrder(order);
			orderItemsList.add(orderItems);
			prdPriceList.add(item.getProduct().getPrice());
			prdQtyList.add(item.getQuantity());
		}
		int totalAmount = 0;
		for(int i=0; i<prdPriceList.size(); i++) {
			totalAmount = totalAmount + (prdPriceList.get(i).intValueExact() * prdQtyList.get(i));
		}
		if(ordersList != null && !ordersList.isEmpty()) {
			ordersList.add(order);
		}
		else {
			ordersList = new ArrayList<Order>();
			ordersList.add(order);
		}
		order.setAmount(BigDecimal.valueOf(totalAmount));
		order.setCustomer(customer);
		order.setOrderItems(orderItemsList);
		order.setDate(currDate);
		customer.setOrdersList(ordersList);
		model.addAttribute("order", order);
		orderRepo.save(order);
		orderItemsRepo.saveAll(orderItemsList);
		custRepo.save(customer);
		return "/order/order-home";
	}
	
	@GetMapping("/payment")
	public String payment(@RequestParam("orderId") int orderId, Principal principal) {
		String cust_email = principal.getName();
		Customer customer = custRepo.findByEmail(cust_email);
		Cart cart = customer.getCart();
		Order order = orderRepo.getById(orderId);
		List<OrderItems> orderItemsList = new ArrayList<OrderItems>();
		List<CartItems> cartItemsList = new ArrayList<CartItems>();
		Set<Integer> productsIdFromOrderItemsIdSet = new HashSet<Integer>();
		orderItemsList = order.getOrderItems();
		cartItemsList = cart.getCartItems();
		for(OrderItems orderItem : orderItemsList) {
			productsIdFromOrderItemsIdSet.add(orderItem.getProduct().getId());
		}
		Iterator<CartItems> itr = cartItemsList.iterator();
		while(itr.hasNext()) {
			CartItems cartItem = itr.next();
			if(productsIdFromOrderItemsIdSet.contains(cartItem.getProduct().getId())) {
				itr.remove();
				cartItemsRepo.delete(cartItem);
			}
		}
		cart.setCartItems(cartItemsList);
		cartRepo.save(cart);
		return "/order/payment";
	}
	
	@GetMapping("/order-history")
	public String orderHistory(Principal principal, Model model) {
		String cust_email = principal.getName();
		Customer customer = custRepo.findByEmail(cust_email);
		List<Order> ordersList = customer.getOrdersList();
		model.addAttribute("orders", ordersList);
		return "/order/order-history";
	}
	
	@GetMapping("/view-order-details")
	public String orderDetails(@RequestParam("orderId") int orderId, Model model) {
		Order order = orderRepo.getById(orderId);
		model.addAttribute("order", order);
		return "/order/order-home";
	}
}
