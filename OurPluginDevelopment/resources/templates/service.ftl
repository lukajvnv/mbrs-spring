<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name = class_name_cap/>

package ${class_package};

import java.util.List;

import ${class.typePackage}.${class_name_cap};

public interface ${class_name_cap}Service {

    List<${ class_dto_cap }> getAll();
    ${ class_dto_cap } save(${ class_dto_cap } ${ class_dto });
    ${ class_dto_cap } getOne(Long id);
    void delete(${ class_dto_cap } ${ class_dto });
    void deleteById(Long id);
	
}

<@u.block_end />