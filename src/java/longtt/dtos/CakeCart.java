/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.dtos;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Long
 */
public class CakeCart implements Serializable {
    private String userId;
    private HashMap<Integer, CakeDTO> cart;
    float total;
    
    public String getUserId() {
        return userId;
    }

    public HashMap<Integer, CakeDTO> getCart() {
        return cart;
    }

    public CakeCart() {
        this.userId = "guest";
        this.cart = new HashMap<Integer, CakeDTO>();
        this.total = 0;
    }
    
    public CakeCart(String userId) {
        this.userId = userId;
        this.cart = new HashMap<Integer, CakeDTO>();
        this.total = 0;
    }
    
    public void addToCart(CakeDTO dto) throws Exception {
        if (this.cart.containsKey(dto.getId())) {
            int cartQty = this.cart.get(dto.getId()).getCartQty() + dto.getCartQty();
            dto.setCartQty(cartQty);
        }
        this.cart.put(dto.getId(), dto);
    }
    
    public void removeFromCart(int id) throws Exception {
        if (this.cart.containsKey(id))
            this.cart.remove(id);
    }
    
    public void updateCart(int id, int qty) throws Exception {
        if (this.cart.containsKey(id))
            this.cart.get(id).setCartQty(qty);
    }

    public void updateQuantity(int id, int qty) throws Exception {
        if (this.cart.containsKey(id))
            this.cart.get(id).setQuantity(qty);
    }
    
    public void setTotal(float total) {
        this.total = total;
    }
    
    public float getTotal() throws Exception {
        this.total = 0;
        for (CakeDTO dto : this.cart.values())
            this.total = this.total + dto.getPrice() * dto.getCartQty();
        return this.total;
    }
}
