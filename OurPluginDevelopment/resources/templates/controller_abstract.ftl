<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_service = class_name + "ImplService">
<#assign class_service_cap = class_name_cap + "ImplService">
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<#assign class_dto_form = class_name + "FormDto">
<#assign class_dto_form_cap = class_name_cap + "FormDto">

<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap+ "ControllerAbstract"/>
package ${class_package};

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import ${dto_package}.${class_dto_cap};
import ${dto_package}.${class_dto_form_cap};
import ${service_package}.${class_service_cap};

<#list entity_properties?keys as key>
import ${service_package}.${key}ImplService
</#list>

public abstract class ${class_name_cap}ControllerAbstract {
    
    private static final String BASE_PAGE = "${class_name}_list";
    private static final String FORM_PAGE = "${class_name}_form";
    private static final String OVERVIEW_PAGE = "${class_name}_overview";
    
    protected ${class_service_cap} ${class_service};
    
    <#list entity_properties as key, value>
    @Autowired
    protected ${key}ImplService ${value.name}ImplService;
	
	</#list>
	  
    public ${class_name_cap}ControllerAbstract(${class_service_cap} ${class_service}) {
       this.${class_service} = ${class_service};
    }

    @GetMapping("/new")
    public String create${class_name_cap}(Model model) {
        model.addAttribute("${class_name}", new ${class_dto_form_cap}());
        <#list entity_properties?values as value>
        model.addAttribute("<@u.plural_print noun=value.name />", ${value.name}ImplService.getAll());	
        </#list>
        
        return FORM_PAGE;
    }

    @GetMapping("/{id}")
    public String getOverview(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("${class_name}", ${class_service}.getOne(id));
        return OVERVIEW_PAGE;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("<@u.plural_print noun=class_name />", ${class_service}.getAll());

        return BASE_PAGE;
    }

    @PostMapping
    public String createOrUpdate${class_name_cap}(@Valid @ModelAttribute("${class_name}") ${class_dto_form_cap} ${class_dto_form}, BindingResult result) {
        if (result.hasErrors()) {
			
        }
        
    <#list entity_properties as key, value>
        <#if value.upper == 1>
        ${key}Dto ${value.name}Dto = ${value.name}ImplService.get(${class_dto_form}.get${value.name?cap_first}());
        <#else>
        List<${key}Dto> ${value.name}Dto = new ArrayList<${key}Dto>();
        for(String id: ${value.name}Dto.get${value.name?cap_first}) {
            Integer ${value.name}Id = Integer.parseInteger(id);
            ${value.name}Dto.add(${value.name}ImplService.get(${value.name}Id));
    	}
    	 
        </#if>	
    </#list>
        
        ${class_dto_cap} ${class_dto} = ${class_dto_cap}.builder()
		<#list properties as property>
			<#if entity_properties[property.type.name]??>
                .${property.name}(${property.name}Dto)
            <#else>
                .${property.name}(${class_dto_form}.get${property.name?cap_first}())
			</#if>	
        </#list>
                .build();
        ${class_service}.save(${class_dto});

        return "redirect:/${class_name}"
    }

    @GetMapping("/edit")
    public String edit${class_name_cap}(@RequestParam("id") Integer id, Model model) {
        ${class_dto_cap} ${class_dto} = ${class_service}.getOne(id);
        model.addAttribute("${class_name}", ${class_dto});

        return FORM_PAGE;
    }

    @DeleteMapping
    public String delete${class_name_cap}(@RequestParam("id") Integer id) {
        ${class_service}.deleteById(id);

        return "redirect:/${class_name}";
    }
}
<@u.block_end />