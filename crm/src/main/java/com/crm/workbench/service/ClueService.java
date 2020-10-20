package com.crm.workbench.service;

import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    List<Clue> show();

    Clue detail(String id);

    List<Activity> showDetal(String id);

    Boolean delectDetail(String id);

    List<Activity> addActivityDetail(Map<String, String> map);

    boolean bound(String clueId, String[] activityId);
}
