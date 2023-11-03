package com.shopme.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	 
	@Autowired private CustomerOAuth2UserService oAuth2UserService;
	@Autowired private OAuth2LoginSuccessHandler oauth2LoginHandler;
	@Autowired private DatabaseLoginSuccessHandler dbLoginSuccessHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
        http
            .authorizeHttpRequests(authorize -> authorize
            		.requestMatchers("/account_details","/update_account_details",
            				"/orders/**","/cart","/address_book/**","/checkout",
            				"/place_order","/process_paypal_order","/write_review/**").authenticated()
            		.anyRequest().permitAll()
            )
            .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .usernameParameter("email")
                    .successHandler(dbLoginSuccessHandler)
                    .permitAll()
                   
                )
            .oauth2Login(oauth2Login -> oauth2Login
                    .loginPage("/login")
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService) 
                 ).successHandler(oauth2LoginHandler)
                )
                .logout(logout -> logout
                		.permitAll() 
                )
                .rememberMe(remember-> remember
                		.key("Adjhbsdbs_2312342y378")
                		.tokenValiditySeconds(7*24*60*60)
                ).sessionManagement(sessionManagement -> sessionManagement
                		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                ;
      
        return http.build();
    }
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
}
