package com.softwareTest.timeline.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//TODO Ref https://blog.csdn.net/larger5/article/details/81063438
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	AjaxAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	AjaxAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	AjaxAuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	AjaxLogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	AjaxAccessDeniedHandler accessDeniedHandler;

	@Autowired
	CustomAuthenticationProvider provider;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.authenticationProvider(provider);
//		auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
				.csrf()
				.disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.httpBasic()
				.authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.authorizeRequests()
				.anyRequest()
//				.access("@rbacauthorityservice.hasPermission(httpServletRequest,authentication)")
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler)
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandler)
				.permitAll()
				.and()
				.antMatcher("/**")
				.authorizeRequests()
				.and()
				.cors();

		http.rememberMe().rememberMeParameter("remember-me")
				.userDetailsService(customUserDetailsService).tokenValiditySeconds(Constants.TOKEN_EXPIRATION_SECONDS);

		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
		http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class); // JWT Filter
		http.addFilterAt(customAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
	}

	@Bean//此处名字必须一样
	CustomAuthenticationFilter customAuthenticationFilter() throws Exception
	{
		CustomAuthenticationFilter filter=new CustomAuthenticationFilter();
		filter.setAuthenticationSuccessHandler(new AjaxAuthenticationSuccessHandler());
		filter.setAuthenticationFailureHandler(new AjaxAuthenticationFailureHandler());
		filter.setFilterProcessesUrl("/login");

		//这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}

	@Override
	@Bean // share AuthenticationManager for web and oauth
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
