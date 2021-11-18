package com.example.viewresolution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class ViewResolutionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViewResolutionApplication.class, args);
	}

	@Controller
	static class ApplicationController {
		@GetMapping("/")
		public String home() {
			return "home";
		}

		@GetMapping("/other")
		public String other() {
			return "other";
		}

		@GetMapping("/another")
		public String another() {
			return "other";
		}
	}
}
