package com.k.service.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.k.service.interceptor.AuthInterceptor;
import com.k.service.interceptor.OpenApiInterceptor;
import com.k.service.interceptor.RequestBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor interceptor;

    @Resource
    private OpenApiInterceptor openApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(openApiInterceptor).addPathPatterns("/openApi", "/openApi/**")
                .excludePathPatterns("/login", "/swagger-resources**", "**.js", "**.css", "/doc.html**")
                .excludePathPatterns("/webjars/**", "/v2/**");


        registry.addInterceptor(interceptor).addPathPatterns("/**").
                excludePathPatterns("/**");

    }

    @Bean
    public FilterRegistrationBean<RequestBodyFilter> setLogServiceFilter() {
        FilterRegistrationBean<RequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        RequestBodyFilter requestBodyFilter = new RequestBodyFilter();
        registrationBean.setFilter(requestBodyFilter);
        registrationBean.setName("interceptor filter body params");
        registrationBean.addUrlPatterns("/openApi/**");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class,
                                   new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addDeserializer(LocalDateTime.class,
                                     new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addDeserializer(LocalDate.class,
                                     new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addDeserializer(LocalTime.class,
                                     new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toPlainString());
            }
        });
        objectMapper.registerModules(simpleModule, new Jdk8Module(), new ParameterNamesModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);
    }


}
