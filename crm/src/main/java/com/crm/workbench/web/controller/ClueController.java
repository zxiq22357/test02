package com.crm.workbench.web.controller;

import com.crm.settings.domain.DicValue;
import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.PrintJson;
import com.crm.utils.ServiceFactory;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.Clue;
import com.crm.workbench.service.ActivityService;
import com.crm.workbench.service.ClueService;
import com.crm.workbench.service.impl.ActivityServiceImpl;
import com.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 作者：周熙强
 */
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索控制器");

        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);

        }else if("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/clue/show.do".equals(path)){
            show(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/showDetail.do".equals(path)){
            showDetail(request,response);
        }else if ("/workbench/clue/delectDetail.do".equals(path)){
            delectDetail(request,response);
        }else if ("/workbench/clue/addActivityDetail.do".equals(path)){
            addActivityDetail(request,response);
        }else if ("/workbench/clue/bound.do".equals(path)){
            bound(request,response);
        }else if ("/workbench/clue/addSearchActivityDetail.do".equals(path)){
            addSearchActivityDetail(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入交易转换操作");

        String clueId = request.getParameter("clueId");

        String flag = request.getParameter("flag");

        if (flag.equals(true)){

        }
    }

    private void addSearchActivityDetail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("convert查询市场活动列表(模糊查询)");

        String aname = request.getParameter("aname");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> list = activityService.showActivityByAname(aname);

        PrintJson.printJsonObj(response,list);
    }

    private void bound(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联信息的操作");

        String clueId = request.getParameter("clueId");
        String activityId[] = request.getParameterValues("activityId");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean bool = clueService.bound(clueId,activityId);

        PrintJson.printJsonFlag(response,bool);
    }

    private void addActivityDetail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入添加线索的模糊查询操作");

        String clueId = request.getParameter("clueId");
        String aname = request.getParameter("aname");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Map<String,String> map = new HashMap<>();

        map.put("clueId",clueId);
        map.put("aname",aname);

        List<Activity> list = clueService.addActivityDetail(map);

        PrintJson.printJsonObj(response,list);
    }


    private void delectDetail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入取消关联的操作");

        String id = request.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Boolean bool = clueService.delectDetail(id);

        PrintJson.printJsonFlag(response,bool);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索中的活动关联查询操作");

        String id = request.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Activity> list = clueService.showDetal(id);

        PrintJson.printJsonObj(response,list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入线索的详细信息页面");
        String id = request.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = clueService.detail(id);
        request.setAttribute("clue",clue);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void show(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入展示线索信息的操作");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Clue> list = clueService.show();

        PrintJson.printJsonObj(response,list);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索创建保存操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();

        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setNextContactTime(nextContactTime);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean bool = clueService.save(clue);

        PrintJson.printJsonFlag(response,bool);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

            List<User> list = userService.getUserList();

        PrintJson.printJsonObj(response,list);

    }
}
