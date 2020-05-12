<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class?cap_first>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${package};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ${class_name_cap}<T> extends JpaRepository<T, ${id_type}> {

}
<@u.block_end />