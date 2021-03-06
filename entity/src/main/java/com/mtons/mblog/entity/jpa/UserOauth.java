/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.entity.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 第三方开发授权登录
 *
 * @author langhsu on 2015/8/12.
 */
@Entity
@Table(name = "mto_user_oauth")
@Getter
@Setter
public class UserOauth extends com.yueny.kapo.api.pojo.instance.Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // 系统中的用户ID

    @Column(name = "oauth_type")
    private Integer oauthType; // 认证类型：QQ、新浪

    @Column(name = "oauth_user_id", length = 128)
    private String oauthUserId; // 对应第三方用户ID

    @Column(name = "oauth_code", length = 128)
    private String oauthCode;  // 第三方返回的code

    @Column(name = "access_token", length = 128)
    private String accessToken;  // 访问令牌

    @Column(name = "expire_in", length = 128)
    private String expireIn;

    @Column(name = "refresh_token", length = 128)
    private String refreshToken;

}
