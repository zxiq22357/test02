package com.crm.workbench.dao;

import com.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity activity);

    List<Activity> getListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int delectById(String[] id);

    Activity getConditionById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> showDetail(String id);

    List<Activity> addActivityDetail(Map<String, String> map);

    List<Activity> showActivityByAname(String aname);
}
