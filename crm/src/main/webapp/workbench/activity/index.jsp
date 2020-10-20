<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){

	    //为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			/*
            * 操作模态窗口的方式
            *   需要操作的模态窗口的jquery对象，调用model方法，为该方法传递参数 show:打开模态窗口 hide:关闭模态窗口
            * */
		//

        //走后台，目的是为了取得用户信息列表，为所有者下拉框铺值
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data) {
					var html = "<option></option>";
					$.each(data,function (i,j) {
						html += "<option value='" + j.id+ "'>"+ j.name +"</option>";
					})
					$("#create-owner").html(html);

					//从session中获取id的值，使模态窗口的用户名为登入用户的用户名
					//在js中用el表达式必须用""括起来才有效
					var id = "${user.id}";
					$("#create-owner").val(id);
					$("#createActivityModal").modal("show");
				}
			})
		})

		//为保存按钮添加事件，将数据保存在表中
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.success){
						//清空模态窗口的数据
						/*
						* 注意:
						* 	我们拿到了form表单的jquery对象
						* 	对于表单的jquery对象来说，提供了submit()方法提交表单
						* 	但没有提供reset()方法重置表单(坑:IDEA为我们提示了方法)
						*
						* 	但是原生js提供了reset()方法，所以要将jquery对象转换为原生js对象，即dom对象
						*
						* 	jquery转换为dom对象:
						* 		jquery对象[下标]
						* 	dom对象转换为jquery对象:
						* 		$(dom)
						* */

						/*
						* $("#activityPage").bs_pagination('getOption', 'currentPage'):
						* 		操作后停留在当前页
						*
						* $("#activityPage").bs_pagination('getOption', 'rowsPerPage'):
						* 		操作后维持已经设置好的每页展现的记录数
						*
						* 这两个参数不需要我们进行任何的修改操作
						* 	直接使用即可
						*
						* */

						//做完添加操作后，应该回到第一页，维持每页展现的记录数

						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						$("#activityAddForm")[0].reset();

						//关闭模态窗口
						$("#createActivityModal").modal("hide");
					}else {
						alert("添加市场活动信息失败!!!")
					}
				}
			})
		})

		//页面加载完毕后触发一个方法
		//默认展示第一页，每页展示两个记录
		pageList(1,2);

		//点击查询按钮，然后局部刷新信息
		$("#searchBtn").click(function () {
		    /*
		    * 点击搜索按钮时，应该先将搜索信息储存起来，以免未点击查询时，分页插件自动查询
		    * */

		    $("#hidden-name").val($.trim($("#search-name").val()));
		    $("#hidden-owner").val($.trim($("#search-owner").val()));
		    $("#hidden-startDate").val($.trim($("#search-startDate").val()));
		    $("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);
		})

        //点击全选框时，全部选中
        $("#qx").click(function () {
            $("input[name=xz]").prop("checked",this.checked);
        })

        //以下这种方法是不对的
        $("input[name=xz]").click(function () {

        })

        //因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作的

        /*
        * 动态生成的元素，我们要以on方法的形式来触发事件
        *
        * 语法:
        *   $(需要绑定动态元素的有效的普通外层元素).on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数)
        * */

        $("#search-activity").on("click",$("input[name=xz]"),function () {
            $("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
        })

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#delectBtn").click(function () {
			//找到复选框中所有选中的复选框jquery对象
			var $xz = $("input[name=xz]:checked");

			if ($xz.length==0){
				alert("请选择需要删除的数据!");
			}else {
				if (confirm("确定删除选中数据吗?")){
					//alert("123")
					//json不能再ajax的data中有重复的属性名，所以只能用拼接的方式
					//url:workbench/activity/delete.do?id=xxx&id=xxx...
					//拼接参数
					var param = "";

					//将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录id
					for (var i=0 ; i < $xz.length ; i++){
						param += "id=" + $($xz[i]).val();

						//如果不是最后的id，则两者之间需要加&连接
						if (i < $xz.length-1){
							param += "&";
						}
					}

					//alert(param);
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function (data) {
							/*
                            * 只要知道删除成功与否即可
                            * */
							if (data.success){
								alert("删除成功");
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							}else {
								alert("删除失败");
							}
						}

					})
				}
			}
		})

		//修改用户信息数据
		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if ($xz.length == 0){
				alert("请选择需要修改的数据")
			}else if ($xz.length > 1){
				alert("只能修改一条数据")
			}else {
				var id = $xz.val();

				//处理所有者下拉框
				$.ajax({
					url:"workbench/activity/getUser.do",
					data:{
						"id" : id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
                        /*
                        * 需要从后台获取两个信息
                        * "uList":[{用户1},{用户2},..],"a":[{市场活动}]
                        * */

                        var html = "<option></option>";

                        $.each(data.uList,function (i,n) {
                            html += "<option value='"+ n.id +"'>" + n.name + "</option>"
                        })

                        $("#edit-owner").html(html);

                        //处理单条activity
                        $("#edit-id").val(data.a.id);
                        $("#edit-owner").val(data.a.owner);
                        $("#edit-name").val(data.a.name);
                        $("#edit-startDate").val(data.a.startDate);
                        $("#edit-endDate").val(data.a.endDate);
                        $("#edit-cost").val(data.a.cost);
                        $("#edit-description").val(data.a.description);

					}
				})

				$("#editActivityModal").modal("show");

				//对修改的数据进行更新操作
				$("#updateBtn").click(function () {
					//修改操作一般都是复制添加操作的代码，因为原理相同
					$.ajax({
						url:"workbench/activity/update.do",
						data:{
							"id" : $.trim($("#edit-id").val()),
							"owner" : $.trim($("#edit-owner").val()),
							"name" : $.trim($("#edit-name").val()),
							"startDate" : $.trim($("#edit-startDate").val()),
							"endDate" : $.trim($("#edit-endDate").val()),
							"cost" : $.trim($("#edit-cost").val()),
							"description" : $.trim($("#edit-description").val())
						},
						type:"post",
						dataType:"json",
						success:function (data) {
							if (data.success){
								//pageList(1,2);
								//修改操作后，页面应维持在当前页，维持每页展现的记录数
								pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

								//关闭模态窗口
								$("#editActivityModal").modal("hide");
							}else {
								alert("修改市场活动信息失败!!!")
							}
						}
					})
				})
			}
		})
	});

	/*
	* 关于所有的关系型数据库，做前端的分页相关操作的基础组件
	* 就是pageNo和pageSize
	* pageNo:页码
	* pageSize:每页展现的记录数
	*
	* pageList方法:就是发出ajax请求到后台，从后台取得最新的市场活动信息列表数据
	* 				通过响应回来的数据，局部刷新市场活动信息列表
	*
	* 我们都在哪些情况下，需要调用pageList方法(什么情况下需要刷新一下市场活动列表)
	* (1)点击左侧菜单中的"市场活动"超链接，需要刷新市场活动列表，调用pageList方法
	* (2)添加，修改，删除后，需要刷新市场活动列表，调用pageList方法
	* (3)点击查询按钮的时候，需要刷新市场活动列表，调用pageList方法
	* (4)点击分页组件的时候，调用pageList方法
	*
	* 以上为pageList方法制定了六个入口，也就是说，在以上6个操作执行完毕后，我们必须要调用pageList方法，刷新市场活动信息列表
	* */
	function pageList(pageNo,pageSize) {
		//将复选框的全选关闭
		$("#qx").prop("checked",false);

        $("#search-name").val($.trim($("#hidden-name").val()));
        $("#search-owner").val($.trim($("#hidden-owner").val()));
        $("#search-startDate").val($.trim($("#hidden-startDate").val()));
        $("#search-endDate").val($.trim($("#hidden-endDate").val()));

        $.ajax({
			url:"workbench/activity/pageList.do",
			data: {
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				//动态拼接数据，如果查询的信息为空，可以通过动态sql去除查询条件
				//data中的数据是json数组，存的是{"total":100,"dataList":[{市场活动1},{2}...]}
				//total表示查询出来的总记录数，需用到分页插件
				var html = "";

				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="' + n.id +'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id=' + n.id + '\';">' + n.name +'</a></td>';
					html += '<td>' + n.owner + '</td>';
					html += '<td>' + n.startDate + '</td>';
					html += '<td>' + n.endDate + '</td>';
					html += '</tr>';
				})

				$("#search-activity").html(html);

				//数据查询完毕后，通过分页插件，对前端展现分页信息

				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});


			}
		})
	}
	
