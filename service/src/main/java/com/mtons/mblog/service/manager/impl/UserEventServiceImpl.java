/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.service.manager.impl;

import com.mtons.mblog.base.consts.Consts;
import com.mtons.mblog.dao.repository.UserRepository;
import com.mtons.mblog.service.manager.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

/**
 * 用户事件操作, 用于统计用户信息
 * @author langhsu
 */
@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {
    @Autowired
    private UserRepository userMapper;

    @Override
    public void identityPost(Long userId, boolean plus) {
        userMapper.updatePosts(userId, (plus) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP, Calendar.getInstance().getTime());
    }

    @Override
    public void identityComment(Long userId, boolean plus) {
        userMapper.updateComments(Collections.singleton(userId), (plus) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP, Calendar.getInstance().getTime());
    }

    @Override
    public void identityComment(Set<Long> userIds, boolean plus) {
        userMapper.updateComments(userIds, (plus) ? Consts.IDENTITY_STEP : Consts.DECREASE_STEP, Calendar.getInstance().getTime());
    }

}