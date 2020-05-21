<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_name_id = "${" + class_name + ".id" + "}">
<#assign class_name_plural = u.plural(class_name)>
<#macro print_complex_property prop>
	<#local property_name_url = prop.type.name?uncap_first />
	<#local property_name = prop.name />
	<#local property_name_cap = property_name?cap_first />
	<#local property_id = "${" + property_name + ".id" + "}" />
                        <td><a href="<c:url value="/${property_name_url}/${property_id}"/>">${property_name_cap}</a></td>
</#macro>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>List of ${class_name_plural}</title>
    </head>
    <body>
        <%@ include file="navbar.jsp"%>
        <div class="container">
            <div>
                 <a class="btn btn-outline-primary btn-sm float-right" href="<c:url value="/${class_name}/new"/>">Add new ${class_name}</a>
            </div>
            <div>
                 <table class="table table-sm table-hover table-bordered text-center mt-3">
                    <tr>
                        <#list properties as property>
                        <th>${property.name}</th>
                        </#list>
                    </tr>
                    <c:forEach items="${ "${" + class_name_plural + "}" }" var="${class_name}">
                    <tr>
                    	<#list properties as property>
                        <#if entity_properties[property.type.name]??>
                        <#if property.upper == -1>
                        <#-- Not sure about @ManyToMany -->
                        <c:forEach items="${ "${" + property.name + "}" }" var="${property.name}_sigle">
                        <#assign property_id = "${" + property.name + "_single.id" + "}" />
                        <td><a href="<c:url value="/${property.type.name?uncap_first}/${property_id}"/>">${property.name?cap_first} ${property_id}</a></td>
                        </c:forEach>
                        <#else>
                        <#-- @ManyToOne or @OneToOne -->
                        <@print_complex_property prop = property />
                        </#if>
                        <#else>
                        <td>${ "${" + class_name + "." + property.name +  "}" }"</td>
			            </#if>
                    	</#list>
                        <td>
                            <a class="btn btn-sm btn-info" href="<c:url value="/${class_name}/${class_name_id}"/>">Detail</a>
                            <a class="btn btn-sm btn-primary" href="<c:url value="/${class_name}/edit?id=${class_name_id}"/>">Edit</a>
                            <a class="btn btn-sm btn-danger" href="<c:url value="/${class_name}/delete?id=${class_name_id}"/>">Delete</a>
                        </td>
                    </tr>
                    </c:forEach>
                 </table>
            </div>
         </div>
   </body>
</html>