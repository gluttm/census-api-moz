package mz.co.truetech.jwt;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import mz.co.truetech.dto.projection.PUserDTO;
import mz.co.truetech.exceptions.ApiException;
import mz.co.truetech.exceptions.ApiRequestException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import mz.co.truetech.repository.UserRepository;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final Environment environment;
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	private final UserRepository userRepository;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
					.readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
				Authentication authentication = new UsernamePasswordAuthenticationToken(
						authenticationRequest.getUsername(),
						authenticationRequest.getPassword()
				);
				return authenticationManager.authenticate(authentication);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		Optional<mz.co.truetech.entity.User> user =  userRepository.findByUsername(authResult.getName());
		user.ifPresent((u) -> {
			if (u.getIsLocked()) {
				response.setStatus(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("message", environment.getProperty("account.locked"));
				response.setContentType(APPLICATION_JSON_VALUE);
				try {
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		});
		String token = Jwts.builder()
							.setSubject(authResult.getName())
							.claim("authorities", authResult.getAuthorities())
							.setIssuedAt(new Date())
							.setExpiration(new Date(System.currentTimeMillis() + (jwtConfig.getTokenExpirationAfterSeconds() * 1000)))
							.signWith(secretKey)
							.compact();

		PUserDTO pUserDTO = new PUserDTO();
		if (user.isPresent()) {
			pUserDTO = new PUserDTO(user.get());
		}
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		SuccessAuthResponse authResp = new SuccessAuthResponse(token, pUserDTO);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.forLanguageTag("pt"));
		df.setTimeZone(TimeZone.getTimeZone("Africa/Maputo"));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		objectMapper.setDateFormat(df);
		objectMapper.writeValue(response.getOutputStream(), authResp);
				
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(FORBIDDEN.value());
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.forLanguageTag("pt"));
		df.setTimeZone(TimeZone.getTimeZone("Africa/Maputo"));
		objectMapper.setDateFormat(df);
		objectMapper.findAndRegisterModules();
		ApiException exceptionApi = new ApiException(environment.getProperty("notvalid.login"), HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
		objectMapper.writeValue(response.getOutputStream(), exceptionApi);
	}

}
