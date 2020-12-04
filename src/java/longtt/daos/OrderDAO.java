/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.daos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import longtt.dtos.OrderDTO;

/**
 *
 * @author Long
 */
public class OrderDAO implements Serializable {

    private Connection conn = null;
    private PreparedStatement preStm = null;
    private ResultSet rs = null;

    private void closeConnection() throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (preStm != null) {
            preStm.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    public static Connection getMyConnection() throws Exception {
        Connection conn = null;
        Context context = new InitialContext();
        Context end = (Context) context.lookup("java:comp/env");
        DataSource ds = (DataSource) end.lookup("DBCon");
        conn = ds.getConnection();
        return conn;
    }

    public boolean createUserOrder(OrderDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert Into tblOrder(OrderID, UserID, Total, Date, Name, Phone, Address, [Payment Method], [Payment Status]) "
                         + "values (?,?,?,GETDATE(),?,?,?,?,?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, dto.getId());
            preStm.setString(2, dto.getUserId());
            preStm.setFloat(3, dto.getTotal());
            preStm.setString(4, dto.getName());
            preStm.setString(5, dto.getPhone());
            preStm.setString(6, dto.getAddress());
            preStm.setString(7, dto.getPaymentMethod());
            preStm.setBoolean(8, dto.isPaymentStatus());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public boolean createGuestOrder(OrderDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert Into tblOrder(OrderID, Total, Date, Name, Phone, Address, [Payment Method], [Payment Status]) "
                         + "values (?,?,GETDATE(),?,?,?,?,?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, dto.getId());
            preStm.setFloat(2, dto.getTotal());
            preStm.setString(3, dto.getName());
            preStm.setString(4, dto.getPhone());
            preStm.setString(5, dto.getAddress());
            preStm.setString(6, dto.getPaymentMethod());
            preStm.setBoolean(7, dto.isPaymentStatus());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public int getOrderId() throws Exception {
        int count = 0;
        try {
            String query = "Select COUNT(OrderID)+1 as Count From tblOrder";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            rs = preStm.executeQuery();
            if (rs.next()) {
                count = rs.getInt("Count");
            }
        } finally {
            closeConnection();
        }
        return count;
    }
    
    public List<OrderDTO> getOrdersByLikeId(String search, String userId) throws Exception {
        List<OrderDTO> list = new ArrayList<>();
        OrderDTO dto = null;
        String date, name, phone, address, paymentMethod;
        int orderId;
        float total;
        boolean paymentStatus;
        try {
            String query = "Select OrderID, UserID, Total, convert(varchar,[Date],0) as [Date], Name, Phone, Address, [Payment Method], [Payment Status] "
                         + "from tblOrder "
                         + "Where OrderID LIKE ? AND UserID = ? "
                         + "Order By OrderID DESC";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + search + "%");
            preStm.setString(2, userId);
            rs = preStm.executeQuery();
            while (rs.next()) {
                orderId = rs.getInt("OrderID");
                total = rs.getFloat("Total");
                date = rs.getString("Date");
                name = rs.getString("Name");
                phone = rs.getString("Phone");
                address = rs.getString("Address");
                paymentMethod = rs.getString("Payment Method");
                paymentStatus = rs.getBoolean("Payment Status");
                dto = new OrderDTO(orderId, userId, total, date, name, phone, address, paymentMethod, paymentStatus);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
    
    public OrderDTO getOrderById(int id) throws Exception {
        OrderDTO dto = null;
        try {
            String query = "Select UserID, Total, convert(varchar,[Date],0) as [Date], Name, Phone, Address, [Payment Method], [Payment Status] "
                         + "from tblOrder "
                         + "Where OrderID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, id);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("UserID");
                float total = rs.getFloat("Total");
                String date = rs.getString("Date");
                String name = rs.getString("Name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                String paymentMethod = rs.getString("Payment Method");
                boolean paymentStatus = rs.getBoolean("Payment Status");
                dto = new OrderDTO(id, userId, total, date, name, phone, address, paymentMethod, paymentStatus);
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
}
