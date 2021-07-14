<#list fields as field>
	<div class="layui-form-item">
		<label for="name" class="layui-form-label">
			<#if (field.requried)!false>
				<span class="x-red">*</span>
			</#if>
			${field.describe}
		</label>
		<#if field.type == "IMAGE">
			<div class="layui-input-block">
		<#else>
			<div class="layui-input-inline">
		</#if>
			<#if field.type == "SELECT">
				<#include "/editable/include/form-select.ftl">
			<#elseif field.type == "IMAGE">
				<#if (info[field.name])?? && (info[field.name]) != ''>
					<#list info[field.name]?split(",") as imageUrl>
						<#include "/editable/include/form-image.ftl">
					</#list>
					<#if field.multiple>
						<#include "/editable/include/form-image.ftl">
					</#if>
				<#else>
					 <#include "/editable/include/form-image.ftl">
				</#if>
			<#else>
				<#assign readonly=(info[field.name])?? && field.primaryKey>
				<#include "/editable/include/form-input.ftl">
			</#if>
		</div>
	</div>
</#list>
<script>
	function uploadFormImages(successCallback){
		uploadImagesByPolicy("${_request.contextPath}/admin/generate_upload_policy", $("input.upload-input"), successCallback);
	}
</script>