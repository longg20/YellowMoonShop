/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import longtt.daos.CakeDAO;
import longtt.dtos.CakeDTO;

/**
 *
 * @author Long
 */
public class CreateController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CreateController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
            if (!isMultiPart) {
            } else {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List items = null;
                try {
                    items = upload.parseRequest(request);
                } catch (FileUploadException e) {
                    LOGGER.error("Error at CreateController: " + e.getMessage());
                }
                Iterator iterator = items.iterator();
                Hashtable param = new Hashtable();
                String fileName = null;
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (item.isFormField()) {
                        param.put(item.getFieldName(), item.getString());
                    } else {
                        String itemName = item.getName();
                        fileName = itemName.substring(itemName.lastIndexOf("\\") + 1);
                        String realPath = getServletContext().getRealPath("/") + "images\\" + fileName;
                        File saveFile = new File(realPath);
                        File destFile = new File(realPath);
                        if (destFile.exists()) {
                            destFile.delete();
                            item.write(saveFile);
                        } else {
                            item.write(saveFile);
                        }
                    }
                }
                String name = (String) param.get("txtName");
                String image = (String) param.get("txtImage");
                String description = (String) param.get("txtDescription");
                float price = Float.parseFloat((String) param.get("txtPrice").toString());
                int category = Integer.parseInt((String) param.get("txtCategory").toString());
                int quantity = Integer.parseInt((String) param.get("txtQuantity").toString());
                String createDate = (String) param.get("txtCreateDate");
                String expirationDate = (String) param.get("txtExpirationDate");
                
                CakeDAO cdao = new CakeDAO();
                CakeDTO cdto = new CakeDTO(name, fileName, description, category, createDate, expirationDate, price, quantity);
                if (cdao.createCake(cdto)) {
                    request.setAttribute("NOTICE", "Add new Cake Successfully.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at CreateController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher("SearchCakeController").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
