<#include "/admin/utils/ui.ftl"/>
<@layout>

<section class="content-header">
    <h1>访问记录管理</h1>
    <ol class="breadcrumb">
        <li><a href="${base}/admin">首页</a></li>
        <li class="active">访问记录管理</li>
    </ol>
</section>
<section class="content container-fluid">
    <div class="row">
        <div class="col-md-12">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">访问记录列表</h3>
                    <div class="box-tools">
                        <a class="btn btn-default btn-sm" href="javascrit:;" data-action="batch_del">批量删除</a>
                    </div>
                </div>
                <div class="box-body">
                    <input type="hidden" id="currentDayer" value="${nowDate}">

                    <#-- 查询条件 -->
                    <form id="qForm" class="form-inline search-row">
                        <#-- 隐藏的分页信息 -->
                        <input type="hidden" name="pageNo"/>

<#--                        <div class="form-group">-->
<#--                            <select class="selectpicker show-tick form-control" name="channelId">-->
<#--                                <option value="0">所有栏目</option>-->
<#--                                <#list channels as channel>-->
<#--                                    <option value="${channel.id}" <#if (channelId == channel.id)> selected </#if>>${channel.name}</option>-->
<#--                                </#list>-->
<#--                            </select>-->
<#--                        </div>-->

                        <div class="form-group">
                            <div class="input-group-btn">
                                <button class="btn btn-default" type="button">
                                    Agent(<span class="text-danger">模糊</span>)
                                </button>
                                <input type="text" class="form-control" name="clientAgent"
                                       placeholder='Agent，左模糊查询, sql(a%)' title='Agent，左模糊查询'>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="input-group date" data-provide="datepicker">
                                 <span class="input-group-addon">
                                     <span class="glyphicon glyphicon-th"></span>
                                 </span>
                                <input type="text" class="form-control datepicker"
                                       id="createDate" name="createDate"
                                       placeholder='日期' title='日期'>
                            </div>
                        </div>

                        <div class="pull-right btn-group">
                            <button id="btn_search" onclick="searchInfo()" type="button"
                                    class="btn btn-primary btn-space">
                                <span class="fa fa-search" aria-hidden="true" class="btn-icon-space"></span>
                                查询
                            </button>
                            <button id="btn_reset" onclick="resetSearch()" type="button"
                                    class="btn btn-default btn-space">
                                <span class="fa fa-eraser" aria-hidden="true" class="btn-icon-space"></span>
                                重置
                            </button>
                            <#--
                            <button id="btn_refresh" onclick="refresh()" type="button"
                                    class="btn btn-default btn-space">
                                <span class="fa fa-refresh" class="btn-icon-space"></span>
                            </button>
                            -->
                        </div>
                    </form>

                    <#-- table -->
                    <table id="table" data-toolbar="#toolbar"></table>
                </div>

            </div>
        </div>
    </div>

