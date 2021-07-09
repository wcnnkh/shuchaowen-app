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
				<select name="${field.name}">
					<#if (field.options)??>
						<#list field.options as option>
							<option value="${option.key}" <#if ((info[field.name])!'')==option.value>selected="selected"</#if>>${option.text}</option>
						</#list>
					</#if>
				</select>
			<#elseif field.type == "IMAGE">
				<div class="upload-img" name="${field.name}" 
					<#if field.multiple>
					data-auto-add = "true"
					</#if>
				>
					
					<#if (info[field.name])??>
						<#list info[field.name]?split(",") as url>
							 <img src="${_request.contextPath}/admin/images/addImg.png" class="add-img" /> 
							 <input name="file" type="file" class="upload-input" data-url="${url}">
							 <img class="show-img" alt="" url="${url}"> 
							 <img class="delete-img" src="${_request.contextPath}/admin/images/delete.png" style="display: inline-block">
						</#list>
					<#else>
						 <img src="${_request.contextPath}/admin/images/addImg.png" class="add-img" /> 
						 <input name="file" type="file" class="upload-input" data-url="">
						 <img class="show-img" alt="" style="display:none"> 
						 <img class="delete-img" src="${_request.contextPath}/admin/images/delete.png" style="display: inline-block">
					</#if>
				</div>
			<#else>
				<input type="text" name="${field.name}" required="" value="${(info[field.name])!''}" 
				   <#if (info[field.name])?? && field.primaryKey>readonly="readonly"</#if>
				   <#if (field.required)!false>lay-verify="required"</#if> 
				   autocomplete="off" class="layui-input" placeholder="请输入${field.describe}" />
			</#if>
		</div>
	</div>
</#list>
