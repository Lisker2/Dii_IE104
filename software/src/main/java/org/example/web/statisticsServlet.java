package org.example.web;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.*;

import com.google.gson.Gson;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeMapper;
import org.example.obj.Arrange;
import org.example.obj.Event;
import org.example.util.ASAP.NRP.Core.Tools.RosterPrinter;
import org.example.util.AbstractClasses.HyperHeuristic;
import org.example.util.Examples.ExampleHyperHeuristic1;
import org.example.util.HHs.adaphh.acceptance.AcceptanceCriterionType;
import org.example.util.HHs.adaphh.hyperheuristic.GIHH;
import org.example.util.HHs.adaphh.selection.SelectionMethodType;

import org.example.util.PersonnelScheduling.PersonnelScheduling;
import org.example.util.sqlSessionFactoryUtils;
import org.example.util.xmlReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

@WebServlet("/statisticsServlet")
public class statisticsServlet extends HttpServlet{

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
            int[] statistics = r.statistics();
            /*
            String[] labels = {"Stroke", "Diabetes", "Cirrhosis", "Tuberculosis"};
            int[] data = {15, 25, 18, 42};
            StringBuilder jsonData = new StringBuilder(", \"labels\": [");
            for (int i = 0; i < labels.length; i++) {
                jsonData.append("\"").append(labels[i]).append("\"");
                if (i < labels.length - 1) {
                    jsonData.append(",");
                }
            }
            jsonData.append("], \"data\": [");
            for (int i = 0; i < data.length; i++) {
                jsonData.append(data[i]);
                if (i < data.length - 1) {
                    jsonData.append(",");
                }
            }
            jsonData.append("]}");

             */

            String json = "{\"contract\": " + statistics[0] + ", \"employees\": " + statistics[1] +
                    ", \"shiftTypes\": " + statistics[2] + ", \"totalShift\": " + statistics[3] +"}";
            //System.out.println(json);
            response.setContentType("application/json");
            response.getWriter().write(json);
        }
    }
}
