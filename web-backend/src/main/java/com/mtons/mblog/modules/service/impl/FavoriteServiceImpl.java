package com.mtons.mblog.modules.service.impl;

import com.mtons.mblog.modules.data.FavoriteVO;
import com.mtons.mblog.modules.data.PostVO;
import com.mtons.mblog.modules.repository.FavoriteRepository;
import com.mtons.mblog.base.utils.BeanMapUtils;
import com.mtons.mblog.modules.entity.Favorite;
import com.mtons.mblog.modules.service.FavoriteService;
import com.mtons.mblog.modules.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author langhsu on 2015/8/31.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class FavoriteServiceImpl extends BaseService implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private PostService postService;

    @Override
    public Page<FavoriteVO> pagingByUserId(Pageable pageable, String uid) {
        Page<Favorite> page = favoriteRepository.findAllByUid(pageable, uid);

        List<FavoriteVO> rets = new ArrayList<>();
        Set<Long> postIds = new HashSet<>();
        for (Favorite po : page.getContent()) {
            rets.add(BeanMapUtils.copy(po));
            postIds.add(po.getPostId());
        }

        if (postIds.size() > 0) {
            Map<Long, PostVO> posts = postService.findMapByIds(postIds);

            for (FavoriteVO t : rets) {
                PostVO p = posts.get(t.getPostId());
                t.setPost(p);
            }
        }
        return new PageImpl<>(rets, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public void add(String uid, String articleBlogId) {
        Favorite po = favoriteRepository.findByUidAndArticleBlogId(uid, articleBlogId);

        Assert.isNull(po, "您已经收藏过此文章");

        PostVO articleBlog = postService.get(articleBlogId);

        // 如果没有喜欢过, 则添加记录
        po = new Favorite();
        po.setUserId(articleBlog.getAuthorId());
        po.setUid(uid);
        po.setPostId(articleBlog.getId());
        po.setArticleBlogId(articleBlogId);
        po.setCreated(new Date());

        favoriteRepository.save(po);
    }

    @Override
    @Transactional
    public void delete(String uid, String articleBlogId) {
        Favorite po = favoriteRepository.findByUidAndArticleBlogId(uid, articleBlogId);
        Assert.notNull(po, "还没有喜欢过此文章");
        favoriteRepository.delete(po);
    }

    @Override
    @Transactional
    public void delete(String articleBlogId) {
        int rows = favoriteRepository.deleteByArticleBlogId(articleBlogId);
        log.info("favoriteRepository delete {}", rows);
    }

}