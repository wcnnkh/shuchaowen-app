<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh">
<head>
	<#include "include/head.ftl">
</head>
<body class="layui-anim layui-anim-up">
<div class="x-nav">
	<a class="layui-btn layui-btn-small"
	   style="line-height: 1.6em; margin-top: 3px; float: right"
	   href="javascript:location.replace(location.href);" title="刷新"> <i
			class="layui-icon" style="line-height: 30px">ဂ</i></a>
</div>

<div class="x-body">
	<div class="layui-row">
		<form class="layui-form layui-col-md12 x-so">
			<#list fields as field>
				${field.describe}
				<#if (field.options)>
					<div class="layui-input-inline">
						<select name="${field.name}">
							<#list field.options as option>
								<option value="${option.value }">${option.text}</option>
							</#list>
						</select>
					</div>
				</#if>
				<input type="text" name="${field.name}"
									   placeholder="请输入${field.describe}" autocomplete="off"
									   class="layui-input">
				&nbsp;&nbsp
			</#list>
			<button class="layui-btn" lay-submit="" lay-filter="sreach">
				<i class="layui-icon">&#xe615;</i>
			</button>
		</form>
	</div>
	<button class="layui-btn" onclick="x_admin_show('添加${name}','add')">
		<i class="layui-icon"></i>添加
	</button>
	<span class="x-right" style="line-height: 40px">页码[${page }/${maxPage }]&nbsp;&nbsp;共有数据：${totalCount }
			条</span>
	<table class="layui-table">
		<thead>
			<tr>
				<#list fields as field>
					<th>${field.describe}</th>
				</#list>
			</tr>
		</thead>
		<tbody>
		<#list list as item>
			<tr>
				<#list fields as field>
					<td>${item[field.name]}</td>
				</#list>
				<td class="td-manage">
				<a title="删除"
										 onclick="x_admin_show('删除','delete?<#list fields as f><#if f.primaryKey>${f.name}=${item[field.name]}&</#if></#list>')"
										 href="javascript:;"> <i class="layui-icon">&#xe63c;</i>
				</a>
				&nbsp;&nbsp;
				<a title="查看/修改"
										 onclick="x_admin_show('查看/修改','info?<#list fields as f><#if f.primaryKey>${f.name}=${item[field.name]}&</#if></#list>')"
										 href="javascript:;"> <i class="layui-icon">&#xe63c;</i>
				</a>
				</td>
			</tr>
		</#list>
		</tbody>
	</table>
	<#include "/admin/ftl/include/pagination.ftl" />
</div>
</body>
</html>
