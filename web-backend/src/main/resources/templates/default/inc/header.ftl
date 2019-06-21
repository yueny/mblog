<!-- Login dialog BEGIN -->
<div id="login_alert" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document" style="width: 450px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">请登录</h4>
            </div>
            <div class="modal-body">
                <form method="POST" action="${base}/login" accept-charset="UTF-8">
                    <div class="form-group">
                        <label class="control-label" for="username">账号</label>
                        <input class="form-control" id="ajax_login_username" name="username" type="text" required>
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="password">密码</label>
                        <input class="form-control" id="ajax_login_password" name="password" type="password" required>
                    </div>
                    <div class="form-group">
                        <button id="ajax_login_submit" class="btn btn-success btn-block btn-sm" type="button">
                            登录 Use it
                        </button>
                    </div>
                    <div class="form-group">
                        <div id="ajax_login_message" class="text-danger"></div>
                    </div>
                    <@controls name="register">
                        <fieldset class="form-group">
			    <#if site.hasValue("weibo_client_id")>
                            <a class="btn btn-default btn-block" href="${base}/oauth/callback/call_weibo">
                                <i class="fa fa-weibo"></i> 微博帐号登录
                            </a>
                            </#if>
                            <#if site.hasValue("qq_app_id")>
                            <a class="btn btn-default btn-block" href="${base}/oauth/callback/call_qq">
                                <i class="fa fa-qq"></i> QQ帐号登录
                            </a>
                            </#if>
                            <#if site.hasValue("github_client_id")>
                            <a class="btn btn-default btn-block" href="${base}/oauth/callback/call_github">
                                <i class="fa fa-github"></i> Github帐号登录
                            </a>
                            </#if>
                        </fieldset>
                    </@controls>
                </form>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-- Login dialog END -->

<!--[if lt IE 9]>
<div class="alert alert-danger alert-dismissible fade in" role="alert" style="margin-bottom:0">
	<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
	<strong>您正在使用低版本浏览器，</strong> 在本页面的显示效果可能有差异。
	建议您升级到
	<a href="http://www.google.cn/intl/zh-CN/chrome/" target="_blank">Chrome</a>
	或以下浏览器：
	<a href="www.mozilla.org/en-US/firefox/‎" target="_blank">Firefox</a> /
	<a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> /
	<a href="http://www.opera.com/" target="_blank">Opera</a> /
	<a href="http://windows.microsoft.com/en-us/internet-explorer/download-ie" target="_blank">Internet Explorer 9+</a>
</div>
<![endif]-->

<!-- Fixed navbar -->
<header class="site-header headroom">
    <div class="container">
        <nav class="navbar" role="navigation">
            <div class="navbar-header">
                <button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${base}/">
                    <img src="<@resource src=options['site_logo']/>"/>
                </a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
					<#if profile??>
						<li data="user">
							<a href="${base}/users/${profile.domainHack}" nav="user">我的主页</a>
						</li>
					</#if>

					<#list channels as row>
                        <#--  是否为叶子节点。1为是叶子节点，0为不是叶子节点(存在后续分支) isLeaf  -->
                        <#if row.nodeType?? && row.nodeType.isLeaf == 1>
                            <li>
                                <a href="${base}/channel/${row.flag}" nav="${row.name}">${row.name}</a>
                            </li>
                        <#else>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    ${row.name}
                                    <b class="caret"></b>
                                </a>

                                <ul class="dropdown-menu">
                                    <#if row.children??>
                                        <#list row.children as cc>
                                            <li><a href="${base}/channel/${cc.flag}" nav="${cc.name}">${cc.name}</a></li>
                                        </#list>
                                    <#else>
                                        <li role="presentation" class="dropdown-header">无</li>
                                    </#if>
                                </ul>
                            </li>
                        </#if>

                    </#list>

                    <li>
                        <a href="${base}/tags" nav="tags">标签</a>
                    </li>
                    <#--
                    <li>
                        <a href="${base}/tags" nav="tags">标签</a>
                    </li>
                    -->
                </ul>

                <ul class="navbar-button list-inline" id="header_user">
                    <li view="search" class="hidden-xs hidden-sm">
                        <form method="GET" action="${base}/search" accept-charset="UTF-8" class="navbar-form navbar-left">
                            <div class="form-group">
                                <input class="form-control search-input mac-style" placeholder="搜索" name="kw" type="text" value="${kw}">
                                <button class="search-btn" type="submit"><i class="fa fa-search"></i></button>
                            </div>
                        </form>
                    </li>

                    <#if profile??>
                        <@controls name="post">
                            <li>
                                <a href="${base}/post/editing" class="plus"
                                   data-toggle="popover" data-placement="bottom" data-content="写文章">
                                    <i class="icon icon-note"></i>
                                    写文章
                                </a>
                            </li>
                        </@controls>
                        <li class="dropdown">
                            <a href="#" class="user dropdown-toggle" data-toggle="dropdown">
                                <img class="img-circle" src="<@resource src=profile.avatar + '?t=' + .now?time />">
                                <span>${profile.name}</span>
                            </a>
                            <ul class="dropdown-menu" role="menu">
                                <li>
                                    <a href="${base}/users/${profile.domainHack}">我的主页</a>
                                </li>
                                <li>
                                    <a href="${base}/settings/profile">编辑资料</a>
                                </li>
                                <li class="divider"></li>
                                <@shiro.hasPermission name="admin">
                                    <li><a href="${base}/admin">后台管理</a></li>
                                </@shiro.hasPermission>
                                <li class="divider"></li>
                                <li><a href="${base}/logout">退出</a></li>
                            </ul>
                        </li>
                    <#else>
                        <@controls name="login_show">
                            <li><a href="${base}/login" class="btn btn-default btn-sm signup">登录</a></li>
                        </@controls>
                        <@controls name="register">
                            <li><a href="${base}/register" class="btn btn-primary btn-sm signup">注册</a></li>
                        </@controls>
                    </#if>
                    <li>
                        <a class="plus">
                            <i class="tooltip-icon glyphicon glyphicon-exclamation-sign"></i>
                        </a>
                    </li>
                </ul>

            </div>
        </nav>
    </div>
</header>

<script type="text/javascript">
    $(function () {
        $('a[nav]').each(function(){
            $this = $(this);
            if($this[0].href == String(window.location)){
                $this.closest('li').addClass("active");
            }
        });

        $("[data-toggle='popover']").popover();
    });
</script>

<script>
    $(function(){
        var tips;
        $('i.tooltip-icon').on({
            mouseenter:function(){
                var that = this;
                tips =layer.tips(
                        "<span style='color:#000;'>本平台暂时不大面积对外开放，需要注册账户或权限升级问题，请联系邮箱: yueny09@163.com</span>",
                        that,
                        {tips:[2,'#fff'],time:0,area: 'auto',maxWidth:500}
                );
            },
            mouseleave:function(){
                layer.close(tips);
            }
        });
    });
</script>
<!-- Header END -->
