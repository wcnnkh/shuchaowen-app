<#list fields as field>
	<div class="layui-form-item">
		<label for="name" class="layui-form-label">
			<#if (field.requried)!false>
				<span class="x-red">*</span>
			</#if>
			${field.describe}
		</label>
		<div class="layui-input-inline">
			<#if field.inputType == "SELECT" && field.options??>
				<select name="${field.name}">
					<#list field.options as option>
						<option value="${option.value}" <#if ((info[field.name])!'')==option.value>selected="selected"</#if>>${option.text}</option>
					</#list>
				</select>
			<#elseif field.inputType == "IMAGE">
				<div class="upload-img" name="${field.name}">
					 <img src="${_request.contextPath}/admin/images/addImg.png" class="add-img" /> 
					 <input name="file" type="file" class="upload-input" data-url="">
					 <img class="show-img" alt=""> 
					 <img class="delete-img" src="${_request.contextPath}/admin/images/delete.png" style="display: inline-block">
				</div>
			<#elseif field.inputType == "IMAGES">
				<div class="upload-img" name="${field.name}" data-auto-add = "true">
					 <img src="${_request.contextPath}/admin/images/addImg.png" class="add-img" /> 
					 <input name="file" type="file" class="upload-input" data-url="">
					 <img class="show-img" alt="" style="display:none"> 
					 <img class="delete-img" src="${_request.contextPath}/admin/images/delete.png" style="display: inline-block">
				</div>
			<#else>
				<input type="text" name="${field.name}" required="" value="${(info[field.name])!''}" 
				   <#if (info[field.name])?? && field.primaryKey>readonly="readonly"</#if>
				   <#if (field.required)!false>lay-verify="required"</#if> 
				   autocomplete="off" class="layui-input"/>
			</#if>
		</div>
	</div>
</#list>
