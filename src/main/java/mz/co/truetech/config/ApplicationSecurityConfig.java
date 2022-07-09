package mz.co.truetech.config;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.jwt.JwtConfig;
import mz.co.truetech.jwt.JwtTokenVerifier;
import mz.co.truetech.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import mz.co.truetech.repository.UserRepository;
import mz.co.truetech.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder;
	private final UserService applicationUserService;
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	private final UserRepository userRepository;
	private final Environment environment;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtUsernameAndPasswordAuthenticationFilter jwtUPfilter = new JwtUsernameAndPasswordAuthenticationFilter(
				authenticationManager(), environment, jwtConfig, secretKey, userRepository
				);
		jwtUPfilter.setFilterProcessesUrl("/api/v1/login");
		http.logout().clearAuthentication(true).logoutUrl("/api/v1/logout").invalidateHttpSession(true);
		http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
		http.authorizeRequests().antMatchers("/api/v1/census").permitAll();
		http.authorizeRequests().antMatchers("/index.html", "/", "/home",
				"/assets/css/*.css", "/asstes/vendor/bootstrap/css/*.css",
				"/assets/vendor/**","/favicon.ico","/assets/vendor/bootstrap/js/*.js","/*.js.map").permitAll();
	    http.headers().frameOptions().disable(); // fix h2 refused to connect
		http.cors().and()
			.csrf()
			.disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilter(jwtUPfilter)
			.addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers("/api/v1/login/**").permitAll()
			.anyRequest()
			.authenticated();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(applicationUserService);
		
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
