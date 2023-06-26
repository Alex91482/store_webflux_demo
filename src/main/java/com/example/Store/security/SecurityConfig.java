package com.example.Store.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;


@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(
                        "/","/403","/404","/product/**","/videoOnPage/**","/login","/cardProduct","/blog", "/products","/scooter",
                        "/test","/productPage/**",
                        "/images/**", "/resource/images/**", "/raw/video/**", "/static/styles/**","/static/js/**","/styles/**"
                ).permitAll()
                .and().authorizeExchange()
                .pathMatchers("/admin/**", "/upload/**", "/actuator", "/actuator/**").hasRole("ADMIN")
                .anyExchange()
                .authenticated()
                .and().formLogin().loginPage("/login").authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/admin/stock"))
                .and()
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {

        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder().encode("test"))
                .roles("ADMIN")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder().encode("test"))
                .roles("ADMIN")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


