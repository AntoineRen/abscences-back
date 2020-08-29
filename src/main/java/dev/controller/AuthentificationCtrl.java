package dev.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.repository.UtilisateurRepository;
import dev.security.InfosAuthentification;
import dev.security.JwtConfig;
import io.jsonwebtoken.Jwts;

@RestController
public class AuthentificationCtrl {

	private UtilisateurRepository utilisateurRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private JwtConfig jwtConfig;

	public AuthentificationCtrl(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
		super();
		this.utilisateurRepository = utilisateurRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtConfig = jwtConfig;
	}
	
	@PostMapping("/auth")
	public ResponseEntity<?> authenticate(@RequestBody InfosAuthentification infos){
		
		return this.utilisateurRepository.findByNomUtilisateur(infos.getNomUtilisateur())
				.filter(utilisateur -> passwordEncoder.matches(infos.getMotDePasse(), utilisateur.getMotDePasse()))
				.map(utilisateur -> {
					
					Map<String,Object> infosSupplementairesToken = new HashMap<>();
					infosSupplementairesToken.put("roles", utilisateur.getRoles());
					
					String jetonJWT = Jwts.builder()
							.setSubject(utilisateur.getNomUtilisateur())
							.addClaims(infosSupplementairesToken)
							.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiresIn() * 1000))
							.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtConfig.getSecret())
							.compact();
					
					ResponseCookie tokenCookie = ResponseCookie.fromClientResponse(jwtConfig.getCookie(), jetonJWT)
							.httpOnly(true)
							.maxAge(jwtConfig.getExpiresIn() *1000)
							.path("/")
							.build();
					
					return ResponseEntity.ok()
							.header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
							.build();
				})
				.orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
}
