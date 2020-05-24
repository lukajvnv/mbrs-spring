<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${class_package};

import ${class_package}.${base_repository};

import ${class.typePackage}.${class_name_cap};

<#--  @Repository -->
public interface ${class_name_cap}Repository extends ${base_repository}<${class_name_cap}> {

}
<@u.block_end />