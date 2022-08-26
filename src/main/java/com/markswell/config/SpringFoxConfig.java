package com.markswell.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;


@Configuration
public class SpringFoxConfig implements WebMvcConfigurer, WebMvcOpenApiTransformationFilter {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.markswell.resource"))
                .build();
    }

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI swagger = context.getSpecification();
        Server server = new Server().url("localhost:8081").description("Development server");
        swagger.setServers(Arrays.asList(server));
        swagger.info(apiInfo());
        return swagger;
    }

    @Override
    public boolean supports(DocumentationType docType) {
        return docType.equals(DocumentationType.OAS_30);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    public Info apiInfo() {
        Contact contact = new Contact()
                                .name("Markswell")
                                .url("https://www.linkedin.com/in/markswell-menezes-0049b865/")
                                .email("markswellmenezes@gmail.com");

        return new Info()
                        .title("Registration Customer")
                .version("1.0.0")
                .description("Api for reference to a spring boot app")
                .contact(contact);

    }
}
