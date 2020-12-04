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
import longtt.dtos.CategoryDTO;

/**
 *
 * @author Long
 */
public class CategoryDAO implements Serializable {

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
    
    public List<CategoryDTO> getAllCategories() throws Exception {
        List<CategoryDTO> list = new ArrayList<>();
        CategoryDTO dto = null;
        int id;
        String name;
        try {
            String query = "Select CategoryID, Name from tblCategory";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getInt("CategoryID");
                name = rs.getString("Name");
                dto = new CategoryDTO(id, name);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
}
