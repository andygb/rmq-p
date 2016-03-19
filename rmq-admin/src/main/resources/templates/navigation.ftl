<#assign appName=((data.data.appName)!'')>
<#assign authMenuList=((data.data.authMenuList)![])>


<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav metismenu">
            <li class="nav-header">
                <div class="dropdown profile-element" dropdown>
                    <img alt="image" class="img-circle" src="img/profile_small.jpg"/>
                    <a class="dropdown-toggle" dropdown-toggle href>
                        <span class="clear">
                            <span class="block m-t-xs">
                                <strong class="font-bold">{{user.trueName}} ({{user.roleName}})</strong>
                            </span>
                        </span>
                    </a>
                    <!-- <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a href="#">登出</a></li>
                    </ul> -->
                </div>
                <div class="logo-element">
                    <a href="#">${appName}</a>
                </div>
            </li>
        </ul>


        <ul side-navigation class="nav metismenu" id="side-menu">

            <#list authMenuList as menu>
                <li ng-class="{active: $state.includes('${menu.menuurl}')}">
                    <a href=""><i class="${menu.menuicon}"></i> <span class="nav-label">${menu.menuname}</span><span
                            class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('${menu.menuurl}')}">
                        <#list menu.subMenuList as submenu>
                            <li ui-sref-active="active"><a ui-sref="${menu.menuurl}.${submenu.menuurl}">${submenu.menuname}<span
                                    class="label label-info pull-right">NEW</span></a>
                            </li>
                        </#list>
                    </ul>
                </li>
            </#list>
        </ul>

        <#--<ul side-navigation class="nav metismenu" id="side-menu">-->

            <#--<li ui-sref-active="active" ng-class="{in: $state.includes('topic-mgmt.query')}"><a ui-sref="topic-mgmt.query">Topic管理</a></li>-->

            <#--<li ui-sref-active="active" ng-class="{in: $state.includes('record.query')}"><a ui-sref="record.query">消息记录</a></li>-->

        <#--</ul>-->
        <!--<ul side-navigation class="nav metismenu" id="side-menu">-->

        <!--<li ng-class="{active: $state.includes('dic-mgmt')}">-->
        <!--<a href=""><i class="fa fa-cog"></i> <span class="nav-label">基础设置</span><span class="fa arrow"></span></a>-->
        <!--<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('dic-mgmt')}">-->
        <!--<li ui-sref-active="active"><a ui-sref="dic-mgmt.dic-query">字典管理<span class="label label-info pull-right">NEW</span></a></li>-->

        <!--</ul>-->
        <!--</li>-->
        <!--<li ng-class="{active: $state.includes('user-mgmt')}">-->
        <!--<a href=""><i class="fa fa-user"></i> <span class="nav-label">会员管理</span><span class="fa arrow"></span></a>-->
        <!--<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('user-mgmt')}">-->
        <!--<li ui-sref-active="active"><a ui-sref="user-mgmt.user-level-mgmt">用户等级管理<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--<li ui-sref-active="active"><a ui-sref="user-mgmt.shop-level-mgmt">店铺等级管理<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--</ul>-->
        <!--</li>-->
        <!--<li ng-class="{active: $state.includes('trade-mgmt')}">-->
        <!--<a href=""><i class="fa fa-money"></i> <span class="nav-label">订单管理</span><span class="fa arrow"></span></a>-->
        <!--<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('trade-mgmt')}">-->
        <!--<li ui-sref-active="active"><a ui-sref="trade-mgmt.trade-query">订单查询<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--<li ui-sref-active="active"><a ui-sref="trade-mgmt.trade-item-query">订单商品<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--<li ui-sref-active="active"><a ui-sref="trade-mgmt.trade-period-query">分期订单<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--</ul>-->
        <!--</li>-->
        <!--<li ng-class="{active: $state.includes('coupon-mgmt')}">-->
        <!--<a href=""><i class="fa fa-money"></i> <span class="nav-label">优惠券管理</span><span class="fa arrow"></span></a>-->
        <!--<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('coupon-mgmt')}">-->
        <!--<li ui-sref-active="active"><a ui-sref="coupon-mgmt.coupon-query">优惠券查询<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--<li ui-sref-active="active"><a ui-sref="coupon-mgmt.coupon-userecord-query">优惠券使用记录<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--</ul>-->
        <!--</li>-->
        <!--<li ng-class="{active: $state.includes('message-mgmt')}">-->
        <!--<a href=""><i class="fa fa-envelope-o"></i> <span class="nav-label">消息管理</span><span class="fa arrow"></span></a>-->
        <!--<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('message-mgmt')}">-->
        <!--<li ui-sref-active="active"><a ui-sref="message-mgmt.sms-send">短信发送<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--</ul>-->
        <!--</li>-->
        <!--<li ng-class="{active: $state.includes('shop-mgmt')}">-->
        <!--<a href=""><i class="fa fa-cube"></i> <span class="nav-label">店铺管理</span><span class="fa arrow"></span></a>-->
        <!--<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('shop-mgmt')}">-->
        <!--<li ui-sref-active="active"><a ui-sref="shop-mgmt.shop-query">店铺审核<span class="label label-info pull-right">NEW</span></a></li>-->
        <!--</ul>-->
        <!--</li>-->
        <!--</ul>-->
    </div>
</nav>