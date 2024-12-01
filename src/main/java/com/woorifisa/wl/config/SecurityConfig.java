package com.woorifisa.wl.config;

import com.woorifisa.wl.filter.AddInfoCheckFilter;
import com.woorifisa.wl.service.CustomOAuth2UserService;
import com.woorifisa.wl.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
public class SecurityConfig {


    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, UserService userService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login-extra", "/api/**", "/css/**", "/js/**", "/images/**", "/logout").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/login-success")
                        .failureHandler((request, response, exception) -> {
                            HttpSession session = request.getSession(false);
                            if (session != null && session.getAttribute("error_message") == null) {
                                session.setAttribute("error_message", exception.getMessage());
                            }
                            response.sendRedirect("/login?error=true");
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .addFilterAfter(new AddInfoCheckFilter(userService), SecurityContextPersistenceFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }
}


