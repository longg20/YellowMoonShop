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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import longtt.dtos.UserDTO;

/**
 *
 * @author Long
 */
public class UserDAO implements Serializable {

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

    public UserDTO checkLogin(String id, String password) throws Exception {
        UserDTO dto = null;
        try {
            String query = "Select Name, Phone, Address, Role from tblUser where UserID = ? AND Password = ? AND Status = 1";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, id);
            preStm.setString(2, password);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                int role = rs.getInt("Role");
                dto = new UserDTO(id, name, phone, address, role, 1);
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
}
