/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import bl.StudentBL;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jan
 */
@WebServlet(name = "StudentAdmin", urlPatterns = {"/StudentAdmin"})
public class StudentAdmin extends HttpServlet {

    private StudentBL studentBL = new StudentBL();
    private boolean pressed = false;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String path = this.getServletContext().getRealPath("/res/students.csv");
        try {
            studentBL.loadData(new File(path));
            this.getServletContext().setAttribute("allAttendingClasses", studentBL.getAllAttendingClasses());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
        request.getRequestDispatcher("/jsp/StudentDisplay.jsp").forward(request, response);
        response.setContentType("text/html;charset=UTF-8");

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
        request.getSession().setAttribute("filteredStudents", studentBL.getStudentsFromClass(""));
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
        String attendingClass = request.getParameter("attendingClasses");
        if (request.getParameter("display") != null) {
            pressed = true;
            System.out.println(attendingClass);
            request.getSession().setAttribute("filteredStudents", studentBL.getStudentsFromClass(attendingClass));
        } else {
            String[] credentials = request.getParameter("studentGroup").split(";");
            if (credentials != null) {
                studentBL.removeStudent(credentials);
            }
            if (pressed) {
                request.getSession().setAttribute("filteredStudents", studentBL.getStudentsFromClass(attendingClass));
            } else {
                request.getSession().setAttribute("filteredStudents", studentBL.getStudentsFromClass(""));
            }
        }
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
