package org.example.web;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeHisMapper;
import org.example.mapper.ArrangeMapper;
import org.example.mapper.TspHisMapper;
import org.example.mapper.TspMapper;
import org.example.obj.Arrange;
import org.example.obj.Tsp;
import org.example.util.sqlSessionFactoryUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;


@WebServlet("/arrangedeleteServlet")
public class arrangedeleteServlet extends HttpServlet {
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
        ArrangeHisMapper arrangeHisMapper=sqlSession.getMapper(ArrangeHisMapper.class);
        String requestData = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        JSONObject json = new JSONObject(requestData);
        String time = json.getString("time");
        Arrange arrange = arrangeHisMapper.select_by_time(time);
        arrangeHisMapper.delete(email,time);
        sqlSession.commit();

        File fileToDelete = new File(arrange.solution_path);
        if (fileToDelete.exists()) {
            boolean deletionStatus = fileToDelete.delete();
            if (deletionStatus) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
