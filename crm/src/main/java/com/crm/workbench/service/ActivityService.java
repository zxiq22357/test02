package com.crm.workbench.service;

import com.crm.vo.PaginationVo;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVo<Activity> dataList(Map<String, Object> map);

    boolean delect(String[] id);

    Map<String, Object> getUser(String id);

    boolean updateCondition(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> showRemark(String id);

    boolean delectRemark(String id);

    Boolean saveRemark(ActivityRemark activityRemark);

    Boolean editRemark(ActivityRemark activityRemark);

    List<Activity> showActivityByAname(String aname);
}
