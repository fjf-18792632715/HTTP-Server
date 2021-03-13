package com.fjf.webapps.dictionary;

import com.fjf.standard.ServletException;
import com.fjf.standard.http.HttpServlet;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpServletResponse;
import com.fjf.standard.http.HttpSession;

import java.io.IOException;

public class LoginActionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username.equals("fjf") && password.equals("123456")) {
            User user = new User(username, password);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            System.out.println(session.toString());
            resp.sendRedirect("profile-action");
        } else {
            resp.sendRedirect("login.html");
        }
    }
}
