package ${class.typePackage};
// ${class.comment!}

${class.visibility} class ${class.name} {  
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
