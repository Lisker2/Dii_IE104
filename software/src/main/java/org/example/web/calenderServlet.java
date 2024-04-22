package org.example.web;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeMapper;
import org.example.mapper.UserMapper;
import org.example.obj.Arrange;
import org.example.obj.Event;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.*;

import com.google.gson.Gson;
import org.example.obj.User;
import org.example.util.sqlSessionFactoryUtils;
import org.example.util.xmlReader;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calenderServlet")
public class calenderServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        SqlSessionFactory sqlSessionFactory= sqlSessionFactoryUtils.getSqlSessionFactory();

        SqlSession sqlSession = sqlSessionFactory.openSession();

        ArrangeMapper arrangeMapper=sqlSession.getMapper(ArrangeMapper.class);

        Arrange arrange=arrangeMapper.select(email);

        if(arrange.solution_path!=null){

            xmlReader r = new xmlReader(arrange.solution_path,arrange.instance_path);
            List<Event> events = r.getsolution();
            Gson gson = new Gson();
            String eventDataJson = gson.toJson(events);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(eventDataJson);
        }

    }


}
