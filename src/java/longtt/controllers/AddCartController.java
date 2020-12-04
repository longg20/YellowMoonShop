/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import longtt.daos.CakeDAO;
import longtt.dtos.CakeCart;
import longtt.dtos.CakeDTO;
import longtt.dtos.UserDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author Long
 */
public class AddCartController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddCartController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String idStr = request.getParameter("txtId");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                CakeDAO cdao = new CakeDAO();
                CakeDTO cdto = cdao.getCakeById(id);
                cdto.setCartQty(1);

                HttpSession session = request.getSession();
                UserDTO udto = (UserDTO) session.getAttribute("USER");
                CakeCart cart = (CakeCart) session.getAttribute("CART");

                if (cart == null) {
                    if (udto != null) { //logged in
                        if (udto.getRole() == 1) {  //user
                            cart = new CakeCart(udto.getId());
                        }
                    } else { //guest
                        cart = new CakeCart();
                    }
                }

                if (cart != null) {
                    boolean valid = true;
                    if (cart.getCart().get(id) != null) {
                        if (cart.getCart().get(id).getCartQty() + 1 > cdto.getQuantity()) {
                            valid = false;
                            request.setAttribute("NOTICE", "Amount exceed maximum quantity of cake.");
                        }
                    }
                    if (valid) {
                        cart.addToCart(cdto);
                        cart.setTotal(cart.getTotal());
                        session.setAttribute("CART", cart);
                    }
                }
            } else {
                request.setAttribute("NOTICE", "CakeID not found");
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at AddCartController: " + e.getMessage());
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
