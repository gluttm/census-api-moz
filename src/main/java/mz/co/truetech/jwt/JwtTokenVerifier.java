package mz.co.truetech.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtTokenVerifier extends OncePerRequestFilter {
	
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	
	

	public JwtTokenVerifier(JwtConfig jwtConfig, SecretKey secretKey) {
		this.secretKey = secretKey;
		this.jwtConfig = jwtConfig;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().equals("/api/v1/login")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
			if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
			try {
				Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build()
						.parseClaimsJws(token);
				Claims body = claimsJws.getBody();
				String username = body.getSubject();

				@SuppressWarnings("unchecked")
				var authorities = (List<Map<String, String>>) body.get("authorities");
				Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
						.map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());

				Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
						simpleGrantedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (JwtException e) {
				response.setStatus(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("message", e.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
			filterChain.doFilter(request, response);
		}
	}

}
