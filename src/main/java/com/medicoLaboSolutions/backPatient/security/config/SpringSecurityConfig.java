package com.medicoLaboSolutions.backPatient.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * This class implements the security aspect of the application.
 * The authentication is secured with credentials proper to the user.
 * In order to login one user has to enter his username and his password that must match with the one registered in the database.
 *
 * Once the login is successful, the user is redirected to his home page based on his "role" (either ADMIN or standard USER for instance)
 *
 * In order to create a profile (user), the needed requests and associated web pages are allowed for a new user.
 * Allowed pages:
 *      User management -> "/user/list", "user/add", "user/validate"
 *      Home page -> "/"
 *
 * For the rest of the requests, the user must be logged in otherwise he will be redirected to the login page.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("doctorUser")).roles("USER").build(),
                User.withUsername("admin1").password(passwordEncoder.encode("doctorAdmin")).roles("USER", "ADMIN").build()
        );
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Set up the login form
        httpSecurity.formLogin().defaultSuccessUrl("/patients").permitAll();
        //Set up the filter chain for different request (authentication needed or not)
        httpSecurity.authorizeHttpRequests((authorizationManagerRequestMatcherRegistry)->
                authorizationManagerRequestMatcherRegistry
                        .anyRequest().authenticated());
        return httpSecurity.build();
    }
    /*@Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(bCryptPasswordEncoder.encode("userPass"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(bCryptPasswordEncoder.encode("adminPass"))
                .roles("USER", "ADMIN")
                .build());
        return manager;
    }*/

/*public class SpringSecurityConfig  extends WebSecurityConfiguration
{

    @Autowired
    private DataSource dataSource;

    *//**
     * This method configures the authentication method with the database.
     * In order to authenticate, you must enter your username and password that will be compared to the ones stored in the database.
     *
     * @param auth AuthenticationManagerBuilder instance that will ensure the authentication configuration of the application
     * @throws Exception
     *//*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
            .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,1 as enabled from user where username = ?")
                .authoritiesByUsernameQuery("select username, role from user where username = ?");
    }

    *//**
     * This method configures the different requests access for the different users.
     * It also defines the parameter for the login and logout process.
     *      The login page is located at /login.
     *
     *      Only a few requests can be executed without being logged in so that a new user can create his profile.
     *      All the other requests require you to be authenticated in order to be executed.
     *
     * @param http HttpSecurity instance that will configure the different access and requests.
     * @throws Exception
     *//*
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
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
