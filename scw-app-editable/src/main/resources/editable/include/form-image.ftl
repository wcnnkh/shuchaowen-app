<div class="upload-img" name="${field.name}" 
	<#if field.width?? || field.height??>
		style="<#if field.width??>width:${field.width}px;</#if><#if field.height??>height:${field.height}px;</#if>"
	</#if>

	<#if field.multiple>
	data-auto-add = "true"
	</#if>
>	
     <#assign minWidth=0>
	 <#if ((field.width)!0) lt ((field.height)!0)>
	 	<#assign minWidth=((field.width)!0)>
	 <#else>
	 	<#assign minWidth=((field.height)!0)>
	 </#if>
	 <img src="${_request.contextPath}/admin/images/addImg.png" class="add-img" style="<#if imageUrl?? && imageUrl != ''>display:none;</#if><#if minWidth gt 0>width:${minWidth}px;margin-left:${-minWidth/2}px;</#if><#if field.height??>height:${minWidth}px;margin-top:${-minWidth/2}px</#if>"/>
	 <input name="${field.name}" type="file" class="upload-input" data-url="${imageUrl!''}">
	 <#if imageUrl?? && imageUrl != ''>
	 	<img class="show-img" alt="" src="${imageUrl}">
	 <#else>
	 	<img class="show-img" alt="" style="display:none"> 
	 </#if>
	 <img class="delete-img" src="${_request.contextPath}/admin/images/delete.png" style="display: inline-block">
</div>