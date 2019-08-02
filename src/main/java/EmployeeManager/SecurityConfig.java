package EmployeeManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by lsh on 08/02/2018.
 */

class PlaintextEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/login/form").failureUrl("/login/fail").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/wechat/**", "/contact/**");
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
        web.ignoring().antMatchers("/**/*.js", "/**/*.css", "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.txt");
        web.ignoring().antMatchers("/**/error.html", "/**/error/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication().passwordEncoder(new PlaintextEncoder())
                .withUser(System.getenv("AdminUsername")).password(System.getenv("AdminPassword")).roles("USER");

    }

}
