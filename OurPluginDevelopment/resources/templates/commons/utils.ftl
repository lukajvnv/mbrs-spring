<#macro gen_template_metadata author class_name template_name>
    <#assign info_line_1 = "generated class:  ${class_name}.java, template used : ${template_name}"  >
	<#assign info_line_2 = "author: ${author}, time: ${.now}"  >
/**
    ######################################## BEGIN ###############################################
    ${ info_line_1 }
    ${ info_line_2 }
    ##############################################################################################
*/
</#macro>

<#macro block_end >
/**
    ########################################  END ################################################
*/
</#macro>
<#macro plural_print noun>
	<#compress>
		<#if noun?ends_with("s") >
			${noun}es	
		<#else>
			${noun}s
		</#if>		
	</#compress>
</#macro>

<#function plural noun>
	<#local result = "">
	<#if noun?ends_with("s") >
		<#local result ="${noun}es">	
	<#else>
		<#local result ="${noun}s">	
	</#if>
	<#return result>
</#function>

<#macro print_attributes attributes>
	<#if attributes?has_content>
		<#compress>
			(<#list attributes as attribute>${attribute.name} = <@print_attribute_values values=attribute.values /><#sep>,</#sep></#list>)
		</#compress>
	</#if>
</#macro>

<#macro print_id_annotations props>
 <#if props?has_content>
  <#list props as p>
      ${p.annotationName}<#if (p.name)?has_content>(${p.name} = <@print_attribute_values values=p.values />)</#if>
  </#list>
 </#if>
</#macro>

<#macro print_attribute_values values>
	<#compress>
		<#if (values?size > 1)>{</#if> <#list values as value> ${value}<#sep>,</#sep> </#list> <#if (values?size > 1)>} </#if>
	</#compress>
</#macro>

<#-- filter collections by attribute specified via name and its value -->
<#function filter things name value>
    <#local result = []>
    <#list things as thing>
        <#if thing[name] == value>
            <#local result = result + [thing]>
        </#if>
    </#list>
    <#return result>
</#function>
