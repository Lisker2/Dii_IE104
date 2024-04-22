package org.example.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeHisMapper;
import org.example.mapper.ArrangeMapper;
import org.example.obj.Arrange;
import org.example.util.ASAP.NRP.Core.Tools.RosterPrinter;
import org.example.util.AbstractClasses.HyperHeuristic;
import org.example.util.Examples.ExampleHyperHeuristic1;
import org.example.util.HHs.adaphh.acceptance.AcceptanceCriterionType;
import org.example.util.HHs.adaphh.hyperheuristic.GIHH;
import org.example.util.HHs.adaphh.selection.SelectionMethodType;

import org.example.util.HHs.haea.HaeaHH;
import org.example.util.HHs.leangihh.LeanGIHH;
import org.example.util.PersonnelScheduling.PersonnelScheduling;
import org.example.util.sqlSessionFactoryUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/hhServlet")
public class hhServlet extends HttpServlet{
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get the path
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        SqlSessionFactory sqlSessionFactory= sqlSessionFactoryUtils.getSqlSessionFactory();

        SqlSession sqlSession = sqlSessionFactory.openSession();

        ArrangeMapper arrangeMapper=sqlSession.getMapper(ArrangeMapper.class);
        ArrangeHisMapper arrangeHisMapper = sqlSession.getMapper(ArrangeHisMapper.class);

        Arrange arrange=arrangeMapper.select(email);


        // write the solutions
        ServletContext servletContext = getServletContext();
        String contextPath = servletContext.getRealPath(File.separator);

        File directory = new File(servletContext.getRealPath(File.separator)+ "/data/" + email + "/");
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Failed to create directory");
            }
        }

        String instance = request.getParameter("instance");
        String time = request.getParameter("time");
        String hh_method = request.getParameter("hh");
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = sdf.format(new Date(currentTimestamp.getTime()));
        System.out.println(instance+time+hh_method);
        long time_ms = Long.parseLong(time);

        String instance_path = contextPath + "/data/" + instance + ".xml";
        String solution_path = contextPath + "data/" + email + "/" +  "solution_" + instance + "_" + hh_method + "_" + time +  "_" + formattedDateTime + ".xml";

        arrangeMapper.update_path(email,instance_path,solution_path,formattedDateTime);
        arrangeHisMapper.add_path(email,instance_path,solution_path,formattedDateTime);
        sqlSession.commit();

        System.out.println(instance_path);
        PersonnelScheduling problem = new PersonnelScheduling(1234,instance_path);

        HyperHeuristic hyper_heuristic_object = new ExampleHyperHeuristic1(5678);
        switch (hh_method) {
            case "AdapHH":
                hyper_heuristic_object = new GIHH(5678, problem.getNumberOfHeuristics(), time_ms, "",
                        SelectionMethodType.AdaptiveLimitedLAassistedDHSMentorSTD,
                        AcceptanceCriterionType.AdaptiveIterationLimitedListBasedTA);
                break;
            case "HaeaHH":
                hyper_heuristic_object = new HaeaHH(5678);
                break;
            case "LeanGIHH":
                hyper_heuristic_object = new LeanGIHH(5678);
                break;
        }

        problem.loadInstance(0);
        hyper_heuristic_object.setTimeLimit(time_ms * 1000);
        hyper_heuristic_object.loadProblemDomain(problem);
        hyper_heuristic_object.run();
        RosterPrinter.PrintRosterAsXML(problem.solutions[0], solution_path);
        response.sendRedirect("main.jsp");
        sqlSession.close();
    }

}
