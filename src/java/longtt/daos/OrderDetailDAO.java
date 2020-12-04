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
import longtt.dtos.CakeDTO;
import longtt.dtos.OrderDetailDTO;

/**
 *
 * @author Long
 */
public class OrderDetailDAO implements Serializable {

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
    
    public boolean createOrderDetail(int orderId, CakeDTO cake) throws Exception {
        boolean check = false;
        try {
            String query = "Insert Into tblOrderDetail(OrderID, CakeID, Quantity, TotalPrice) values (?,?,?,?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, orderId);
            preStm.setInt(2, cake.getId());
            preStm.setInt(3, cake.getCartQty());
            preStm.setFloat(4, cake.getCartQty() * cake.getPrice());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public List<OrderDetailDTO> getOrderDetailByOrderID(int id) throws Exception {
        List<OrderDetailDTO> list = new ArrayList<>();
        OrderDetailDTO dto = null;
        String cakeName;
        int detailId, cakeId, quantity;
        float total;
        try {
            String query = "Select A.DetailID, A.CakeID, B.Name, A.Quantity, A.TotalPrice "
                         + "from tblOrderDetail A, tblCake B "
                         + "Where A.CakeID = B.CakeID AND A.OrderID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, id);
            rs = preStm.executeQuery();
            while (rs.next()) {
                detailId = rs.getInt("DetailID");
                cakeId = rs.getInt("CakeID");
                cakeName = rs.getString("Name");
                quantity = rs.getInt("Quantity");
                total = rs.getFloat("TotalPrice");
                dto = new OrderDetailDTO(detailId, id, cakeId, quantity, total);
                dto.setCakeName(cakeName);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
}
