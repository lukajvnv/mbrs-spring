<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap + "Controller"/>
package ${class_package};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ${service_package}.${class_name_cap}ImplService;

@Controller
@RequestMapping("/${class_name}")
public class ${class_name_cap}Controller extends ${class_name_cap}ControllerAbstract {
	
    @Autowired
    public ${class_name_cap}Controller(${class_name_cap}ImplService ${class_name}Service) {
        super(${class_name}Service);
    }
}

<@u.block_end />