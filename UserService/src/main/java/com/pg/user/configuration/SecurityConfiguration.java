package com.pg.user.configuration;

import com.pg.user.security.JwtFilter;
import com.pg.user.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(JwtUtil jwtUtil,UserDetailsService userDetailsService){
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, JwtUtil jwtUtil) throws Exception{
        JwtFilter jwtfilter=new JwtFilter(authenticationManager,jwtUtil);
        http.csrf(csrf->csrf.disable()).authorizeHttpRequests(auth->
                auth.requestMatchers("/api/user/create","/generate-token").permitAll().anyRequest().authenticated()
        ).addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider()));
    }
}
