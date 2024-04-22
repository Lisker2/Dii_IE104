package org.example.web;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.TspHisMapper;
import org.example.mapper.TspMapper;
import org.example.obj.Tsp;
import org.example.util.AbstractClasses.HyperHeuristic;
import org.example.util.Examples.ExampleHyperHeuristic1;
import org.example.util.HHs.adaphh.acceptance.AcceptanceCriterionType;
import org.example.util.HHs.adaphh.hyperheuristic.GIHH;
import org.example.util.HHs.adaphh.selection.SelectionMethodType;
import org.example.util.HHs.haea.HaeaHH;
import org.example.util.HHs.leangihh.LeanGIHH;
import org.example.util.createNearestCities;
import org.example.util.sqlSessionFactoryUtils;
import org.example.util.travelingSalesmanProblem.TSP;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.Part;


@WebServlet("/tsp_hhServlet")
@MultipartConfig
public class tsp_hhServlet extends HttpServlet {

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

        Tsp[] tsp=tspMapper.select(email);


        // write the solutions
        ServletContext servletContext = getServletContext();
        String contextPath = servletContext.getRealPath(File.separator);

        File directory = new File(servletContext.getRealPath(File.separator)+ "data/tsp/" + email + "/");
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Failed to create directory");
            }
        }

        String instance = request.getParameter("instance");
        String budget = request.getParameter("time");
        String hh_method = request.getParameter("hh");
        Part part = request.getPart("file");
        String savedName = part.getSubmittedFileName();
        int dotIndex = savedName.lastIndexOf(".");
        if (dotIndex != -1) {
            savedName = savedName.substring(0, dotIndex);
        }
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = sdf.format(new Date(currentTimestamp.getTime()));

        System.out.println(instance+budget+hh_method+savedName);
        long time_ms = Long.parseLong(budget);


        String directory_path = servletContext.getRealPath(File.separator)+ "data/tsp/" + email + "/";
        String instance_path = "";
        String solution_path = "";
        if(!instance.equals("custom")){
            instance_path = contextPath + "/data/tsp/" + instance;
            solution_path = directory_path +  "solution_" + instance + "_" + hh_method + "_" + budget + "_" + formattedDateTime + ".txt";
        }else{
            part.write(directory_path + savedName + ".tsp");
            createNearestCities nearestCities = new createNearestCities(directory_path, savedName);
            nearestCities.create();
            instance_path = directory_path + savedName;
            solution_path = directory_path +  "solution_" + savedName + "_" + hh_method + "_" + budget + "_" + formattedDateTime + ".txt";

        }

        tspMapper.update_path(email,instance_path,solution_path,formattedDateTime);
        tspHisMapper.add_path(email,instance_path,solution_path,formattedDateTime);

        sqlSession.commit();

        System.out.println(instance_path);
        TSP problem = new TSP(1234,instance_path);

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

        int[] vertexLocations = problem.bestSoFar.permutation;
        double cost = problem.bestSoFar.Cost;
        double[][] adjacencyMatrix = problem.instance.coordinates;
        try (PrintWriter writer = new PrintWriter(new FileWriter(solution_path))) {

            // Write cost
            writer.print(cost);
            writer.println();

            // Write vertexLocations
            for (int location : vertexLocations) {
                writer.print(location + " ");
            }
            writer.println();

            // Write adjacencyMatrix
            for (double[] row : adjacencyMatrix) {
                for (double value : row) {
                    writer.print(value + " ");
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.sendRedirect("tsp_main.html");
        sqlSession.close();
    }
}
