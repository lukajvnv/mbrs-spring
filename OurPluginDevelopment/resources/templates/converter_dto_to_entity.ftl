<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_service = class_name + "ServiceImpl">
<#assign class_service_cap = class_name_cap + "ServiceImpl">
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name = class_name_cap/>

package ${class_package};

import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;

import ${model_package}.${class_name_cap};
import ${dto_package}.${class_dto_cap};
<#list properties as property>
  <#list entities as entity>
	<#if entity.name == property.type.name>
	  <#if property.upper == 1>
import ${repository_package}.${entity.name}Repository;
	  <#elseif property.upper == -1>
import ${model_package}.${entity.name};
import ${dto_package}.${entity.name}Dto;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
      <#break>
	  </#if>
	</#if>
  </#list>
</#list>

import ${repository_package}.${class_name_cap}Repository;

@Component
public class ${class_dto_cap}To${class_name_cap}Converter implements Converter<${class_dto_cap},${class_name_cap}> {
	
	<#list properties as property>
  	  <#list entities as entity>
		<#if entity.name == property.type.name>
		  <#if property.upper == 1>
	@Autowired
	private ${entity.name}Repository ${entity.name?uncap_first}Repository;
		  <#elseif property.upper == -1 >
    @Autowired
	private ${entity.name}DtoTo${entity.name}Converter ${entity.name?uncap_first}DtoTo${entity.name}Converter; 
		  </#if>
		</#if>
  	  </#list>
	</#list>
	
	@Autowired
    private ${class_name_cap}Repository ${class_name}Repository;
	
	@Override
	public ${class_name_cap} convert(${class_dto_cap} ${class_dto}) {
		return new ${class_name_cap}(<#list properties as property><#assign isComplexType = false><#list entities as entity><#if entity.name == property.type.name><#if property.upper == 1>${entity.name?uncap_first}Repository.getOne(${class_dto}.get${entity.name}().getId())<#assign isComplexType = true><#elseif property.upper == -1 >set${entity.name}(${class_dto}.get${property.name?cap_first}())<#assign isComplexType = true></#if></#if></#list><#if isComplexType == false>${class_dto}.get${property.name?cap_first}()<#assign isComplexType = false></#if><#if (property?has_next)>,</#if></#list>);
	}
	
	public ${class_name_cap} convert(Integer ${class_name}Id) {
		return ${class_name}Repository.getOne(${class_name}Id);
	}
	
	<#list properties as property>
  	  <#list entities as entity>
  	    <#if entity.name == property.type.name>
  	      <#if property.upper == -1>
  	public Set<${entity.name}> set${entity.name}(List<${entity.name}Dto> ${entity.name?uncap_first}Dto) {
  		Set<${entity.name}> retVal = new HashSet<${entity.name}>();
  		for(${entity.name}Dto ${entity.name?uncap_first} : ${entity.name?uncap_first}Dto) {
  			retVal.add(${entity.name?uncap_first}DtoTo${entity.name}Converter.convert(${entity.name?uncap_first}.getId()));
  		}
  		return retVal;
  	}
  		  </#if>
  	    </#if> 	
  	  </#list>
	</#list>
	
	
}

<@u.block_end />