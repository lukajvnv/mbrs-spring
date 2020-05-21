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
                        <td><a href="<c:url value="/${property_name_url}/${property_id}"/>">${property_name_cap}</a></td>
</#macro>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>New ${class_name_cap} form</title>
</head>
<body>
    <%@ include file="navbar.jsp"%>
    <c:url var="action" value="/${class_name}" />		
    <div class="container">
        <div class="row">
            <div class="col-md-3"></div>
            <div class="col-md-6 border p-4">
                <h5 class="text-center">New ${class_name_cap} form</h5>
                <form:form class="p-2" action="${opening_bracket}action${closing_bracket}" method="post" modelAttribute="${class_name}">
                <#list properties as property>
                    <#assign label= "<form:label path=\"${property.name}\">${property.name?cap_first}</form:label>">
                    <#if entity_properties[property.type.name]??>
                    <#if property.upper == -1>
                    <#-- Not sure about @ManyToMany -->
                    <div class="form-group">
                        ${label}
                        <#-- TODO: SET SOMEHOW ITEMLABEL TO SOME DISPLAY PROPERTY, ADDITIONAL ITEMVALUE TO CUSTOM ID PROPERTY -->
                        <form:checkboxes items="${u.plural(property.name)}" path="${property.name}" element="div class='checkbox form-control' CssStyle='float:right'" itemValue="id"/>		
                    </div>
                    <#else>
                    <#-- @ManyToOne or @OneToOne -->
                    <div class="form-group">
                        ${label}
                        <form:select path="${property.name}" cssClass="form-control">
                            <option value="-1">Select a ${property.name}</option>
                            <#-- TODO: SET SOMEHOW ITEMLABEL TO SOME DISPLAY PROPERTY, ADDITIONAL ITEMVALUE TO CUSTOM ID PROPERTY -->
                            <form:options items="${u.plural(property.name)}" itemValue="id"/>
                        </form:select>	
                    </div>
                    </#if>
                    <#else>
                    <#-- simple data type -->
                    <#-- checkbox -->
                    <#if property.type.name == "Boolean" || property.type.name == "boolean">
                    <div class="form-group">
                        <form:checkbox path="${property.name}" />
                        ${label}
                    </div>
                    <#else>
                    <#-- text -->
                    <#-- textarea TODO -->
                    <div class="form-group">
                        ${label}
                        <form:input cssClass="form-control" path="${property.name}" />
                    </div>
                    </#if>		            
                    </#if>
                </#list>
                    <div>
                         <button class="btn btn-success float-right" type="submit">Add ${class_name}</button>
                    </div>      
                 </form:form>
            </div>
            <div class="col-md-3"></div>
          </div>
     </div>
</body>
</html>