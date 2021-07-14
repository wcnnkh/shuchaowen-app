<select name="${field.name}" <#if (readonly!false)>disabled="disabled"</#if>>
	<option value="">未选择</option>
	<#if (field.options)??>
		<#list field.options as option>
			<option value="${option.key}" <#if (info[field.name])?? && (info[field.name])?c == option.key>selected="selected"</#if>>${option.value}</option>
		</#list>
	</#if>
</select>