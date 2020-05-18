<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_service = class_name + "ServiceImpl">
<#assign class_service_cap = class_name_cap + "ServiceImpl">
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap+ "ControllerAbstract"/>
package ${class_package};

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import ${dto_package}.${class_dto_cap};
import ${service_package}.${class_service_cap};

public abstract class ${class_name_cap}ControllerAbstract {
    
    private static final String BASE_PAGE = "<@u.plural_print noun=class_name />";
    private static final String FORM_PAGE = "create${class_name_cap}";
    private static final String OVERVIEW_PAGE = "${class_name}Overview";
    
    protected ${class_service_cap} ${class_service};
	  
    public ${class_name_cap}ControllerAbstract(${class_service_cap} ${class_service}) {
       this.${class_service} = ${class_service};
    }

    @GetMapping
    public String create${class_name_cap}(Model model) {
        model.addAttribute("${class_name}", new ${class_dto_cap}());
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
    public String createOrUpdate${class_name_cap}(@Valid @ModelAttribute("${class_name}") ${class_dto_cap} ${class_dto}, BindingResult result) {
        if (result.hasErrors()) {
			
        }
        
        ${class_service}.save(${class_dto});

        return "redirect:/all"
    }

    @GetMapping("/edit")
    public String edit${class_name_cap}(@RequestParam("id") Integer id, Model model {
        ${class_dto_cap} ${class_dto} = ${class_service}.getOne(id);
        model.addAttribute("${class_name}", ${class_dto});

        return FORM_PAGE;
    }

    @DeleteMapping
    public String delete${class_name_cap}(@RequestParam("id") Integer id) {
        ${class_service}.deleteById(id);

        return "redirect:/all";
    }
}
<@u.block_end />