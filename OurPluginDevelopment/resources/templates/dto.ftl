<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_dto = class_name + "Dto">
<#assign class_dto_cap = class_name_cap + "Dto">
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name = class_name_cap/>

package ${class_package};

public class ${class_dto_cap} {

<#list properties as property>
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

    public ${class_dto_cap}() {
	
    }
    
    public ${class_dto_cap}(<#list properties as property>${property.type.name} ${property.name}<#if (property?has_next)>,</#if></#list>) {
       <#list properties as property>
       this.${property.name} = ${property.name};
       </#list>
    }

<#list properties as property>
	<#if property.upper == 1 >   
    public ${property.type.name} get${property.name?cap_first}(){
         return ${property.name};
    }
  
    public void set${property.name?cap_first}(${property.type.name} ${property.name}){
         this.${property.name} = ${property.name};
    }
  
  <#elseif property.upper == -1 >
    public Set<${property.type.name}> get${property.name?cap_first}(){
         return ${property.name};
    }
  
    public void set${property.name?cap_first}( Set<${property.type.name}> ${property.name}){
         this.${property.name} = ${property.name};
    }
      
  <#else>   
	  <#list 1..property.upper as i>
    public ${property.type.name} get${property.name?cap_first}${i}(){
         return ${property.name}${i};
    }
  
    public void set${property.name?cap_first}${i}(${property.type.name} ${property.name}${i}){
         this.${property.name}${i} = ${property.name}${i};
    }
        
	  </#list>  
  </#if>     
</#list>
	
}

<@u.block_end />