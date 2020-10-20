package com.crm.settings.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.MD5Util;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：周熙强
 */
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到登录验证中");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        //需要将密码的明文转换成MD5密文的形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("------------------>ip" + ip);

        //未来业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user = us.login(loginAct,loginPwd,ip);

            request.getSession().setAttribute("user",user);

            System.out.println("成功");

            //程序执行到这，说明程序执行成功，没有异常，返回成功信息，用json返回
            //调用工具类中的PrintJson(实际就是jackson类)
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            //执行到这，说明登录失败，抛出异常，该异常的信息要返回到前端显示
            e.printStackTrace();
            String msg = e.getMessage();
            /*
            * 现在控制层需要向前端提供多项信息，响应ajax请求
            * 通过以下两种方式传递信息，
            *   1.创建一个map集合，存储信息
            *   2.创建一个Vo类，该引用类存储信息
            *
            * 当该信息被多次应用时，创建Vo类
            * 当该信息只是当前使用时，用map集合，减少类的开销
            * */
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
