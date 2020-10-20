package com.crm.workbench.web.controller;

import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.*;
import com.crm.vo.PaginationVo;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.ActivityRemark;
import com.crm.workbench.service.ActivityService;
import com.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * 作者：周熙强
 */
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");

        String path = request.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            search(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delect(request,response);
        }else if ("/workbench/activity/getUser.do".equals(path)){
            getUser(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/showRemark.do".equals(path)){
            showRemark(request,response);
        }else if ("/workbench/activity/delectRemark.do".equals(path)){
            delectRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if ("/workbench/activity/editRemark.do".equals(path)){
            editRemark(request,response);
        }
    }

    private void editRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行备份信息的修改工作!!");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        System.out.println(id);
        System.out.println(noteContent);

        ActivityRemark activityRemark = new ActivityRemark();

        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean bool = activityService.editRemark(activityRemark);

        PrintJson.printJsonFlag(response,bool);


    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入保存备份的操作");
        String remark = request.getParameter("remark");
        String aid = request.getParameter("activityAid");

        String id = UUIDUtil.getUUID();
        String noteContent = remark;
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        String activityId = aid;

        ActivityRemark activityRemark = new ActivityRemark();

        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setActivityId(activityId);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean bool = activityService.saveRemark(activityRemark);

        PrintJson.printJsonFlag(response,bool);


    }

    private void delectRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除备份的操作");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String id = request.getParameter("id");
        boolean bool = activityService.delectRemark(id);

        PrintJson.printJsonFlag(response,bool);
    }

    private void showRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取备份信息的操作");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String id = request.getParameter("activityAid");

        List<ActivityRemark> activityRemark = activityService.showRemark(id);

        PrintJson.printJsonObj(response,activityRemark);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到跳转到详细信息页的操作");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = activityService.detail(id);
        request.setAttribute("a",a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动更新列表");

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startData");
        String endDate = request.getParameter("endData");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();

        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        boolean bool = activityService.updateCondition(activity);

        PrintJson.printJsonFlag(response,bool);
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户名列表");

        String id = request.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
        * 获得aList,a
        * */

        Map<String,Object> map = activityService.getUser(id);

        PrintJson.printJsonObj(response,map);
    }

    private void delect(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进行市场信息删除操作");
        String id[]= request.getParameterValues("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean bool = activityService.delect(id);

        PrintJson.printJsonFlag(response,bool);
    }

    private void search(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("刷新用户信息列表,查询信息(条件查询+分页查询)");

        String pageNos = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNos);
        String pageSizes = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizes);
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //计算出数据库分页语法中的第一个参数：越过前几条数据
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //由于以后需要用到分页查询，应各个模块的需要，选择使用一个加了泛型的通用vo要方便很多
        //vo是用在后台数据无法用现有的domain类存储且需要频繁使用时，给前端传数据所使用的封装类

        PaginationVo<Activity> paginationVo = activityService.dataList(map);
        //List<Activity> list = activityService.search(map);

        PrintJson.printJsonObj(response,paginationVo);
    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = userService.getUserList();

        PrintJson.printJsonObj(response,users);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行保存添加的用户信息列表");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        boolean bool = activityService.save(activity);

        PrintJson.printJsonFlag(response,bool);
    }

}
