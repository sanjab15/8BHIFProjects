<%-- 
    Document   : StudentDisplay
    Created on : 13.11.2018, 08:30:03
    Author     : Jan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="beans.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css%27%3E'>
        <title>JSP Page</title>
        <style>
            table{
                font-family: Open Sans, Times, serif;
            }
            td{
                border: 1px solid black;
            }
        </style>
    </head>
    <body>
        <h1>Student Administration</h1>
        <form align="center" action="StudentAdmin" method="POST">
            <%
                String selectedClass = request.getParameter("attendingClasses") != null ? request.getParameter("attendingClasses") : "";
            %>
            <select name="attendingClasses">
                    <c:forEach var="attendingClass" items="${allAttendingClasses}" >
                        <option>${attendingClass}</option> 
                    </c:forEach>
            </select>
            <input type="submit" value="Display" name="display" />   
            <input type="submit" value="remove" name="remove" />
            <table align="center"> 
                <%
                    List<Student> students = new ArrayList();
                        if((ArrayList) session.getAttribute("filteredStudents") != null)
                        {
                            students = (ArrayList) session.getAttribute("filteredStudents");
                        
                        int i = 0;
                        for (Student student : students) {
                %>
                <c:forEach var="Student" items="filteredStudents">
                    <tr>
                        <td><input type="radio" name="studentGroup" value="${Student.getFullname() + ";" + student.attendingClass}" /></td>
                        <td>${Student.getFullname()}</td>
                        <td>${Student.dateOfBirth}</td>
                    </tr>
                </c:forEach>   

            </table>
            
        </form>
    </body>
</html>
