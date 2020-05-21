<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name = class.name?uncap_first>
<#assign class_name_id = "${" + class_name + ".id" + "}">
<#assign class_name_plural = u.plural(class_name)>
<#assign opening_bracket = "${">
<#assign closing_bracket = "}">
<#macro print_complex_property prop>
	<#local property_name_url = prop.type.name?uncap_first />
	<#local property_name = prop.name />
	<#local property_name_cap = property_name?cap_first />
	<#local property_id = "${" + property_name + ".id" + "}" />
                        <div class="m-2">
                            <span>${property_name_cap}: <span class="font-weight-bold"><a href="<c:url value="/${property_name_url}/${property_id}"/>">${property_name_cap}</a></span></span>
                        </div>
                        <hr class="my-2">
</#macro>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>
    <%@ include file="navbar.jsp"%>
    <div class="container">
        <div class="text-center mt-2">
            <h5>${class_name_cap} details</h5>
            <div>
                 <div>
                 <#list properties as property>
                    <#if entity_properties[property.type.name]??>
                        <#if property.upper == -1>
                        <#-- Not sure about @ManyToMany -->
                        <c:forEach items="${ "${" + property.name + "}" }" var="${property.name}_sigle">
                        <#assign property_id = "${" + property.name + "_single.id" + "}" />
                        <div class="m-2">
                            <span>${property.name?cap_first}: <span class="font-weight-bold"><a href="<c:url value="/${property.type.name?uncap_first}/${property_id}"/>">${property.name?cap_first} ${property_id}</a></span></span>
                        </div>
                        <hr class="my-2">
                        </c:forEach>
                        <#else>
                        <#-- @ManyToOne or @OneToOne -->
                        <@print_complex_property prop = property />
                        </#if>
                        <#else>
                        <div class="m-2">
                            <span>${property.name?cap_first}: <span class="font-weight-bold">${opening_bracket}${property.name}${closing_bracket}</span></span>
                        </div>
                        <hr class="my-2">			            
                    </#if>
                 </#list>
                 </div>
            </div>
            <a  href="<c:url value="/${class_name}"/>" class="btn btn-sm btn-outline-primary mt-4">Edit</a>
            <a  href="<c:url value="/${class_name}"/>" class="btn btn-sm btn-outline-danger mt-4">Delete</a>
         </div>
    </div>
</body>
</html>