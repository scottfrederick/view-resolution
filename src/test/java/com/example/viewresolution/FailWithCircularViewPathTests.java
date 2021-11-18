package com.example.viewresolution;

import com.example.viewresolution.ViewResolutionApplication.ApplicationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class FailWithCircularViewPathTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void home() throws Exception {
		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("home"));
	}

	// fails with "Circular view path [other]: would dispatch back to the current handler URL [/other] again."
	// error when URL path matches view name
	@Test
	public void other() throws Exception {
		this.mockMvc.perform(get("/other"))
				.andExpect(status().isOk())
				.andExpect(view().name("other"));
	}

	@Test
	public void another() throws Exception {
		this.mockMvc.perform(get("/another"))
				.andExpect(status().isOk())
				.andExpect(view().name("other"));
	}

}