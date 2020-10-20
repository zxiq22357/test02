package com.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 作者：周熙强
 */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到字符器编码的过滤器");

        //过滤post请求中文乱码的问题
        servletRequest.setCharacterEncoding("UTF-8");
        //过滤响应流响应中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");
        //过滤结束，将请求放行
        filterChain.doFilter(servletRequest,servletResponse);

    }

}
