package org.example.web;

import org.example.obj.Tsp;
import org.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.TspHisMapper;
import org.example.mapper.TspMapper;
import org.example.util.sqlSessionFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/tspactivateServlet")
public class tspactivateServlet extends HttpServlet {
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

        TspMapper tspMapper=sqlSession.getMapper(TspMapper.class);
        TspHisMapper tspHisMapper=sqlSession.getMapper(TspHisMapper.class);
        String requestData = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        JSONObject json = new JSONObject(requestData);
        String time = json.getString("time");
        Tsp tsp = tspHisMapper.select_by_time(time);
        tspMapper.update_path(email,tsp.instance_path,tsp.solution_path,time);
        sqlSession.commit();
        response.sendRedirect("tsp_main.html");
    }
}
