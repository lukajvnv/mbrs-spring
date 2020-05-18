<#import "/commons/utils.ftl" as u>
package ${enum.typePackage};
<#assign class_name_cap = enum.name?cap_first>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>

public enum ${class_name_cap} {
<#list enum.values as value>
	${value}<#sep>,</#sep>
</#list>
}