<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_service = class_name + "ServiceImpl">
<#assign class_service_cap = class_name_cap + "ServiceImpl">
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name = class_name_cap/>

package ${class_package};

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import ${converter_package}.${class_name_cap}To${class_name_cap}DtoConverter;
import ${converter_package}.${class_name_cap}DtoTo${class_name_cap}Converter;
import ${repository_package}.${class_name_cap}Repository;
import ${model_package}.${class_name_cap};
import ${dto_package}.${class_dto_cap};

public abstract class ${class_name_cap}AbstractService implements ${class_name_cap}Service {
	
    private final ${class_name_cap}Repository ${class_name}Repository;

    private final ${class_name_cap}To${class_dto_cap}Converter ${class_name}To${class_dto_cap}Converter;

    private final ${class_dto_cap}To${class_name_cap}Converter ${class_dto}To${class_name_cap}Converter;

    public ${class_name_cap}AbstractService(${class_name_cap}Repository ${class_name}Repository,${class_name_cap}To${class_dto_cap}Converter ${class_name}To${class_dto_cap}Converter, ${class_dto_cap}To${class_name_cap}Converter ${class_dto}To${class_name_cap}Converter) {
        this.${class_name}Repository = ${class_name}Repository;
        this.${class_name}To${class_dto_cap}Converter = ${class_name}To${class_dto_cap}Converter;
        this.${class_dto}To${class_name_cap}Converter = ${class_dto}To${class_name_cap}Converter;
    }
	
    @Override
    List<${ class_dto_cap }> getAll() {
    	List<${class_dto_cap}> ${class_dto}List = new ArrayList<>();
        for(${class_name_cap} ${class_name} : ${class_name}Repository.findAll()) {
            ${class_dto}List.add(${class_name}To${class_dto_cap}Converter.convert(${class_name}));
        }
        return ${class_dto}List;
    }
    
    @Override
    ${ class_dto_cap } save(${ class_dto_cap } ${ class_dto }) {
        ${class_name_cap} ${class_name} = ${class_name}Repository.save(${class_dto}To${class_name_cap}Converter.convert(${class_dto}));
        return ${class_dto}To${class_dto_cap}Converter.convert(${class_name});
    }
    
    @Override
    ${ class_dto_cap } getOne(Long id) {
    	Optional<${class_name_cap}> ${class_name} = ${class_name}Repository.findById(id);
        if(${class_name}.isPresent()){
            ${class_dto_cap} ${class_dto} = ${class_name}To${class_dto_cap}Converter.convert(${class_name}.get());
        }
        return ${class_dto};
    }
    
    @Override
    void delete(${ class_dto_cap } ${ class_dto }) {
        ${class_name_cap} ${class_name} = ${class_dto_cap}To${class_name_cap}.convert(${class_dto});
        if(${class_name} != null) {
            ${class_name}Repository.delete(${class_name});
        }
    }
    
    @Override
    void deleteById(Long id); {
        ${class_name}Repository.deleteById(id);
    }
	
}

<@u.block_end />