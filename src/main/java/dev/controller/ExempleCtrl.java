package dev.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exemples")
public class ExempleCtrl {

	@GetMapping
	public String get() {
		return "ceci est un exemple de GET";
	}

	@PostMapping
	public String post() {
		return "ceci est un exemple de POST";
	}

	@GetMapping("/admin")
	public String getAdmin() {
		return "ceci est un exemple de GET /admin (role ADMIN)";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/dba")
	public String getDBA() {
		return "ceci est un exemple de GET /dba (rôle ADMIN)";
	}

}
