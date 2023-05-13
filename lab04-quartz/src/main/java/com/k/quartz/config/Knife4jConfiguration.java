package com.k.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                //分组名称
                .groupName("私有云数据备份接口:version 1.0").select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.newdi.databackup.controller")).paths(PathSelectors.any())
                .build();
        return docket;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("私有云数据备份接口文档").description("私有云数据备份接口文档")
                .termsOfServiceUrl("http://localhost:8080/data-backup/doc.html")
                .contact(new Contact("newdi", "https://www.newdimchina.com/", null)).version("v1.0").build();
    }

}
