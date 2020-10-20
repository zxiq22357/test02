package com.crm.workbench.dao;

import com.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {

    Boolean save(Clue clue);

    List<Clue> show();

    Clue detail(String id);
}
