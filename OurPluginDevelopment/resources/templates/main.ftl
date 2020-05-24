<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = app_name + "Application">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${base_package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ${repository_package}.${repository_class};

@SpringBootApplication
@EnableJpaRepositories(basePackages = "${repository_package}",
repositoryBaseClass = ${repository_class}.class)
public class ${class_name_cap} {

	public static void main(String[] args) {
		SpringApplication.run(${class_name_cap}.class, args);
	}

}
<@u.block_end />