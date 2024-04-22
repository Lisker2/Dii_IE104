package org.example.web.filter;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class loginFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String[] urls={"/index.html","/assets/","/data/","/loginServlet","/registerServlet","/sign-up.html"};

        HttpServletRequest req=(HttpServletRequest) request;

        String url=req.getRequestURI().toString();
        for(String u:urls){
            if(url.contains(u)){
                chain.doFilter(request,response);
                return;
            }
        }


        HttpSession session=req.getSession();
        Object user=session.getAttribute("name");
        if(user!=null){
            chain.doFilter(request,response);
        }
        else{
            req.getRequestDispatcher("index.html").forward(req,response);
        }


    }
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
