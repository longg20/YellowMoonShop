/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longtt.controllers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import longtt.daos.CakeDAO;
import longtt.daos.OrderDAO;
import longtt.daos.OrderDetailDAO;
import longtt.dtos.CakeCart;
import longtt.dtos.CakeDTO;
import longtt.dtos.OrderDTO;
import longtt.dtos.UserDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author Long
 */
public class ConfirmCartController extends HttpServlet {

    private static final String HOME = "SearchCakeController";
    private static final String INVALID = "detailCart.jsp";
    private static final Logger LOGGER = Logger.getLogger(ConfirmCartController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = HOME;
        try {
            String paymentMethod = request.getParameter("txtPaymentMethod");
            HttpSession session = request.getSession(false);
            if (session != null) {
                CakeCart cart = (CakeCart) session.getAttribute("CART");
                if (cart != null) {
                    UserDTO udto = (UserDTO) session.getAttribute("USER");
                    OrderDAO odao = new OrderDAO();
                    int id = odao.getOrderId();

                    boolean paymentStatus = false;
                    if (paymentMethod.equals("Cash upon Delivery")) {
                        paymentStatus = false;
                    } else {
                        paymentStatus = true;
                    }

                    CakeDAO cdao = new CakeDAO();
                    int currQty = -1;
                    boolean valid = true;

                    Iterator check = cart.getCart().entrySet().iterator();
                    while (check.hasNext()) {
                        Map.Entry entry = (Map.Entry) check.next();
                        CakeDTO cdto = (CakeDTO) entry.getValue();
                        currQty = cdao.getQuantityByCakeId(cdto.getId());
                        if (cdto.getQuantity() != currQty) {
                            valid = false;
                            url = INVALID;
                            if (currQty == 0) {
                                check.remove();
                            } else {
                                cart.updateQuantity(cdto.getId(), currQty);
                                if (cdto.getCartQty() > currQty) {
                                    cart.updateCart(cdto.getId(), currQty);     //set cartQty to new Quantity
                                }
                            }
                        }
                    }
                    if (valid) {
                        if (udto != null) {    //logged in
                            OrderDTO odto = new OrderDTO(id, udto.getId(), cart.getTotal(), udto.getName(),
                                    udto.getPhone(), udto.getAddress(), paymentMethod, paymentStatus);

                            if (odao.createUserOrder(odto)) {
                                Iterator iterator = cart.getCart().entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iterator.next();
                                    CakeDTO cdto = (CakeDTO) entry.getValue();
                                    OrderDetailDAO ddao = new OrderDetailDAO();
                                    if (ddao.createOrderDetail(id, cdto)) {
                                        cdao.updateCakeQuantity(cdto.getId(), cdto.getQuantity() - cdto.getCartQty());
                                    }
                                }
                                request.setAttribute("NOTICE", "Order is successfully sent. Your Order ID is: " + id);
                                session.removeAttribute("CART");
                            }
                        } else {    //guest
                            String name = request.getParameter("txtName");
                            String phone = request.getParameter("txtPhone");
                            String address = request.getParameter("txtAddress");

                            OrderDTO odto = new OrderDTO(id, cart.getTotal(), name, phone, address,
                                    paymentMethod, paymentStatus);
                            if (odao.createGuestOrder(odto)) {
                                Iterator iterator = cart.getCart().entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iterator.next();
                                    CakeDTO cdto = (CakeDTO) entry.getValue();
                                    OrderDetailDAO ddao = new OrderDetailDAO();
                                    if (ddao.createOrderDetail(id, cdto)) {
                                        cdao.updateCakeQuantity(cdto.getId(), cdto.getQuantity() - cdto.getCartQty());
                                    }
                                }
                                request.setAttribute("NOTICE", "Order is successfully sent.");
                                session.removeAttribute("CART");
                            }
                        }
                    } else {
                        if (cart.getCart().isEmpty()) {
                            url = HOME;
                            request.setAttribute("NOTICE", "All of your Cakes in Cart are Out of Stock");
                            session.removeAttribute("CART");
                        } else {
                            request.setAttribute("NOTICE", "Cake Quantity is updated. Try again.");
                            session.setAttribute("CART", cart);
                        }
                    }
                } else {
                    request.setAttribute("NOTICE", "Cart not found.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR at ConfirmCartController: " + e.getMessage());

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