</section>

    <script type="text/javascript">
        $(function () {
            $.fn.datepicker.defaults.format = "yyyy-mm-dd";
            $.fn.datepicker.defaults.autoclose = true;//选择日期后自动关闭日期选择框
            $.fn.datepicker.defaults.todayBtn = true;//显示今天按钮
            $.fn.datepicker.defaults.todayHighlight = true;//当天高亮显示
            $.fn.datepicker.defaults.pickerPosition="bottom-left",
            <#-- 只显示一年的日期365天, or "-120d"; -->
            // $.fn.datepicker.defaults.startDate = new Date(new Date()-1000 * 60 * 60 * 24 * 365);
            $.fn.datepicker.defaults.endDate = new Date();
        });
        <#--
        $('.datepicker').datepicker({
            format:"yyyy-MM-dd",
            language:"zh-CN", //汉化
            autoclose : true,   //选择日期后自动关闭日期选择框
            todayBtn : "true",  //显示今天按钮
            todayHighlight : true,   //当天高亮显示
            minView: "month",   //不显示时分秒
            showMeridian: 1,
            pickerPosition: "bottom-left",
            startDate : new Date(new Date()-1000 * 60 * 60 * 24 * 365),
            endDate : new Date()
        });
        -->
    </script>

    <script type="text/javascript">
        $(function() {
            $table = $("#table").bootstrapTable({ // 对应table标签的id
                method: "get",
                url: '${ctx}/admin/viewer/get/list.json', // 获取慢服务表格数据的url
                toolbar: '#toolbar',    //工具按钮用哪个容器
                cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
                striped: true,  //表格显示条纹，默认为false
                //sortName: 'pushTime', // 要排序的字段(建议为数据库字段)
                //showRefresh: false,     //是否显示刷新按钮
                pagination: true, // 在表格底部显示分页组件，默认false
                pageList: [10, 25, 50], // 设置页面可以显示的数据条数
                pageNumber: 1,      //初始化加载第一页，默认第一页
                pageSize: 10,      //每页的记录行数（*）
                sidePagination: 'server', // 设置为服务器端分页
                search: false,                      //是否显示表格搜索
                strictSearch: true,
                showRefresh: true,                  //是否显示刷新按钮
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "id",                     //每一行的唯一标识，一般为主键列
                showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图

                /* 得到查询的参数 */
                queryParams : function (params) {
                    // 获取自定义查询条件
                    var temp = queryParams();
                    // 追加分页条件
                    temp["pageSize"] = params.limit;                        //页面大小
                    temp["pageNo"] = (params.offset / params.limit) + 1;  //页码
                    temp["sort"] = params.sort;                         //排序列名
                    temp["sortOrder"] = params.order;                   //排位命令（desc，asc）

                    return temp;
                },
                columns: [
                    <#--    <th title="序号">#</th>-->

                    <#--<#list page.content as viewLog>-->
                    <#--<tr>-->
                    <#--<td>${viewLog_index + 1}</td>-->
                    {
                        checkbox: true,
                        visible: true                  //是否显示复选框
                    },
                    {
                        field: 'clientIp',
                        title: 'IP',
                        align: 'left',
                        valign: 'middle'
                    },
                    {
                        field: 'resourcePath',
                        title: '访问资源路径',
                        align: 'left',
                        valign: 'middle'
                    },
                    {
                        field: 'method',
                        title: '请求方式',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        field: 'parameterJson',
                        title: '请求参数',
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        field: 'clientAgent',
                        title: 'agent',
                        align: 'center',
                        valign: 'middle',
                        formatter: function (value, row, index){
                            return "<span class='text-muted'>" + value + "</span>";
                        }
                    },
                    {
                        field: 'created',
                        title: '访问时间',
                        align: 'center',
                        valign: 'middle'
                        // formatter: function (value, row, index) {
                        //     return dateFormat(value)
                        // }
                    }
                ],
                onLoadSuccess: function(data){  //加载成功时执行
                    layer.msg('加载成功', {time: 500});
                },
                onLoadError: function(){  //加载失败时执行
                    layer.msg('加载数据失败', {icon: 5});
                },
                onDblClickRow: function(row){
                    var $textAndPic = $('<div></div>');

                    // // 带折叠
                    // $textAndPic.append('<div id="accordionB">'+
                    //     '<div>'+
                    //         '<a data-toggle="collapse" data-parent="#accordionB" href="#collapseO">数据信息</a>'+
                    //     '</div>'+
                    //     '<div id="collapseO" class="panel-collapse collapse">'+
                    //     '<div class="panel-body">'+
                    //     '<span class="text-info">起始时间</span>'+
                    //     '[<span class="text-danger">持续总时间</span> (<span class="text-success">自身消耗的时间</span>), <span class="text-primary">在父entry中所占的时间比例</span>, <span class="text-muted">在总时间中所占的时间比例</span>]'+
                    //     '-'+
                    //     '<span class="text-warning">entry信息</span>'+
                    //     '<br/>'+
                    //     'eg:'+
                    //     '<span class="text-info">0</span>'+
                    //     '[<span class="text-danger">739ms</span>(<span class="text-success">48ms</span>), <span class="text-primary">100%</span>, <span class="text-muted">100%</span>]'+
                    //     '-'+
                    //     '<span class="text-warning">DubboInvoke interface com.a.b.c.DxxxQueryService#query</span>'+
                    //     '</div>'+
                    //     '</div>'+
                    //     '</div>');

                    $textAndPic.append('<span>');
                    $textAndPic.append('id: ');
                    $textAndPic.append(row.id);
                    $textAndPic.append('</span><br/>');

                    $textAndPic.append('<span>');
                    $textAndPic.append('clientIp: ');
                    $textAndPic.append(row.clientIp);
                    $textAndPic.append('</span><br/>');

                    $textAndPic.append('<span>');
                    $textAndPic.append('访问资源路径: ');
                    $textAndPic.append('<br/>');
                    $textAndPic.append('<span class="text-success">' + row.resourcePath + '</span>');
                    $textAndPic.append('  <big><b>|</b></big>  ');
                    $textAndPic.append('<span class="text-danger">' + row.method + '</span>');
                    $textAndPic.append('</span><br/>');

                    $textAndPic.append('<span>');
                    $textAndPic.append('客户端: ');
                    $textAndPic.append('<span class="text-success">' + row.clientAgent + ' </span>');
                    $textAndPic.append('</span><br/>');

                    $textAndPic.append('<span>');
                    $textAndPic.append('请求参数: ');
                    $textAndPic.append('<pre>' + row.parameterJson + '</pre>');
                    $textAndPic.append('</span>');

                    $textAndPic.append('<span>');
                    $textAndPic.append('访问时间: ');
                    $textAndPic.append('<span class="text-danger">' + row.created + ' </span>');
                    $textAndPic.append('</span><br/>');

                    <#--$textAndPic.append('<span>');-->
                    <#--$textAndPic.append('系统环境: ' + row.appProfileEnv);-->
                    <#--$textAndPic.append('</span><br/>');-->

                    <#--$textAndPic.append('<span>');-->
                    <#--$textAndPic.append('上报服务器: ' + row.networkAddress);-->
                    <#--$textAndPic.append('</span><br/>');-->

                    <#--$textAndPic.append('<span title="系统上报时间">');-->
                    <#--$textAndPic.append('上报时间: ' + operateTimeFormatter(row.pushTime));-->
                    <#--$textAndPic.append('</span><br/>');-->

                    <#--$textAndPic.append('<span>');-->
                    <#--$textAndPic.append('入库时间: ' + operateTimeFormatter(row.createDate));-->
                    <#--$textAndPic.append('</span><br/>');-->


                    BootstrapDialog.show({
                        title: row.resourcePath + " 访问明细",
                        message: $textAndPic,
                        size: BootstrapDialog.SIZE_WIDE,
                        draggable: true, // Default value is false，可拖拽
                        // closable : false, // Default value is false，点击对话框以外的页面内容可关闭
                        buttons: [
                            // {
                            //     label: '标记',
                            //     icon: 'glyphicon glyphicon-star',
                            //     cssClass: 'btn-warning',
                            //     action: function(dialogRef){
                            //         // 进行行为标记
                            //         dialogRef.close();
                            //     }
                            // },
                            {
                                label: '我知道了',
                                icon: 'fa fa-camera-retro',
                                cssClass: 'btn-primary',
                                action: function(dialogRef){
                                    dialogRef.close();
                                }
                            }
                        ]
                    });
                },
                responseHandler: function(res) {
                    return {
                        <#--
                        res： PageListResponse
                        res.list: PageListResponse 中的 PageList<T> list
                        -->
                        "total": res.list.paginator.items,//总页数
                        "rows": res.list.list   //数据
                    };
                }
            })
        });

        // 获取自定义查询条件
        function queryParams() {
            var param = {};
            $('#qForm').find('[name]').each(function () {
                var value = $(this).val();
                if (value != '') {
                    param[$(this).attr('name')] = value;
                }
            });

            return param;
        }
    </script>

    <script>
        /* 超链接跳转 */
        function operateLinkFormatter(value, row, url){
            return '<a target="_blank" href="' + url + '">' + value + '</a>';
        }
        //连接字段格式化
        function linkFormatter(value, row, index) {
            return "<a href='" + value + "' title='单击打开连接' target='_blank'>" + value + "</a>";
        }
        //Email字段格式化
        function emailFormatter(value, row, index) {
            return "<a href='mailto:" + value + "' title='单击打开连接'>" + value + "</a>";
        }
        //性别字段格式化
        function sexFormatter(value) {
            if (value == "女") { color = 'Red'; }
            else if (value == "男") { color = 'Green'; }
            else { color = 'Yellow'; }

            return '<div  style="color: ' + color + '">' + value + '</div>';
        }
        function dateFormat(value) {
            var dateVal = value + "";

            if (value != null) {
                var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
                var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();

                var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
                var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
                var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

                return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
            }
        }

        // 重置
        function resetSearch() {
            $('#qForm').find('[name]').each(function () {
                $(this).val('');
            });
            $("#createDate").val($("#currentDayer").val());
        }

        /*
        * 查询
        */
        function searchInfo() {
            refresh();
        }

        /*
        * 刷新
        */
        function refresh() {
            //请输入查询日期
            var dateTime= $("#createDate").val();
            //alert(dateTime);
            if(dateTime == null || dateTime == ''){
                layer.msg('请输入查询日期', {icon: 5});
                return;
            }

            //刷新Table，Bootstrap Table 会自动执行重新查询
            $("#table").bootstrapTable('refresh');
        }
    </script>

