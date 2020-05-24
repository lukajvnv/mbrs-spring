<#import "/commons/utils.ftl" as u>
<#list entities as entity>
	<#assign entity_name = entity.name?uncap_first>
	<#assign filteredData = u.filter(entity.persistentProperties, "name", "name") >
	<#if filteredData?has_content>
		<#assign entity_name = filteredData[0]["values"][0]?replace("\"", "") >
	</#if>
	<#list 1..5 as entity_num>
insert into ${entity_name}<#assign shouldComma = false > (<#list entity.properties as property><#if property.upper != -1><#if property?index gt 0 && shouldComma>, </#if><#assign attribute_name = property.name><#if javaTypes?seq_contains(property.type.name) || enumTypes?seq_contains(property.type.name)>${property.name}<#else>${property.name}_id</#if><#assign shouldComma = true ></#if></#list>)<#assign shouldComma = false > values (<#list entity.properties as property><#if property.upper != -1><#if property?index gt 0 && shouldComma>, </#if><#assign attribute_name = property.name><#if javaTypes?seq_contains(property.type.name)><#if property.type.name == 'String'>'${property.name}${entity_num}'<#elseif property.type.name == 'Date'>'2020-05-05'<#elseif property.type.name == 'Integer'>${entity_num}<#-- todo: float --><#elseif property.type.name == 'boolean' || property.type.name="Boolean">true</#if><#elseif enumTypes?seq_contains(property.type.name)><#--  '${enumValues[property.type.name]}'-->0<#else>${entity_num}</#if><#assign shouldComma = true ></#if></#list>);
	</#list>
	
</#list>