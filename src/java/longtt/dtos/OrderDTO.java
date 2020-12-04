/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.dtos;

import java.io.Serializable;

/**
 *
 * @author Long
 */
public class OrderDTO implements Serializable {
    String userId, date, name, phone, address, paymentMethod;
    int id;
    float total;
    boolean paymentStatus;

    public OrderDTO() {
    }

    public OrderDTO(int id, float total, String date, String name, String phone, 
                     String address, String paymentMethod, boolean paymentStatus) {   //guest get
        this.id = id;
        this.total = total;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
    
    public OrderDTO(int id, String userId, float total, String date, String name, 
                     String phone, String address, String paymentMethod, boolean paymentStatus) {    //user get
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public OrderDTO(int id, float total, String name, String phone, 
                     String address, String paymentMethod, boolean paymentStatus) {     //guest insert
        this.id = id;
        this.total = total;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
    
    public OrderDTO(int id, String userId, float total, String name, String phone, 
                     String address, String paymentMethod, boolean paymentStatus) {     //user insert
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
