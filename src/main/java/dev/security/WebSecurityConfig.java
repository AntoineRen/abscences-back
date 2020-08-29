package dev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	JWTAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	JwtConfig jwtConfig;
	
	@Bean
	public PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/exemples").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.antMatchers("/exemples/admin").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and().headers().frameOptions().disable()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
		.logout()
		.logoutSuccessHandler((req,resp,auth) -> resp.setStatus(HttpStatus.OK.value()))
		.deleteCookies(jwtConfig.getCookie());
	}
}
