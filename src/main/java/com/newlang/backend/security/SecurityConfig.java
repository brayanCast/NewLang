package com.newlang.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicación
@EnableWebSecurity //Indicamos que se activa la seguridad web en nuestra aplicación y adempás esta será una clase la cual obtendrá toda la configuración referente a la seguridad
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //Beans objetos administrador por el contenedor principal, existen beans genéricos o para tareas específicas

    //Bean que se encarga de verificar la información de los usuarios que se loguearán en la api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); //Verifica que la informacion que se esté pasando a través del login sea correcta
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este bean incorpora el filtro de seguridad de Json Wbe token que se crea en la clase anterior
    @Bean
    JwtAuthenticationFilter jwtauthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    //Bean para establecer una cadena de filtros de seguridad en la aplicación,
    //se determinan los permisos según los roles de usuario para acceder a la aplicación
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions //Permitimos el manejo de excepciones
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Establece un punto de entrada personalizado de autenticacion para el manejo de autenticaciones autorizadas
                .sessionManagement(session -> session //Gestión de las sesiones
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Sesiones sin estado
                .authorizeHttpRequests(authorize -> authorize //Autorización de peticiones http
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll() //Permitir acceso a endpoints de autenticación
                        .anyRequest().authenticated() //Cualquier otra petición requiere autenticación
                )
                .addFilterBefore(jwtauthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
