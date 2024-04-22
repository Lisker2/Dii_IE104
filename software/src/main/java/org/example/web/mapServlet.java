package org.example.web;

import com.google.gson.Gson;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeMapper;
import org.example.mapper.TspMapper;
import org.example.obj.Arrange;
import org.example.obj.Event;
import org.example.obj.Tsp;
import org.example.obj.User;
import org.example.util.sqlSessionFactoryUtils;
import org.example.util.xmlReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/mapServlet")
public class mapServlet extends HttpServlet{
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

        TspMapper arrangeMapper=sqlSession.getMapper(TspMapper.class);

        Tsp[] tsp=arrangeMapper.select(email);
        int tsp_length = tsp.length;

        if(tsp[0].solution_path!=null){

            try (BufferedReader reader = new BufferedReader(new FileReader(tsp[tsp_length - 1].solution_path))) {
                String line;
                // Skip the cost
                double cost = Double.parseDouble(reader.readLine().split(" ")[0]);

                // Read vertexLocations
                line = reader.readLine();
                String[] vertexLocationTokens = line.split(" ");
                int[] readVertexLocations = new int[vertexLocationTokens.length];
                for (int i = 0; i < vertexLocationTokens.length; i++) {
                    readVertexLocations[i] = Integer.parseInt(vertexLocationTokens[i]);
                }

                // Read adjacencyMatrix
                List<double[]> readAdjacencyMatrix = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] rowTokens = line.split(" ");
                    double[] rowValues = new double[rowTokens.length];
                    for (int i = 0; i < rowTokens.length; i++) {
                        rowValues[i] = Double.parseDouble(rowTokens[i]);
                    }
                    readAdjacencyMatrix.add(rowValues);
                }

                List<double[]> rearrangedAdjacencyMatrix = new ArrayList<>();

                for (int vertexIndex : readVertexLocations) {
                    rearrangedAdjacencyMatrix.add(readAdjacencyMatrix.get(vertexIndex));
                }
                MatrixWithCost matrixWithCost = new MatrixWithCost(rearrangedAdjacencyMatrix, cost);


                Gson gson = new Gson();
                String jsonRearrangedMatrix = gson.toJson(matrixWithCost);

                response.setContentType("application/json");
                response.getWriter().write(jsonRearrangedMatrix);

                /*
                response.setContentType("application/json");
                Gson gson = new Gson();
                String jsonRearrangedMatrix = gson.toJson(rearrangedAdjacencyMatrix);
                //System.out.println(jsonRearrangedMatrix);
                PrintWriter out = response.getWriter();
                out.write(jsonRearrangedMatrix);*/

            }
        }
    }
}
class MatrixWithCost {
    private List<double[]> adjacencyMatrix;
    private double cost;

    public MatrixWithCost(List<double[]> adjacencyMatrix, double cost) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.cost = cost;
    }

}