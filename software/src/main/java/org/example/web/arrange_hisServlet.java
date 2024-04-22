package org.example.web;

import com.google.gson.Gson;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.ArrangeHisMapper;
import org.example.obj.Arrange;
import org.example.util.TableRowData;
import org.example.util.sqlSessionFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/arrange_hisServlet")
public class arrange_hisServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        SqlSessionFactory sqlSessionFactory= sqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ArrangeHisMapper arrangeHisMapper=sqlSession.getMapper(ArrangeHisMapper.class);
        Arrange[] arranges = arrangeHisMapper.select(email);
        List<TableRowData> all_history = new ArrayList<>();
        int index = 1;
        for (Arrange t : arranges) {
            String[] parts = t.solution_path.split("/");
            String last_part = parts[parts.length - 1];
            String[] all_fields = last_part.substring(0, last_part.lastIndexOf(".xml")).split("_");
            int all_fields_length = all_fields.length;
            String rowDataName = "";
            for (int i = 1; i < all_fields_length - 3; i++) {
                rowDataName += all_fields[i];
                rowDataName += " ";
            }
            TableRowData rowData = new TableRowData(index,rowDataName,all_fields[all_fields_length - 3],all_fields[all_fields_length - 2], all_fields[all_fields_length - 1]);
            all_history.add(rowData);
            index ++;
        }

        Gson gson = new Gson();
        String jsonData = gson.toJson(all_history);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);

    }
}

