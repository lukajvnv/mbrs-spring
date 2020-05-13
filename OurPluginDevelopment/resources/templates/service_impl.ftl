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

@Service
public class ${class_name_cap}ServiceImpl extends ${class_name_cap}ServiceAbstract { 

	public ${class_name_cap}ServiceImpl {
	
	}
	
	public ${class_name_cap}ServiceImpl(${class_name_cap}AbstractService ${class_name}AbstractService) {
		super(${class_name}AbstractService);
	}
}


<@u.block_end />