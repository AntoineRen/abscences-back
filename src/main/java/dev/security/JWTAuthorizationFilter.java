package dev.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Configuration
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private JwtConfig JwtConfig;
	
	public JWTAuthorizationFilter(dev.security.JwtConfig jwtConfig) {
		super();
		JwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(request.getCookies() != null) {
			
			for(Cookie cookie : request.getCookies()) {
				
				if(cookie.getName().equals(JwtConfig.getCookie())) {
					
					try {
						Claims body = Jwts.parser()
								.setSigningKey(JwtConfig.getSecret())
								.parseClaimsJws(cookie.getValue())
								.getBody();
						
						String username = body.getSubject();
						
						@SuppressWarnings("unchecked")
						List<String> roles = body.get("roles", List.class);
						
						List<SimpleGrantedAuthority> authorities = roles
								.stream()
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList());
						
						Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					} catch (JwtException e) {
						// TODO: handle exception
					}
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
