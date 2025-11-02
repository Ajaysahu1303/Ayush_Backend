package com.example.ayush.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http.cors();
        // http.csrf().disable();
        // http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

        // http
        //     .authorizeHttpRequests(auth -> auth
        //         .requestMatchers("/api/auth/**", "/api/registration/**", "/api/home", "/api/admin/login").permitAll()
        //         .requestMatchers("/api/admin/**").hasRole("ADMIN")
        //         .anyRequest().hasRole("USER")
        //     )
        //     .formLogin(form -> form
        //         .successHandler((request, response, authentication) -> {
        //             response.setStatus(HttpServletResponse.SC_OK);
        //         })
        //         .failureHandler((request, response, exception) -> {
        //             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //         })
        //     )
        //     .logout().permitAll();

        

        // return http.build();
         http
        .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Arrays.asList("https://ayushstartupportalucer.vercel.app"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(Arrays.asList("*"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        }))
        .csrf(csrf -> csrf.disable())
        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/api/registration/**", "/api/home", "/api/admin/login").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().hasRole("USER"))
        .formLogin(form -> form
            .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
            .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)))
        .logout().permitAll();

    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Allow cookies to be sent cross-site (Vercel -> Railway)

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone();
    }
} 
