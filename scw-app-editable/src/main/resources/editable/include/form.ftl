<#list fields as field>
	<div class="layui-form-item">
		<label for="status" class="layui-form-label"><#if (field.requried)!false><span class="x-red">*</span></#if>${field.describe}</lable>
		<div class="layui-input-inline">
			<#if field.options??>
				<select name="${field.name}">
					<#list field.options as option>
						<option value="${option.value}">${option.text}</option>
					</#list>
				</select>
			<#else>
				<input type="text" name="${field.name}" required="" value=""
					   lay-verify="required" autocomplete="off" class="layui-input">
			</#if>>
		</div>
	</div>
</#list>