<script type="text/javascript">
    function doDelete(ids) {
        J.getJSON('${base}/admin/viewer/delete.json', J.param({'id': ids}, true), ajaxReload);
    }

    $(function() {
        // 批量删除
        $('a[data-action="batch_del"]').click(function () {
            var rows = $table.bootstrapTable('getSelections');

            var ids = [];
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].id);
            }

            if (ids.length == 0) {
                layer.msg("请至少选择一项", {icon: 2});
                return false;
            }

            layer.confirm('确定删除此项吗?', {
                btn: ['确定','取消'], //按钮
                shade: false //不显示遮罩
            }, function(){
                doDelete(ids);
            }, function(){
            });
        });

        <#--// 查看-->
        <#--$('a[data-action="viewLink"]').click(function () {-->
        <#--    var that = $(this);-->

        <#--    jQuery.ajax({-->
        <#--        url: '${base}/admin/viewer/get.json',-->
        <#--        data: {'id': that.attr('data-id')},-->
        <#--        dataType: "json",-->
        <#--        type :  "POST",-->
        <#--        cache : false,-->
        <#--        async: false,-->
        <#--        error : function(i, g, h) {-->
        <#--            layer.msg('发送错误', {icon: 2});-->
        <#--        },-->
        <#--        success: function(resp){-->
        <#--            // 返回对象为 NormalResponse-->
        <#--            if(resp){-->
        <#--                if (resp.code== '00000000') {-->
        <#--                    popup(resp.data);-->
        <#--                } else {-->
        <#--                    layer.msg(resp.message, {icon: 5});-->
        <#--                }-->
        <#--            }-->
        <#--        }-->
        <#--    });-->
        <#--});-->
    })

</script>
</@layout>
