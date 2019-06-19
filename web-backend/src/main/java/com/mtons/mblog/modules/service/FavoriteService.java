package com.mtons.mblog.modules.service;

import com.mtons.mblog.modules.data.FavoriteVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 收藏记录
 * @author langhsu
 */
public interface FavoriteService {
    /**
     * 查询用户收藏记录
     * @param pageable
     * @param uid
     * @return
     */
    Page<FavoriteVO> pagingByUserId(Pageable pageable, String uid);

    void add(String uid, String articleBlogId);
    void delete(String uid, String articleBlogId);
    void delete(String articleBlogId);
}