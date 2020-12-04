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
import org.apache.log4j.Logger;

/**
 *
 * @author Long
 */
public class UpdateCartController extends HttpServlet {

    private static final String SUCCESS = "detailCart.jsp";
    private static final String EMPTY_CART = "SearchCakeController";
    private static final Logger LOGGER = Logger.getLogger(UpdateCartController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = SUCCESS;
        try {
            int id = Integer.parseInt(request.getParameter("txtId"));
            int cartQty = Integer.parseInt(request.getParameter("txtCartQty"));
            int quantity = Integer.parseInt(request.getParameter("txtQuantity"));
            boolean valid = true;
            if (cartQty > quantity) {
                valid = false;
                request.setAttribute("NOTICE", "Amount exceed maximum quantity of cake.");
            }
            if (valid) {
                HttpSession session = request.getSession();
                CakeCart cart = (CakeCart) session.getAttribute("CART");
                if (cartQty == 0) {
                    cart.removeFromCart(id);
                } else {
                    CakeDAO cdao = new CakeDAO();
                    int currQty = cdao.getQuantityByCakeId(id);
                    if (cart.getCart().get(id).getQuantity() != currQty) {
                        cart.updateQuantity(id, currQty);
                        request.setAttribute("NOTICE", "Cake Quantity is updated. Try again.");
                    } else {
                        cart.updateCart(id, cartQty);
                    }
                }

                if (cart.getCart().isEmpty()) {
                    session.removeAttribute("CART");
                    url = EMPTY_CART;
                } else {
                    session.setAttribute("CART", cart);
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at UpdateCartController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
