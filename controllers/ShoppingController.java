package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.services.OrderServices;
import com.example.demo.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;

@Controller
public class ShoppingController {

    @Autowired
    private OrderServices orderServices;

    @Autowired
    private UserServices userServices;

    /**
     * Display shopping cart page
     */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("cartPage", true);
        model.addAttribute("userName", user.getUname());
        return "Cart";
    }

    /**
     * Display checkout page with address form
     */
    @GetMapping("/checkout")
    public String viewCheckout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("checkoutPage", true);
        model.addAttribute("userName", user.getUname());
        model.addAttribute("userEmail", user.getUemail());
        model.addAttribute("userPhone", user.getUnumber());
        return "Checkout";
    }

    /**
     * Display payment page
     */
    @GetMapping("/payment")
    public String viewPayment(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("paymentPage", true);
        model.addAttribute("userName", user.getUname());
        return "Payment";
    }

    /**
     * Process payment and create order
     */
    @PostMapping("/process-payment")
    public String processPayment(
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("subtotal") Double subtotal,
            @RequestParam("deliveryFee") Double deliveryFee,
            @RequestParam("tax") Double tax,
            @RequestParam("total") Double total,
            @RequestParam("cartItems") String cartItemsJson,
            @RequestParam(value = "addressType", defaultValue = "home") String addressType,
            @RequestParam(value = "fullName", defaultValue = "") String fullName,
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "street", defaultValue = "") String street,
            @RequestParam(value = "city", defaultValue = "") String city,
            HttpSession session,
            Model model) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/";
        }

        try {
            // Parse cart items
            ObjectMapper mapper = new ObjectMapper();
            JsonNode cartNode = mapper.readTree(cartItemsJson);

            // Create order for each item in cart
            int orderCount = 0;
            double totalOrderAmount = total;

            if (cartNode.isArray()) {
                for (JsonNode item : cartNode) {
                    Orders order = new Orders();
                    order.setoName(item.get("name").asText());
                    order.setoPrice(item.get("price").asDouble());
                    order.setoQuantity(item.get("quantity").asInt());
                    order.setTotalAmmout(item.get("price").asDouble() * item.get("quantity").asInt());
                    order.setOrderDate(new Date());
                    order.setUser(user);
                    
                    // Save order
                    orderServices.saveOrder(order);
                    orderCount++;
                }
            }

            // Create order ID based on timestamp and count
            long timestamp = System.currentTimeMillis();
            String orderId = "ORD" + timestamp;

            // Prepare order summary for confirmation page
            model.addAttribute("orderId", orderId);
            model.addAttribute("orderTotal", String.format("%.2f", totalOrderAmount));
            model.addAttribute("deliveryAddress", street + ", " + city);
            model.addAttribute("paymentMethod", paymentMethod);
            model.addAttribute("estimatedDelivery", "30-40 minutes");

            // Store order info in session for template
            session.setAttribute("lastOrderId", orderId);
            session.setAttribute("lastOrderTotal", totalOrderAmount);

            return "OrderConfirmation";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Payment processing failed: " + e.getMessage());
            return "Payment";
        }
    }

    /**
     * Display order confirmation page
     */
    @GetMapping("/order-confirmation")
    public String orderConfirmation(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/";
        }

        String orderId = (String) session.getAttribute("lastOrderId");
        Double orderTotal = (Double) session.getAttribute("lastOrderTotal");

        model.addAttribute("orderId", orderId != null ? orderId : "ORD000");
        model.addAttribute("orderTotal", orderTotal != null ? orderTotal : 0.0);
        model.addAttribute("userName", user.getUname());

        return "OrderConfirmation";
    }

    /**
     * Get user's order history
     */
    @GetMapping("/orders/history")
    public String orderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/";
        }

        List<Orders> userOrders = orderServices.getOrdersForUser(user);
        model.addAttribute("orders", userOrders);
        model.addAttribute("userName", user.getUname());
        model.addAttribute("totalOrders", userOrders.size());

        return "OrderHistory";
    }

    /**
     * Clear cart (called via AJAX)
     */
    @PostMapping("/api/cart/clear")
    @ResponseBody
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "{\"status\": \"success\", \"message\": \"Cart cleared\"}";
    }
}
