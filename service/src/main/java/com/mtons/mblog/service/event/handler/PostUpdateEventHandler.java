package com.mtons.mblog.service.event.handler;

import com.mtons.mblog.service.event.PostUpdateEvent;
import com.mtons.mblog.service.atom.CommentService;
import com.mtons.mblog.service.atom.FavoriteService;
import com.mtons.mblog.service.atom.MessageService;
import com.mtons.mblog.service.atom.TagService;
import com.mtons.mblog.service.manager.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 博文发布和博文删除的事件
 *
 * @author yueny09 <deep_blue_yang@163.com>
 *
 * @DATE 2019/6/18 下午12:06
 *
 */
@Component
public class PostUpdateEventHandler implements ApplicationListener<PostUpdateEvent> {
    @Autowired
    private UserEventService userEventService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MessageService messageService;

    @Async
    @Override
    public void onApplicationEvent(PostUpdateEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getAction()) {
            // 文章发布
            case PostUpdateEvent.ACTION_PUBLISH:
                userEventService.identityPost(event.getUserId(), true);
                break;

            // 文章删除
            case PostUpdateEvent.ACTION_DELETE:
                userEventService.identityPost(event.getUserId(), false);

                favoriteService.delete(event.getArticleBlogId());

                commentService.deleteByPostId(event.getPostId());
                tagService.deteleMappingByPostId(event.getPostId());
                messageService.deleteByPostId(event.getPostId());
                break;
        }
    }
}