package com.crm.workbench.dao;

import com.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAid(String[] id);

    int delectCountById(String[] id);

    List<ActivityRemark> show(String id);

    int delectRemarkById(String id);

    int saveRemark(ActivityRemark activityRemark);

    int editRemark(ActivityRemark activityRemark);
}
