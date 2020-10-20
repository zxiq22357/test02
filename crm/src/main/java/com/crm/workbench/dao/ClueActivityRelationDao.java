package com.crm.workbench.dao;

import com.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {

    Boolean delectDetail(String id);

    int bound(ClueActivityRelation car);
}
