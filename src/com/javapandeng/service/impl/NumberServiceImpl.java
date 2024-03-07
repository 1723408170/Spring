package com.javapandeng.service.impl;

import com.javapandeng.base.BaseDao;
import com.javapandeng.base.BaseServiceImpl;
import com.javapandeng.mapper.NumberMapper;
import com.javapandeng.po.Number1;
import com.javapandeng.service.NumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NumberServiceImpl extends BaseServiceImpl<Number1> implements NumberService {

    @Autowired
    private NumberMapper numberMapper;

    @Override
    public BaseDao<Number1> getBaseDao() {
        return numberMapper;
    }
}
