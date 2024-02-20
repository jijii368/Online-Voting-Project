package ace.project.vote.Group.Project.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;

        if(request.getSession().getAttribute("loginAdmin")!=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            response.sendRedirect("/home");
        }
    }
}
