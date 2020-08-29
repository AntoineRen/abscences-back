package dev.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ExempleCtrl.class)
class ExempleCtrlTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	@WithAnonymousUser
	void getAdminAnonymous() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/exemples/admin"))
				.andExpect(MockMvcResultMatchers.status().is(403));
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	void getAdminRoleUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/exemples/admin"))
				.andExpect(MockMvcResultMatchers.status().is(403));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getAdminRoleAdmin() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/exemples/admin"))
				.andExpect(MockMvcResultMatchers.status().is(200));
	}

}
