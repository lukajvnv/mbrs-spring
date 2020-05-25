<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_service = class_name + "ServiceImpl">
<#assign class_service_cap = class_name_cap + "ServiceImpl">
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name = class_name_cap/>

package ${class_package};

import org.springframework.stereotype.Service;

import ${repository_package}.${class_name_cap}Repository;
import ${converter_package}.${class_name_cap}To${class_dto_cap}Converter;
import ${converter_package}.${class_dto_cap}To${class_name_cap}Converter;

@Service
public class ${class_name_cap}ImplService extends ${class_name_cap}AbstractService { 

	
	public ${class_name_cap}ImplService(${class_name_cap}Repository ${class_name}Repository,${class_name_cap}To${class_dto_cap}Converter ${class_name}To${class_dto_cap}Converter, ${class_dto_cap}To${class_name_cap}Converter ${class_dto}To${class_name_cap}Converter) {
		super(${class_name}Repository,${class_name}To${class_dto_cap}Converter,${class_dto}To${class_name_cap}Converter);
	}
}


<@u.block_end />