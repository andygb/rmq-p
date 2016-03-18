
<!--中文testaaa-->
<!DOCTYPE html>
<html ng-app="app">

<head>
<#assign user=((data.user)!{})>
<#assign loginUrl=((data.loginUrl)!'/')>
<#assign appName=((data.appName)!'')>
<#assign authMenuList=((data.authMenuList)![])>
<#assign clientVersion=((clientVersion)!'')>



    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Page title set in pageTitle directive -->
    <title page-title></title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css?v=${clientVersion}" rel="stylesheet">

    <!-- Font awesome -->
    <link href="font-awesome/css/font-awesome.css?v=${clientVersion}" rel="stylesheet">

    <!-- Toastr style -->
    <link href="css/plugins/toastr/toastr.min.css?v=${clientVersion}" rel="stylesheet">

    <!-- Main Inspinia CSS files -->
    <link href="css/animate.css?v=${clientVersion}" rel="stylesheet">
    <link id="loadBefore" href="css/style.css?v=${clientVersion}" rel="stylesheet">

    <!-- Data Tables -->
    <link href="css/plugins/dataTables/dataTables.bootstrap.css?v=${clientVersion}" rel="stylesheet">
    <link href="css/plugins/dataTables/dataTables.responsive.css?v=${clientVersion}" rel="stylesheet">
    <link href="css/plugins/dataTables/dataTables.tableTools.min.css?v=${clientVersion}" rel="stylesheet">

    <!-- Admin Related CSS files -->
    <link href="css/sites/common.css?v=${clientVersion}" rel="stylesheet">
<#--<link href="css/sites/navigation.css" rel="stylesheet">-->
    <link href="css/sites/user.css?v=${clientVersion}" rel="stylesheet">

    <link href="css/remark.css?v=${clientVersion}" rel="stylesheet">
</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body class="{{$state.current.data.specialClass}}" landing-scrollspy id="page-top" >

<!-- Toaster Container -->
<toaster-container toaster-options="{'animation-class': 'slideDown'}"></toaster-container>

<!-- Main view  -->
<div ui-view></div>

<!-- jQuery and Bootstrap -->
<script src="js/jquery/jquery-2.1.1.min.js?v=${clientVersion}"></script>
<script src="js/plugins/jquery-ui/jquery-ui.js?v=${clientVersion}"></script>
<script src="js/bootstrap/bootstrap.min.js?v=${clientVersion}"></script>

<!-- MetsiMenu -->
<script src="js/plugins/metisMenu/jquery.metisMenu.js?v=${clientVersion}"></script>

<!-- SlimScroll -->
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js?v=${clientVersion}"></script>

<!-- Peace JS -->
<script src="js/plugins/pace/pace.min.js?v=${clientVersion}"></script>

<!-- Custom and plugin javascript -->
<script src="js/admin.js?v=${clientVersion}"></script>
<script src="js/utils.js?v=${clientVersion}"></script>

<script type="text/javascript">

    window.LG = {};
    LG.me = {"userName": "${user.userName!""}", "trueName": "${user.name!""}", "userId": "${user.userId!0}","roleName":"${roleName!""}","usertoken":"${token!""}"};
    LG.authMenuList=${authMenuList}
            LG.appConfig = {
                "loginUrl": "${loginUrl}",
                "appName":"${appName}"
            };
</script>

<!-- Main Angular scripts-->
<script src="js/angular/angular.js?v=${clientVersion}"></script>
<script src="js/angular/angular-cookies.js?v=${clientVersion}"></script>
<script src="js/angular/angular-sanitize.min.js?v=${clientVersion}"></script>
<script src="js/angular/i18n/angular-locale_zh-cn.js?v=${clientVersion}"></script>
<script src="js/plugins/oclazyload/dist/ocLazyLoad.min.js?v=${clientVersion}"></script>
<script src="js/angular-translate/angular-translate.min.js?v=${clientVersion}"></script>
<script src="js/ui-router/angular-ui-router.min.js?v=${clientVersion}"></script>
<script src="js/bootstrap/ui-bootstrap-tpls-0.12.0.min.js?v=${clientVersion}"></script>
<script src="js/plugins/angular-idle/angular-idle.js?v=${clientVersion}"></script>

<!-- Data Tables -->
<script src="js/plugins/dataTables/jquery.dataTables.js?v=${clientVersion}"></script>
<script src="js/plugins/dataTables/dataTables.bootstrap.js?v=${clientVersion}"></script>
<script src="js/plugins/dataTables/dataTables.responsive.js?v=${clientVersion}"></script>
<script src="js/plugins/dataTables/dataTables.tableTools.min.js?v=${clientVersion}"></script>
<script src="js/plugins/dataTables/angular-datatables.js?v=${clientVersion}"></script>

<!-- toastr -->
<script src="js/plugins/toastr/toastr.min.js?v=${clientVersion}"></script>

<!-- Admin Script -->
<script src="app/app.core.js?v=${clientVersion}"></script>

<script src="app/topic-mgmt/topic-mgmt.module.js"></script>
<script src="app/topic-mgmt/topic-mgmt.service.js"></script>
<script src="app/topic-mgmt/topic-mgmt.controller.js"></script>

<script src="app/record/record.module.js"></script>
<script src="app/record/record.service.js"></script>
<script src="app/record/record.controller.js"></script>


<script src="app/app.module.js?v=${clientVersion}"></script>

<script src="app/constants.js?v=${clientVersion}"></script>

<script src="app/components/old-site-link.directive.js?v=${clientVersion}"></script>
<script src="app/components/page-title.directive.js?v=${clientVersion}"></script>
<script src="app/components/minimaliza-sidebar.directive.js?v=${clientVersion}"></script>
<script src="app/components/side-navigation.directive.js?v=${clientVersion}"></script>
<script src="app/components/range.filter.js?v=${clientVersion}"></script>

<script src="js/translations.js?v=${clientVersion}"></script>
<script src="app/filters/http.interceptor.js?v=${clientVersion}"></script>
<script src="app/filters/http.filter.js?v=${clientVersion}"></script>
<script src="app/app.config.js?v=${clientVersion}"></script>

</body>
</html>
