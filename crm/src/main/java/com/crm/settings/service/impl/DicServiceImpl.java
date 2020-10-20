package com.crm.settings.service.impl;

import com.crm.settings.dao.DicTypeDao;
import com.crm.settings.dao.DicValueDao;
import com.crm.settings.domain.DicType;
import com.crm.settings.domain.DicValue;
import com.crm.settings.service.DicService;
import com.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：周熙强
 */
public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        //将类型取出来
        List<DicType> listType = dicTypeDao.getType();

        //将类型对应的值取出来
        Map<String,List<DicValue>> map = new HashMap<>();

        for (DicType dt : listType){
            List<DicValue> listValue = dicValueDao.getValue(dt.getCode());
            map.put(dt.getCode(),listValue);
        }

        return map;
    }
}
