package com.mtons.mblog.service.event.handler;

import com.mtons.mblog.base.consts.Consts;
import com.mtons.mblog.bo.MessageVO;
import com.mtons.mblog.bo.PostBO;
import com.mtons.mblog.service.event.MessageEvent;
import com.mtons.mblog.service.atom.MessageService;
import com.mtons.mblog.service.atom.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author langhsu on 2015/8/31.
 */
@Component
public class MessageEventHandler implements ApplicationListener<MessageEvent> {
    @Autowired
    private MessageService messageService;
    @Autowired
    private PostService postService;

    @Async
    @Override
    public void onApplicationEvent(MessageEvent event) {
        MessageVO nt = new MessageVO();
        nt.setPostId(event.getPostId());
        nt.setFromId(event.getFromUserId());
        nt.setEvent(event.getEvent());

        switch (event.getEvent()) {
            // 有人喜欢了你的文章
            case Consts.MESSAGE_EVENT_FAVOR_POST:
                PostBO p = postService.get(event.getPostId());
                nt.setUserId(p.getAuthorId());
                break;

            //有人评论了你
            case Consts.MESSAGE_EVENT_COMMENT:
            // 有人回复了你
            case Consts.MESSAGE_EVENT_COMMENT_REPLY:
                PostBO p2 = postService.get(event.getPostId());
                nt.setUserId(p2.getAuthorId());

                // 自增评论数
                postService.identityComments(p2.getArticleBlogId());
                break;
            default:
                nt.setUserId(event.getToUserId());
        }

        messageService.send(nt);
    }
}