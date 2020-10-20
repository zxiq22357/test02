<%--
  Created by IntelliJ IDEA.
  User: zxiq
  Date: 2020/9/12
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script type="text/javascript">

        $.ajax({
            url:"",
            data:{

            },
            type:"",
            dataType:"json",
            success:function (data) {

            }
        })

            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });
    </script>
</head>
<body>



</body>
</html>
