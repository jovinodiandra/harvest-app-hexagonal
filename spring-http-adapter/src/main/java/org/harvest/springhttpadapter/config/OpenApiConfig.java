package org.harvest.springhttpadapter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Masukkan JWT token yang didapat dari endpoint login"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TernakLele API")
                        .version("1.0.0")
                        .description("""
                                API untuk Sistem Manajemen Budidaya Ikan Lele (TernakLele).
                                
                                ## Fitur Utama
                                - **Autentikasi**: Register dan Login pengguna
                                - **Manajemen User**: CRUD operasi untuk pengguna
                                - **Manajemen Kolam (Ponds)**: CRUD operasi untuk kolam budidaya
                                - **Manajemen Bibit (Seeds)**: CRUD operasi untuk bibit ikan
                                - **Manajemen Pakan**: Jadwal dan pengingat pemberian pakan
                                - **Catatan Kesehatan**: Penyakit, kematian, dan pertumbuhan ikan
                                - **Catatan Panen**: Hasil panen dan laporan
                                - **Keuangan**: Catatan pemasukan dan pengeluaran
                                - **Supplier**: Manajemen pemasok dan kontak
                                
                                ## Autentikasi
                                API ini menggunakan JWT Bearer Token untuk autentikasi. Setelah login, gunakan token yang diterima pada header `Authorization` dengan format:
                                ```
                                Authorization: Bearer <token>
                                ```
                                
                                ## Rate Limiting
                                API ini memiliki rate limit 100 request per menit per user.
                                """)
                        .contact(new Contact()
                                .name("TernakLele Support")
                                .email("support@ternaklele.com")
                                .url("https://ternaklele.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.ternaklele.com")
                                .description("Production Server")
                ));
    }
}
