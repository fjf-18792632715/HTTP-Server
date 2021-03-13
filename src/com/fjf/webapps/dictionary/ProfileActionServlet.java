package com.fjf.webapps.dictionary;

import com.fjf.standard.ServletException;
import com.fjf.standard.http.HttpServlet;
import com.fjf.standard.http.HttpServletRequest;
import com.fjf.standard.http.HttpServletResponse;
import com.fjf.standard.http.HttpSession;

import java.io.IOException;

public class ProfileActionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.html");
        } else {
            resp.setContentType("text/plain");
            resp.getWriter().println(user.toString());
        }
    }
}
