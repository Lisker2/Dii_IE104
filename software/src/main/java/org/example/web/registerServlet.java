package org.example.web;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeMapper;
import org.example.mapper.TspMapper;
import org.example.obj.Arrange;
import org.example.obj.User;

import org.example.mapper.UserMapper;

import org.example.util.sqlSessionFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();

        String password=req.getParameter("password");
        String email=req.getParameter("email");
        String name=req.getParameter("name");

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        SqlSessionFactory sqlSessionFactory= sqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        ArrangeMapper arrangeMapper = sqlSession.getMapper(ArrangeMapper.class);
        TspMapper tspMapper = sqlSession.getMapper(TspMapper.class);

        User u= userMapper.selectByEmail(email);

        if(u == null){
            userMapper.add(user);
            arrangeMapper.add(email);
            tspMapper.add(email);
            sqlSession.commit();
            resp.sendRedirect("/index.html");
            sqlSession.close();

        }else{
            resp.sendRedirect("/sign-up.html");
            sqlSession.close();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
