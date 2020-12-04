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
import longtt.daos.CategoryDAO;
import longtt.dtos.UserDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author Long
 */
public class SearchCakeController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchCakeController.class);
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String name = request.getParameter("txtNameSearch");
            String moneyMinStr = request.getParameter("txtMoneyMin");
            String moneyMaxStr = request.getParameter("txtMoneyMax");
            String categoryStr = request.getParameter("txtCategorySearch");
            String pageStr = request.getParameter("txtPage");
            String movePage = request.getParameter("movePage");
            float moneyMin;
            float moneyMax;
            int page;

            if (name == null || name.isEmpty()) {
                name = "";
            }

            if (moneyMinStr == null || moneyMinStr.isEmpty()) {
                moneyMin = 0;
            } else {
                moneyMin = Float.parseFloat(moneyMinStr);
            }

            if (moneyMaxStr == null || moneyMaxStr.isEmpty()) {
                moneyMax = 100000000;
            } else {
                moneyMax = Float.parseFloat(moneyMaxStr);
            }

            if (categoryStr == null || categoryStr.isEmpty()) {
                categoryStr = "";
            }

            if (pageStr == null) {
                page = 1;
            } else {
                page = Integer.parseInt(pageStr);
            }

            CakeDAO cdao = new CakeDAO();
            int cakeCount = 0;
            HttpSession session = request.getSession(false);
            if (session != null) {
                UserDTO udto = (UserDTO) session.getAttribute("USER");
                if (udto == null || udto.getRole() == 1) {   //guest with session cart || user
                    cakeCount = cdao.countPage(name, moneyMin, moneyMax, categoryStr);
                } else if (udto.getRole() == 2) {   //admin
                    cakeCount = cdao.countPageAdmin(name, moneyMin, moneyMax, categoryStr);
                }
            } else {    //guest
                cakeCount = cdao.countPage(name, moneyMin, moneyMax, categoryStr);
            }
            int pageCount = (int) Math.ceil(cakeCount / (double) 5);

            if (movePage == null); else if (movePage.equals("next")) {
                if (page < pageCount) {
                    page = page + 1;
                }
            } else if (movePage.equals("prev")) {
                if (page > 1) {
                    page = page - 1;
                }
            } else if (movePage.equals("first")) {
                page = 1;
            } else if (movePage.equals("last")) {
                page = pageCount;
            }

            CategoryDAO ctdao = new CategoryDAO();
            if (session != null) {
                UserDTO udto = (UserDTO) session.getAttribute("USER");
                if (udto == null || udto.getRole() == 1) { //guest with session cart || user
                    request.setAttribute("INFO", cdao.getCakesBySearch(name, moneyMin, moneyMax, categoryStr, page));
                } else if (udto.getRole() == 2) {  //admin
                    request.setAttribute("INFO", cdao.getCakesBySearchAdmin(name, moneyMin, moneyMax, categoryStr, page));
                }
            } else {    //guest
                request.setAttribute("INFO", cdao.getCakesBySearch(name, moneyMin, moneyMax, categoryStr, page));
            }
            request.setAttribute("PAGE_CURRENT", page);
            request.setAttribute("PAGE_MAX", pageCount);
            request.setAttribute("CATEGORY", ctdao.getAllCategories());

            if (moneyMax < moneyMin) {
                request.setAttribute("NOTICE", "Min range can't exceed Max range.");
            }

        } catch (Exception e) {
            LOGGER.error("ERROR at SearchCakeController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher("home.jsp").forward(request, response);
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
