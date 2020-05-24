<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = app_name + "Application">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${base_package};

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(${class_name_cap}.class);
	}

}
<@u.block_end />