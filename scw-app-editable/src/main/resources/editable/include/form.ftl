<#list fields as field>
	<div class="layui-form-item">
		<label for="name" class="layui-form-label">
			<#if (field.requried)!false>
				<span class="x-red">*</span>
			</#if>
			${field.describe}
		</label>
		<div class="layui-input-inline">
			<#if field.options??>
				<select name="${field.name}">
					<#list field.options as option>
						<option value="${option.value}">${option.text}</option>
					</#list>
				</select>
			<#else>
				<input type="text" name="${field.name}" required="" value=""
				   <#if (field.required)!false>lay-verify="required"</#if> autocomplete="off" class="layui-input"/>
			</#if>
		</div>
	</div>
</#list>
