/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Manager;

import DAO.CVDAO;
import DAO.RequestDAO;
import DAO.SlotDAO;
import Model.Request;
import Model.Slot;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ActiveMenteeRequestServlet", urlPatterns = {"/activementeerequest"})
public class StatusStudyingRequestServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ActiveMenteeRequestServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ActiveMenteeRequestServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String requestId_raw = request.getParameter("id");
        int requestId;
        try {
            requestId = Integer.parseInt(requestId_raw);
            CVDAO cvd = new CVDAO();
            RequestDAO requestDAO = new RequestDAO();
            Request rq = cvd.getRequestbyRequestId(requestId);
            if (rq.getStatus().equalsIgnoreCase("Processing")) {
                //change status slot of mentor when mentee complete paid
                if (requestDAO.setSlotStatusbyRequestSlotItemUnavailable(requestId)) {
                    System.out.println("change sucess");

                    //System.out.println("pending");
                    cvd.setStatusStudyingRequestId(requestId);
                    //add attendance!!
                    Request newRq = cvd.getRequestbyRequestId(requestId);
                    CreateScheduleServlet.setScheduelForRequest(newRq);
                    response.sendRedirect("paymentmanagercate?id=" + requestId+"&mess=Status changed to Studying and create schedule successfully.");
                } else {
                    response.sendRedirect("paymentmanagercate?id=" + requestId+"&error=Failed to change status.");
                }
            } else if (rq.getStatus().equalsIgnoreCase("Studying")) {
                if (requestDAO.setSlotStatusbyRequestSlotItemAvailable(requestId)) {
                    
                    //System.out.println("studying");
                    //cvd.setStatusProcessingRequestId(requestId);
                    response.sendRedirect("paymentmanagercate?id=" + requestId+"&error=Cannot change status from 'Studying' to 'Processing'.");
                } else {
                    response.sendRedirect("paymentmanagercate?id=" + requestId+"&error=Failed to change status.");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
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