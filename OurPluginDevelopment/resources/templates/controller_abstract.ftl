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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import ${dto_package}.${class_dto_cap};
import ${dto_package}.${class_dto_form_cap};
import ${service_package}.${class_service_cap};

<#if entity_properties?has_content>
import java.util.ArrayList;
import java.util.List;
</#if>

<#list entity_properties?keys as key>
import ${service_package}.${key}ImplService;
</#list>

<#list entity_properties?keys as key>
import ${dto_package}.${key}Dto;
</#list>

<#-- Date converter -->
<#if importedPackages?filter(pack -> pack.name == "Date")?has_content>
import org.springframework.web.bind.WebDataBinder;
import java.text.SimpleDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import java.util.Date;
import org.springframework.web.bind.annotation.InitBinder;
</#if>


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
    
    <#-- Date converter -->
    <#if importedPackages?filter(pack -> pack.name == "Date")?has_content>
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
    </#if>

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

    @GetMapping
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
        ${key}Dto ${value.name}Dto = ${value.name}ImplService.getOne(${class_dto_form}.get${value.name?cap_first}());
        <#else>
        List<${key}Dto> ${value.name}Dto = new ArrayList<${key}Dto>();
        for(String id: ${class_dto_form}.get${value.name?cap_first}()) {
            Integer ${value.name}Id = Integer.parseInt(id);
            ${value.name}Dto.add(${value.name}ImplService.getOne(${value.name}Id));
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

        return "redirect:/${class_name}";
    }

    @GetMapping("/edit")
    public String edit${class_name_cap}(@RequestParam("id") String id, Model model) {
        ${class_dto_cap} ${class_dto} = ${class_service}.getOne(Integer.parseInt(id));
        
        <#list entity_properties as key, value>
        <#if value.upper == -1>
        String[] ${value.name}Array = new String[${class_dto}.get${value.name?cap_first}().size()];
        for(int i = 0; i < ${class_dto}.get${value.name?cap_first}().size(); i++) {
        	${value.name}Array[i] = Integer.toString(${class_dto}.get${value.name?cap_first}().get(i).getId());
        }
        </#if>	
        </#list>
        
        ${class_dto_form_cap} ${class_dto_form} = ${class_dto_form_cap}.builder()
        		<#list properties as property>
        			<#if entity_properties[property.type.name]??>
              			<#if property.upper == -1>  
                          .${property.name}(${property.name}Array)
                        <#else>
                          .${property.name}(${class_dto}.get${property.name?cap_first}().getId())
			            </#if>
			        <#else>
			              .${property.name}(${class_dto}.get${property.name?cap_first}())
			        </#if>
        		</#list>
                          .build();
        
        model.addAttribute("${class_name}", ${class_dto_form});
        <#list entity_properties?values as value>
        model.addAttribute("<@u.plural_print noun=value.name />", ${value.name}ImplService.getAll());	
        </#list>

        return FORM_PAGE;
    }

    @GetMapping("/delete")
    public String delete${class_name_cap}(@RequestParam("id") String id) {
        ${class_service}.deleteById(Integer.parseInt(id));

        return "redirect:/${class_name}";
    }
}
<@u.block_end />