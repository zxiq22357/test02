package com.crm.web.filter;

import com.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者：周熙强
 */
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入登录过滤器");

        //向下转型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();

        //该路径资源允许直接在地址栏输入登录
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(request,response);
        //该路径资源不允许直接在地址栏输入登录
        }else {
            User user = (User) request.getSession().getAttribute("user");

            if (user != null){
                filterChain.doFilter(request,response);
            }else {
                //重定向到登录页
                //重定向的路径怎么写
                //在实际项目开发中，不论前端后端，一律使用绝对路径
            /*转发的路径写法:
                使用的是一种特殊的绝对路径的写法，这种绝对路径不加/项目名，这种路径也称之为内部路径
                如:/login.do
              重定向的路径写法:
                使用的是传统的路径写法，必须加/项目名开头，后面跟具体路径
                如:/web/login.do
             */

                //这里为什么使用重定向，而不用转发
                /*
                 * 转发之后，浏览器上的路径停留在老路径上，没有换成登录界面的路径
                 * 重定向会将浏览器网页栏的路径更新为新资源路径
                 * */
                response.sendRedirect(request.getContextPath() + "/login.jsp");

            }
        }
    }
}
