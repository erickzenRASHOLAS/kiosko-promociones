package cl.kiosko.ms_promociones.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean // llama componentes externos (funciona como libreria)
    public OpenAPI customerOpenAPI(){
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Promociones kiosko")
                        .version("1.0")
                        .description("Documentación de la API para el microservicio de Promociones del kiosko"))
                // 1. Agregamos el candado a todos los endpoints de la API
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Configuramos el formato del botón "Authorize" para usar JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}