package com.mtons.mblog.service.atom.bao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mtons.mblog.bo.PostTagVO;
import com.mtons.mblog.dao.mapper.PostTagMapper;
import com.mtons.mblog.entity.bao.PostTag;
import com.mtons.mblog.service.atom.bao.PostTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-08-22 17:19
 */
@Service
public class PostTagServiceImpl extends AbstractPlusService<PostTagVO, PostTag, PostTagMapper>
        implements PostTagService {
    @Override
    public boolean deleteByPostId(Long postId) {
        LambdaQueryWrapper<PostTag> wrapper = new QueryWrapper<PostTag>().lambda();
        wrapper.eq(PostTag::getPostId, postId);

        return delete(wrapper);
    }

    @Override
    public PostTagVO findByPostIdAndTagId(Long postId, Long tagId) {
        LambdaQueryWrapper<PostTag> wrapper = new QueryWrapper<PostTag>().lambda();
        wrapper.eq(PostTag::getPostId, postId);
        wrapper.eq(PostTag::getTagId, tagId);

        return get(wrapper);
    }

    @Override
    public Page<PostTagVO> findAll(Pageable pageable, Long tagId) {
//        QueryWrapper<PostTag> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PostTag> queryWrapper = new QueryWrapper<PostTag>().lambda();
        queryWrapper.eq(PostTag::getTagId, tagId);
        queryWrapper.orderByDesc(PostTag::getId);

        return findAll(pageable, queryWrapper);
    }
}