</script>
</head>
<body>

    <!--添加隐藏域，保存查询的信息-->
    <input type="hidden" id="hidden-name">
    <input type="hidden" id="hidden-owner">
    <input type="hidden" id="hidden-startDate">
    <input type="hidden" id="hidden-endDate">

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" value="发传单">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--
									关于文本域textarea:
										(1)一定是要以标签对的形式来呈现，正常状态下标签对要紧紧的挨着
										(2)textarea虽然是以标签对的形式来呈现的，但是它也是属于表单元素范畴
										我们所有的对于textatea的取值和赋值操作，应该统一使用val()方法(而不是html()方法)
								-->
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityAddForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--
						data-dismiss="modal"
						关闭模态窗口
					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>



	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
						点击创建按钮，观察两个属性和属性值

						data-toggle="modal":
							表示触发该按钮，将要打开一个模态窗口

						data-target="#createActivityModal":
							表示要打开哪个模态窗口，通过#id的形式找到该窗口

						现在我们是以属性和属性值的方式写在了button元素中，用来打开模态窗口
						但是这样做是有问题的:
							问题在于没有办法对按钮的功能进行扩充

						所以未来的实际项目开发，对于触发模态窗口的操作，一定不要写死在元素当中，
						应该由我们自己写js代码来操作

					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="delectBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="search-activity">

					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>