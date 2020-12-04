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

/**
 *
 * @author Long
 */
public class CakeDAO implements Serializable {

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

    public CakeDTO getCakeById(int id) throws Exception {
        CakeDTO dto = null;
        try {
            String query = "Select Name, Image, Description, Category, convert(varchar,[Create Date],23) as [Create Date], "
                    + "convert(varchar,[Expiration Date],23) as [Expiration Date], Price, Quantity, Status "
                    + "From tblCake "
                    + "Where CakeID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, id);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String image = rs.getString("Image");
                String description = rs.getString("Description");
                int category = rs.getInt("Category");
                String createDate = rs.getString("Create Date");
                String expirationDate = rs.getString("Expiration Date");
                float price = rs.getFloat("Price");
                int quantity = rs.getInt("Quantity");
                int status = rs.getInt("Status");
                dto = new CakeDTO(id, name, image, description, category, createDate, expirationDate, price, quantity, status);
            }
        } finally {
            closeConnection();
        }
        return dto;
    }

    public boolean updateCake(CakeDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Update tblCake set Name = ?, Image = ?, Description = ?, Category = ?, [Create Date] = ?, "
                    + "[Expiration Date] = ?, Price = ?, Quantity = ?, Status = ? "
                    + "Where CakeID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getName());
            preStm.setString(2, dto.getImage());
            preStm.setString(3, dto.getDescription());
            preStm.setInt(4, dto.getCategory());
            preStm.setString(5, dto.getCreateDate());
            preStm.setString(6, dto.getExpirationDate());
            preStm.setFloat(7, dto.getPrice());
            preStm.setInt(8, dto.getQuantity());
            preStm.setInt(9, dto.getStatus());
            preStm.setInt(10, dto.getId());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public boolean createCake(CakeDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert Into tblCake(Name, Image, Description, Category, [Create Date], [Expiration Date], Price, Quantity, Status) "
                    + "values (?,?,?,?,?,?,?,?,?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getName());
            preStm.setString(2, dto.getImage());
            preStm.setString(3, dto.getDescription());
            preStm.setInt(4, dto.getCategory());
            preStm.setString(5, dto.getCreateDate());
            preStm.setString(6, dto.getExpirationDate());
            preStm.setFloat(7, dto.getPrice());
            preStm.setInt(8, dto.getQuantity());
            preStm.setInt(9, 1);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public List<CakeDTO> getCakesBySearch(String nameSearch, float min, float max, String categorySearch, int page) throws Exception {
        List<CakeDTO> list = new ArrayList<>();
        CakeDTO dto = null;
        String name, image, description, categoryName, createDate, expirationDate;
        float price;
        int id, category, quantity;
        try {
            String query = "Select * from ("
                         + "Select ROW_NUMBER() OVER (Order By A.[Create Date] DESC) as rownumber, "
                         + "A.CakeID, A.Name, A.Image, A.Description, A.Category, B.Name as [Category Name], "
                         + "convert(varchar,A.[Create Date],103) as [Create Date], "
                         + "convert(varchar,A.[Expiration Date],103) as [Expiration Date], A.Price, A.Quantity "
                         + "From tblCake A, tblCategory B "
                         + "Where A.Category = B.CategoryID AND A.Status = 1 AND A.Quantity > 0 AND A.[Expiration Date] > GETDATE() AND "
                         + "A.Name Like ? AND A.Price >= ? AND A.Price <= ? AND B.Name LIKE ? "
                         + ") as tblCakePaging "
                         + "Where rownumber > ? AND rownumber <= ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + nameSearch + "%");
            preStm.setFloat(2, min);
            preStm.setFloat(3, max);
            preStm.setString(4, "%" + categorySearch + "%");
            preStm.setInt(5, page * 5 - 5);
            preStm.setInt(6, page * 5);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getInt("CakeID");
                name = rs.getString("Name");
                image = rs.getString("Image");
                description = rs.getString("Description");
                category = rs.getInt("Category");
                createDate = rs.getString("Create Date");
                expirationDate = rs.getString("Expiration Date");
                price = rs.getFloat("Price");
                quantity = rs.getInt("Quantity");
                categoryName = rs.getString("Category Name");
                dto = new CakeDTO(id, name, image, description, category, createDate, expirationDate, price, quantity, 1);
                dto.setCategoryName(categoryName);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }

    public List<CakeDTO> getCakesBySearchAdmin(String nameSearch, float min, float max, String categorySearch, int page) throws Exception {
        List<CakeDTO> list = new ArrayList<>();
        CakeDTO dto = null;
        String name, image, description, categoryName, createDate, expirationDate;
        float price;
        int id, category, quantity, status;
        try {
            String query = "Select * from ("
                         + "Select ROW_NUMBER() OVER (Order By A.[Create Date] DESC) as rownumber, "
                         + "A.CakeID, A.Name, A.Image, A.Description, A.Category, B.Name as [Category Name], "
                         + "convert(varchar,A.[Create Date],103) as [Create Date], "
                         + "convert(varchar,A.[Expiration Date],103) as [Expiration Date], A.Price, A.Quantity, A.Status "
                         + "From tblCake A, tblCategory B "
                         + "Where A.Category = B.CategoryID AND "
                         + "A.Name Like ? AND A.Price >= ? AND A.Price <= ? AND B.Name LIKE ? "
                         + ") as tblCakePaging "
                         + "Where rownumber > ? AND rownumber <= ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + nameSearch + "%");
            preStm.setFloat(2, min);
            preStm.setFloat(3, max);
            preStm.setString(4, "%" + categorySearch + "%");
            preStm.setInt(5, page * 5 - 5);
            preStm.setInt(6, page * 5);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getInt("CakeID");
                name = rs.getString("Name");
                image = rs.getString("Image");
                description = rs.getString("Description");
                category = rs.getInt("Category");
                createDate = rs.getString("Create Date");
                expirationDate = rs.getString("Expiration Date");
                price = rs.getFloat("Price");
                quantity = rs.getInt("Quantity");
                status = rs.getInt("Status");
                categoryName = rs.getString("Category Name");
                dto = new CakeDTO(id, name, image, description, category, createDate, expirationDate, price, quantity, status);
                dto.setCategoryName(categoryName);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
    
    public boolean updateCakeQuantity(int id, int quantity) throws Exception {
        boolean check = false;
        try {
            String query = "Update tblCake set Quantity = ? where CakeID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, quantity);
            preStm.setInt(2, id);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public int countPage(String nameSearch, float min, float max, String categorySearch) throws Exception {
        int count = 0;
        try {
            String query = "Select Count(A.CakeID) as Count "
                         + "From tblCake A, tblCategory B "
                         + "Where A.Category = B.CategoryID AND A.Status = 1 AND A.Quantity > 0 AND A.[Expiration Date] > GETDATE() AND "
                         + "A.Name Like ? AND A.Price > ? AND A.Price < ? AND B.Name LIKE ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + nameSearch + "%");
            preStm.setFloat(2, min);
            preStm.setFloat(3, max);
            preStm.setString(4, "%" + categorySearch + "%");
            rs = preStm.executeQuery();
            if (rs.next())
                count = rs.getInt("Count");
        } finally {
            closeConnection();
        }
        return count;
    }
    
    public int countPageAdmin(String nameSearch, float min, float max, String categorySearch) throws Exception {
        int count = 0;
        try {
            String query = "Select Count(A.CakeID) as Count "
                         + "From tblCake A, tblCategory B "
                         + "Where A.Category = B.CategoryID AND "
                         + "A.Name Like ? AND A.Price > ? AND A.Price < ? AND B.Name LIKE ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, "%" + nameSearch + "%");
            preStm.setFloat(2, min);
            preStm.setFloat(3, max);
            preStm.setString(4, "%" + categorySearch + "%");
            rs = preStm.executeQuery();
            if (rs.next())
                count = rs.getInt("Count");
        } finally {
            closeConnection();
        }
        return count;
    }
    
    public int getQuantityByCakeId(int id) throws Exception {
        int quantity = -1;
        try {
            String query = "Select Quantity from tblCake "
                         + "Where CakeID = ?";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setInt(1, id);
            rs = preStm.executeQuery();
            if (rs.next())
                quantity = rs.getInt("Quantity");
        } finally {
            closeConnection();
        }
        return quantity;
    }
}
