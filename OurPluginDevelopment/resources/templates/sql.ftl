<#import "/commons/utils.ftl" as u>
<#function get_atr_name prop>
	<#local atr_name = prop.name>
    <#local filteredPropData = u.filter(prop.persistentProperties, "name", "name") >
	<#if filteredPropData?has_content>
		<#local atr_name = filteredPropData[0]["values"][0]?replace("\"", "") >
	</#if>
    <#return atr_name>
</#function>
<#macro print_value prop index>
 <#compress>
	<#if javaTypes?seq_contains(prop.type.name)>
		<#if prop.type.name == 'String'>
			'${prop.name}${index}'
		<#elseif prop.type.name == 'Date'>
			'2020-05-05'
		<#elseif prop.type.name == 'Integer'>
			${index}<#-- todo: float -->
		<#elseif prop.type.name == 'boolean' || prop.type.name="Boolean">
			true
		</#if>
	<#elseif enumTypes?seq_contains(prop.type.name)>	
	<#--  '${enumValues[property.type.name]}'-->0
	<#else>
		${index}
	</#if>
 </#compress>
</#macro>
<#list entities as entity>
	<#assign entity_name = entity.name?uncap_first>
	<#assign filteredData = u.filter(entity.persistentProperties, "name", "name") >
	<#if filteredData?has_content>
		<#assign entity_name = filteredData[0]["values"][0]?replace("\"", "") >
	</#if>
	<#list 1..5 as entity_num>
insert into ${entity_name} (<#list entity.properties?filter(prop -> prop.upper != -1) as property><#assign attribute_name = get_atr_name(property)><#if javaTypes?seq_contains(property.type.name) || enumTypes?seq_contains(property.type.name)>${attribute_name}<#else>${attribute_name}_id</#if><#sep>, </#sep></#list>) values (<#list entity.properties?filter(prop -> prop.upper != -1) as property><@print_value prop = property index = entity_num /><#sep>, </#sep></#list>);
	</#list>
	
</#list>