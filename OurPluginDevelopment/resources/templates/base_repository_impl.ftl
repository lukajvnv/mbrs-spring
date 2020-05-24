<#import "/commons/utils.ftl" as u>
<#assign base_repository_class_name_cap = class?cap_first>
<#assign class_name_cap = base_repository_class_name_cap + "Impl">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${package};

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import ${package}.${base_repository_class_name_cap};


public class ${class_name_cap}<T> extends SimpleJpaRepository<T, ${id_type}> implements ${base_repository_class_name_cap}<T> {
    public ${class_name_cap}(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}
}
<@u.block_end />