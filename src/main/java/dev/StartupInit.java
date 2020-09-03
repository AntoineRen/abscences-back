package dev;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.entites.Utilisateur;
import dev.repository.UtilisateurRepository;

@Configuration
public class StartupInit {
	
	private UtilisateurRepository utilisateurRepository;
	private PasswordEncoder passwordEncoder;

	public StartupInit(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
		super();
		this.utilisateurRepository = utilisateurRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@EventListener(ContextRefreshedEvent.class)
	public void init() {
		
		utilisateurRepository.save(new Utilisateur("employe@dev.fr", passwordEncoder.encode("bestpassword"), 
				Arrays.asList("ROLE_EMPLOYE")));
		utilisateurRepository.save(new Utilisateur("manager@dev.fr", passwordEncoder.encode("bestpassword"), 
				Arrays.asList("ROLE_EMPLOYE","ROLE_MANAGER")));
		utilisateurRepository.save(new Utilisateur("admin@dev.fr", passwordEncoder.encode("bestpassword"), 
				Arrays.asList("ROLE_EMPLOYE","ROLE_MANAGER","ROLE_ADMIN")));
	}
	
}
