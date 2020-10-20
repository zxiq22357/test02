package com.crm.workbench.service.impl;

import com.crm.utils.SqlSessionUtil;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.dao.ActivityDao;
import com.crm.workbench.dao.ClueActivityRelationDao;
import com.crm.workbench.dao.ClueDao;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.Clue;
import com.crm.workbench.domain.ClueActivityRelation;
import com.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

/**
 * 作者：周熙强
 */
public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean save(Clue clue) {
        Boolean bool = clueDao.save(clue);
        return bool;
    }

    @Override
    public List<Clue> show() {
        List<Clue> list = clueDao.show();
        return list;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public List<Activity> showDetal(String id) {
        List<Activity> list = activityDao.showDetail(id);
        return list;
    }

    @Override
    public Boolean delectDetail(String id) {
        Boolean bool = clueActivityRelationDao.delectDetail(id);
        return bool;
    }

    @Override
    public List<Activity> addActivityDetail(Map<String, String> map) {
        List<Activity> list = activityDao.addActivityDetail(map);
        return list;
    }

    @Override
    public boolean bound(String clueId, String[] activityId) {

        Boolean bool = true;

        for (String a : activityId){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(a);

            int count = clueActivityRelationDao.bound(car);
            if (count != 1){
                bool = false;
            }
        }

        return bool;
    }
}
