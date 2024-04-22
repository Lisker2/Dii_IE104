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
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/shiftTypesServlet")
public class shiftTypesServlet extends HttpServlet{
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
            Map<String, Map<String, String>> shiftTypes = r.getinstance().get(0);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            //System.out.println(convertToJson(shiftTypes));
            out.print(convertToJson(shiftTypes));
            out.flush();
        }

    }
    private String escapeJsonValue(String value) {
        // Escape special characters manually
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    private String convertToJson(Map<String, Map<String, String>> shiftTypes) {
        StringBuilder json = new StringBuilder("{");

        for (Map.Entry<String, Map<String, String>> entry : shiftTypes.entrySet()) {
            if (json.length() > 1) {
                json.append(",");
            }
            if(!entry.getKey().equals("")){
                json.append("\"").append(entry.getKey()).append("\": {");

                Map<String, String> rowData = entry.getValue();
                int rowCount = rowData.size();

                for (Map.Entry<String, String> rowEntry : rowData.entrySet()) {
                    String escapedValue = escapeJsonValue(rowEntry.getValue()); // Escape the value
                    json.append("\"").append(rowEntry.getKey()).append("\": \"").append(escapedValue).append("\"");

                    rowCount--;
                    if (rowCount > 0) {
                        json.append(",");
                    }
                }
                json.append("}");
            }
        }

        json.append("}");

        return json.toString();
    }

}
