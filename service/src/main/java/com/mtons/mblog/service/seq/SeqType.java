package com.mtons.mblog.service.seq;

import com.yueny.superclub.api.enums.core.IEnumType;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author yueny09 <deep_blue_yang@163.com>
 *
 * @DATE 2019/6/13 下午1:43
 *
 */
public enum SeqType implements IEnumType {
    /**
     * 博文ID
     */
    ARTICLE_BLOG_ID,
    /**
     * 用户 uid
     */
    USER_U_ID,
    /**
     * 简单序列
     */
    SIMPLE,;


    public static SeqType getBy(String name) {
        for(SeqType type : values()) {
            if(StringUtils.endsWith(type.name(), name)) {
                return type;
            }
        }
        return null;
    }

}
