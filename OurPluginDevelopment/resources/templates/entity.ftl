<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${class.typePackage};

// javax.persistence class annotations
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
<#if persistentProperties?has_content>
import javax.persistence.Table;  
</#if>

// lombok classs annotations
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

<#list importedPackages as type>
import ${type.typePackage}.${type.name};
</#list>

@Entity
<#if persistentProperties?has_content>
@Table<@u.print_attributes attributes=persistentProperties />    
</#if>
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${class_name_cap} {
<#list properties as property>
	
	 <@u.print_id_annotations props=property.identityProperties />
	 <#if (property.persistentAnnotationName)?has_content>
      ${property.persistentAnnotationName}<@u.print_attributes attributes=property.persistentProperties />
	 </#if>
	<#if property.upper == 1 >   
      ${property.visibility} ${property.type.name} ${property.name};
    <#elseif property.upper == -1 > 
      ${property.visibility} Set<${property.type.name}> ${property.name} = new HashSet<${property.type.name}>();
    <#else>   
    	<#list 1..property.upper as i>
      ${property.visibility} ${property.type.name} ${property.name}${i};
		</#list>  
    </#if>     
</#list>

}

<@u.block_end />