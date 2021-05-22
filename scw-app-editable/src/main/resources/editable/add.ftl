<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<#include "/admin/ftl/include/head.ftl">
</head>
<body>
<div class="x-body">
	<form class="layui-form">
		<#include "/editable/include/form.ftl">
		<div class="layui-form-item">
			<label for="L_repass" class="layui-form-label"> </label>
			<button class="layui-btn" lay-filter="add" lay-submit="">保存</button>
		</div>
	</form>
</div>
<script>
	layui.use(['form','layer'], function(){
		$ = layui.jquery;
		var form = layui.form
				,layer = layui.layer;

		form.on('submit(add)', function(data){
			var requestData = data.field;
			requestData.toUid = "${(admin.uid)!}";
			$.ajax({
				"url": "admin_create_or_update",
				"method":"POST",
				"dataType":"json",
				"data": requestData,
				success:function(response){
					if(response.code != 0){
						layer.alert(response.msg, {icon: 5});
					}else{
						layer.alert("操作成功", {icon: 6},function () {
							parent.location.reload();
						});
					}
				},
				error:function(){
					layer.msg("网络或系统错误，请稍后重试", {icon: 5});
				}
			})
			return false;
		});


	});
</script>
</body>
</html>