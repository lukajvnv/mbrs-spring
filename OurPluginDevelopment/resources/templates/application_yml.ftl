server:
  port: ${port}
  # since v2.0 servlet added; problem with exporting war to external tomcat
  servlet:
    contextPath: ${contextPath}
spring:
  application:
    name: ${applicationName}
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
  datasource:
<#if dataSource == "h2" || dataSource == "mysql">
    driver-class-name: ${driverClassName}
</#if>
    url: ${dataSourceUrl}
    username: ${username}
    password: ${password}
    sql-script-encoding: UTF-8
    initialization-mode: always
    tomcat:
      test-while-idle: true
      validation-query: SELECT 1
<#if dataSource == "h2">
  h2:
    console:
      enabled: true
      path: ${h2Path}
</#if>
  jpa:
<#if dataSource == "mysql">
    database-platform: org.hibernate.dialect.MySQLDialect
</#if>
    show-sql: true
    hibernate:
      ddl-auto: create
      nameing:
        strategy: org.hibernate.cfg.ImprovedNamingStrategy
    properties:
      hibernate:
        dialect: ${hibernateDialect}