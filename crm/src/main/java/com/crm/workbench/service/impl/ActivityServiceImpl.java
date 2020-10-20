package com.crm.workbench.service.impl;

import com.crm.settings.dao.UserDao;
import com.crm.settings.domain.User;
import com.crm.utils.SqlSessionUtil;
import com.crm.vo.PaginationVo;
import com.crm.workbench.dao.ActivityDao;
import com.crm.workbench.dao.ActivityRemarkDao;
import com.crm.workbench.dao.ClueActivityRelationDao;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.ActivityRemark;
import com.crm.workbench.service.ActivityService;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：周熙强
 */
public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean save(Activity activity) {
        int a = activityDao.save(activity);
        if (a == 1){
            return true;
        }
        return false;
    }

    @Override
    public PaginationVo<Activity> dataList(Map<String, Object> map) {
        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dateList
        List<Activity> list = activityDao.getListByCondition(map);

        //将total和dateList封装到vo中
        PaginationVo vo = new PaginationVo();
        vo.setCount(total);
        vo.setDataList(list);

        return vo;
    }

    @Override
    public boolean delect(String[] id) {
        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAid(id);

        //删除备份，返回收到影响的条数
        int count2 = activityRemarkDao.delectCountById(id);

        if (count1 != count2){
            flag = false;
        }

        //删除市场活动
        int count3 = activityDao.delectById(id);

        if (count3 != id.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUser(String id) {
        //取uList
        List<User> uList = userDao.getUserList();

        //取a
        Activity a = activityDao.getConditionById(id);

        //封装到集合map
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);

        //返回map
        return map;
    }

    @Override
    public boolean updateCondition(Activity activity) {
        int a = activityDao.update(activity);
        if (a == 1){
            return true;
        }
        return false;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    @Override
    public List<ActivityRemark> showRemark(String id) {
        List<ActivityRemark> activityRemark = activityRemarkDao.show(id);
        return activityRemark;
    }

    @Override
    public boolean delectRemark(String id) {
        int count = activityRemarkDao.delectRemarkById(id);
        if (count == 1){
            return true;
        }
        return false;
    }

    @Override
    public Boolean saveRemark(ActivityRemark activityRemark) {
        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count == 1){
            return true;
        }
        return false;
    }

    @Override
    public Boolean editRemark(ActivityRemark activityRemark) {
        int count = activityRemarkDao.editRemark(activityRemark);
        if (count == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<Activity> showActivityByAname(String aname) {

        List<Activity> list = activityDao.showActivityByAname(aname);

        return list;
    }

}
