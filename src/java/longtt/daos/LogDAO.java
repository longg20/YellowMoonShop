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
import longtt.dtos.LogDTO;

/**
 *
 * @author Long
 */
public class LogDAO implements Serializable {

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
    
    public boolean addLog(LogDTO dto) throws Exception {
        boolean check = false;
        try {
            String query = "Insert into tblLog(UserID, CakeID, Date, Status) values (?,?,GETDATE(),?)";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            preStm.setString(1, dto.getUserId());
            preStm.setInt(2, dto.getCakeId());
            preStm.setInt(3, 1);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }
    
    public List<LogDTO> getAllLogs() throws Exception {
        List<LogDTO> list = new ArrayList<>();
        LogDTO dto = null;
        String userId, date, userName, cakeName;
        int id, cakeId, status;
        boolean check = false;
        try {
            String query = "Select A.LogID, A.UserID, A.CakeID, convert(varchar,A.[Date],0) as [Date], A.Status, B.Name as userName, C.Name as cakeName "
                         + "from tblLog A, tblUser B, tblCake C "
                         + "Where A.Status = 1 AND A.UserID = B.UserID AND A.CakeID = C.CakeID "
                         + "Order By A.Date DESC";
            conn = getMyConnection();
            preStm = conn.prepareStatement(query);
            rs = preStm.executeQuery();
            while (rs.next()) {
                id = rs.getInt("LogID");
                userId = rs.getString("UserID");
                cakeId = rs.getInt("CakeID");
                date = rs.getString("Date");
                status = rs.getInt("Status");
                userName = rs.getString("userName");
                cakeName = rs.getString("cakeName");
                dto = new LogDTO(id, userId, cakeId, date, status);
                dto.setUserName(userName);
                dto.setCakeName(cakeName);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
}
