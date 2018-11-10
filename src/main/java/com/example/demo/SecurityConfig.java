package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private DataSource dataSource;
	private static final String USER_SQL = "select"
			+ " user_id,"
			+ " password,"
			+ " true"
			+ " from"
			+ " m_user"
			+ " where"
			+ " user_id=?";
	private static final String ROLE_SQL = "select"
			+ " user_id,"
			+ " role"
			+ " from"
			+ " m_user"
			+ " where"
			+ " user_id=?";

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/webjar/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/signup").permitAll()
				.antMatchers("/rest/**").permitAll()
				.antMatchers("/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated();

		http.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureUrl("/login")
				.usernameParameter("userId")
				.passwordParameter("password")
				.defaultSuccessUrl("/home", true);

		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login");

		RequestMatcher csrfMatcher=new RestMatcher("/rest/**");
		http.csrf().requireCsrfProtectionMatcher(csrfMatcher);

//		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(USER_SQL)
				.authoritiesByUsernameQuery(ROLE_SQL)
				.passwordEncoder(passwordEncoder());
	}
}
