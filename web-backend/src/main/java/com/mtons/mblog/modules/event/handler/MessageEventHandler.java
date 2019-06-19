package main.java.com.mtons.mblog.modules.event.handler;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.modules.data.MessageVO;
import com.mtons.mblog.modules.data.PostVO;
import main.java.com.mtons.mblog.modules.event.MessageEvent;
import main.java.com.mtons.mblog.modules.service.MessageService;
import main.java.com.mtons.mblog.modules.service.PostService;
import org.apache.commons.lang3.StringUtils;
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
            case Consts.MESSAGE_EVENT_FAVOR_POST:
                PostVO p = postService.get(event.getPostId());
                nt.setUserId(p.getAuthorId());
                break;

            //有人评论了你
            case Consts.MESSAGE_EVENT_COMMENT:
            // 有人回复了你
            case Consts.MESSAGE_EVENT_COMMENT_REPLY:
                PostVO p2 = postService.get(event.getPostId());
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
