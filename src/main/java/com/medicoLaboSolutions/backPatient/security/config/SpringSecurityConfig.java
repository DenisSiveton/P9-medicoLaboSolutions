package com.medicoLaboSolutions.backPatient.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * This class implements the security aspect of the application.
 * The authentication is secured with credentials proper to the user.
 * In order to login one user has to enter his username and his password that must match with the one registered in the database.
 *
 * Once the login is successful, the user is redirected to the current home page based on his "role" (either ADMIN or standard USER for instance)
 *
 * In order to create a profile (user), the needed requests and associated web pages are allowed for a new user.
 * Allowed pages:
 *      User management -> "/user/list", "user/add", "user/validate"
 *      Home page -> "/patients"
 *
 * For the rest of the requests, the user must be logged in otherwise he will be redirected to the login page.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method configures the authentication method with the database.
     * In order to authenticate, you must enter your username and password that will be compared to the ones stored in the database.
     */
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * This method configures the different requests access for the different users.
     * It also defines the parameter for the login and logout process.
     * The login page is located at /login.
     * <p>
     * Only a few requests can be executed without being logged in so that a new user can create his profile.
     * All the other requests require you to be authenticated in order to be executed.
     *
     * @param httpSecurity HttpSecurity instance that will configure the different access and requests.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Set up the login form
        httpSecurity.formLogin().defaultSuccessUrl("/patients").permitAll();
        //Set up the filter chain for different request (authentication needed or not)
        httpSecurity.authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) ->
                authorizationManagerRequestMatcherRegistry
                        .anyRequest().authenticated());
        return httpSecurity.build();
    }

    /*
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/","/user/list", "/user/add", "/user/validate").permitAll()
                .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/default",true)
                    .failureUrl("/login-error")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/app-logout")
                    .permitAll();
    }*/
}
