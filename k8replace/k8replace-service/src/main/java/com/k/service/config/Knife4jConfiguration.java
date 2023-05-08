package com.k.service.config;

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
                .groupName("接口:version 1.0").select().apis(RequestHandlerSelectors.basePackage("com.k.service"))
                .paths(PathSelectors.any()).build();
        return docket;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("接口文档").description("接口文档")
                .termsOfServiceUrl("http://localhost:8080/doc.html")
                .contact(new Contact("xxx", "https://www.nxxx.com/", null)).version("v1.0").build();
    }

}
