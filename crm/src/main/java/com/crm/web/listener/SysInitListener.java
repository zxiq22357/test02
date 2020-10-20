package com.crm.web.listener;

import com.crm.settings.domain.DicValue;
import com.crm.settings.service.DicService;
import com.crm.settings.service.impl.DicServiceImpl;
import com.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：周熙强
 */
public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文对象创建");

        ServletContext servletContext = event.getServletContext();

        //从后台中取得数据字典的值
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());

        //7个list封装到一个map中，然后返回到服务端
        Map<String, List<DicValue>> map = dicService.getAll();

        Set<String> set = map.keySet();

        for (String key : set){
            servletContext.setAttribute(key,map.get(key));
        }

        System.out.println("上下文对象赋值结束");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
