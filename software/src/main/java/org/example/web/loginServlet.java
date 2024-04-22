package org.example.web;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.UserMapper;
import org.example.obj.User;
import org.example.util.sqlSessionFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //receive the username and passcode
        String email=req.getParameter("email");
        String password=req.getParameter("password");

        SqlSessionFactory sqlSessionFactory= sqlSessionFactoryUtils.getSqlSessionFactory();

        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);

        User user=userMapper.select(email,password);


        sqlSession.close();

        if(user!=null){
            HttpSession session =req.getSession();
            session.setAttribute("name", user.name);
            session.setAttribute("email", email);

            Cookie c_username=new Cookie("name", user.name);
            Cookie c_email=new Cookie("email", email);
            Cookie c_password=new Cookie("password",password);
            c_username.setMaxAge(60*60*24*7);
            c_password.setMaxAge(60*60*24*7);
            resp.addCookie(c_username);
            resp.addCookie(c_password);
            resp.addCookie(c_email);

            resp.sendRedirect("select.html");

        }
        else{
            resp.sendRedirect("index.html");
        }

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}