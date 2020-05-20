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

import ${model_package}.${class_name_cap};
import ${dto_package}.${class_dto_cap};

@Component
public class ${class_name_cap}To${class_dto_cap}Converter implements Converter<${class_name_cap},${class_dto_cap}> {
	
	<#list properties as property>
  	  <#list entities as entity>
		<#if entity.name == property.name?cap_first>
	@Autowired
	private ${entity.name}ToDtoConverter ${property.name}ToDtoConverter;
		</#if>
  	  </#list>
	</#list>
	
	
	@Override
	public ${class_dto_cap} convert(${class_name_cap} ${class_name}) {
		return new ${class_dto_cap}(<#list properties as property><#assign isComplexType = false><#list entities as entity><#if entity.name == property.name?cap_first>${property.name}ToDtoConverter.convert(${class_name}.get${property.name?cap_first}())<#assign isComplexType = true></#if></#list><#if isComplexType == false>${class_name}.get${property.name?cap_first}()</#if><#if (property?has_next)>,</#if></#list>);
	}
}

<@u.block_end />