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
	  <#if property.upper == -1>
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import ${model_package}.${entity.name};
import ${dto_package}.${entity.name}Dto;
	  <#break>
      </#if>
    </#if>
  </#list>
</#list>
@Component
public class ${class_name_cap}To${class_dto_cap}Converter implements Converter<${class_name_cap},${class_dto_cap}> {
	
	<#list properties as property>
  	  <#list entities as entity>
		<#if entity.name == property.type.name>
    @Autowired
	private ${entity.name}To${entity.name}DtoConverter ${property.name}To${entity.name}DtoConverter;
		</#if>
  	  </#list>
	</#list>
	
	
	@Override
	public ${class_dto_cap} convert(${class_name_cap} ${class_name}) {
		return new ${class_dto_cap}(<#list properties as property>
									<#assign isComplexType = false>
									<#list entities as entity>
									<#if entity.name == property.type.name>
									<#if property.upper == 1>
									${property.name}To${entity.name}DtoConverter.convert(${class_name}.get${entity.name}())
									<#assign isComplexType = true>
									<#elseif property.upper == -1 >
									list${entity.name}Dto(${class_name}.get${property.name?cap_first}())
									<#assign isComplexType = true>
									</#if>
									</#if>
									</#list>
									<#if isComplexType == false>
									${class_name}.get${property.name?cap_first}()
									</#if><#if (property?has_next)>,</#if></#list>);
	}
	
	<#list properties as property>
  	  <#list entities as entity>
  	    <#if entity.name == property.type.name>
  	      <#if property.upper == -1>
  	public List<${entity.name}Dto> list${entity.name}Dto(Set<${entity.name}> set${entity.name}) {
  		List<${entity.name}Dto> retVal = new ArrayList<${entity.name}Dto>();
  		for(${entity.name} ${entity.name?uncap_first} : set${entity.name}) {
  			retVal.add(${property.name}To${entity.name}DtoConverter.convert(${entity.name?uncap_first}));
  		}
  		return retVal;
  	}
  		  </#if>
  	    </#if> 	
  	  </#list>
	</#list>
}

<@u.block_end />