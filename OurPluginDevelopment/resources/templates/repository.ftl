<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>

<@u.block_end />