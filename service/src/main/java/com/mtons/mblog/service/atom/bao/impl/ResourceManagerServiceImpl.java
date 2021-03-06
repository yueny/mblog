/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.service.atom.bao.impl;

import com.mtons.mblog.bo.ResourceBO;
import com.mtons.mblog.entity.bao.Resource;
import com.mtons.mblog.dao.mapper.ResourceMapper;
import com.mtons.mblog.service.atom.bao.ResourceManagerService;
import com.yueny.rapid.lang.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * 图片资源管理
 */
@Service
@Transactional
public class ResourceManagerServiceImpl extends AbstractPlusService<ResourceBO, Resource, ResourceMapper>
        implements ResourceManagerService {
    @Override
    public int updateAmount(Collection<String> md5s, long increment) {
        return baseMapper.updateAmount(md5s, increment);
    }

    @Override
    public int updateAmountByIds(Collection<Long> ids, long increment) {
        return baseMapper.updateAmountByIds(ids, increment);
    }

    @Override
    public String save(ResourceBO resourceBO) {
        if(resourceBO == null){
            return null;
        }

        if(StringUtils.isEmpty(resourceBO.getThumbnailCode())){
            resourceBO.setThumbnailCode(UuidUtil.getUUIDForNumber32());
        }

        insert(resourceBO);

        return resourceBO.getThumbnailCode();
    }
}
