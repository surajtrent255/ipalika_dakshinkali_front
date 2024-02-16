package com.ishanitech.ipalikawebapp.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ishanitech.ipalikawebapp.security.CustomAuthenticationProvider;
import com.ishanitech.ipalikawebapp.security.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class IpalikaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider authenticationProvider;

	@Autowired
	private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/assets/**").permitAll()
				.antMatchers("/report/download-csv").permitAll()
		.antMatchers("/home", "/", "/index", "/login").permitAll()
		.antMatchers(HttpMethod.GET, "/favourite-place/**", "/report", "/public/**", "/ward/**").permitAll()
		.antMatchers(HttpMethod.POST, "/public/**").permitAll()
		.antMatchers("/super-admin/**").hasRole("SUPER_ADMIN")
		.antMatchers("/central-admin/**").hasRole("CENTRAL_ADMIN")
		.antMatchers("/ward-admin/**").hasRole("WARD_ADMIN")
		.antMatchers("/surveyor/**").hasRole("SURVEYOR")
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/")
		.loginProcessingUrl("/login")
		.usernameParameter("username")
		.passwordParameter("password")
		.successHandler(authenticationSuccessHandler)
		.failureUrl("/?error=true")
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.permitAll();
	}

}
