package coach.barnamenevis.security.config;

import coach.barnamenevis.security.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UsersService usersService;

    private final OAuth2UserService oAuth2UserService;

    @Autowired
    public SecurityConfig(DataSource dataSource, UsersService usersService, OAuth2UserService oAuth2UserService) {
        this.dataSource = dataSource;
        this.usersService = usersService;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/error", "/info").permitAll()
//                .antMatchers("/user/**").hasAnyAuthority("USER","ADMIN")
//                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").usernameParameter("email").successHandler(new LoginSuccessHandler())
                .and().oauth2Login()
                .loginPage("/oauth2Login")
                .authorizationEndpoint().baseUri("/login/oauth2").and()
                .redirectionEndpoint().baseUri("/login/callback").and()
                .userInfoEndpoint().userService(oAuth2UserService).and()
                .successHandler(new LoginSuccessHandler())
                .and().rememberMe().rememberMeCookieName("remember")
                .tokenValiditySeconds(60)
                .rememberMeParameter("remember")
                .and().exceptionHandling().accessDeniedPage("/error")
                .and().logout().logoutUrl("/mylogout").deleteCookies("remember");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
