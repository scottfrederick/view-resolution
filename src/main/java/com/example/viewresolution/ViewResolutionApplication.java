package com.example.viewresolution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class ViewResolutionApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ViewResolutionApplication.class, args);
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver("", ".html");
		viewResolver.setAlwaysInclude(true);
		registry.viewResolver(viewResolver);
		registry.order(Ordered.HIGHEST_PRECEDENCE);
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

	@Configuration(proxyBeanMethods = false)
	@Profile("secured")
	static class SecurityFilterConfiguration {

		@Bean
		SecurityFilterChain configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.authorizeRequests((requests) -> requests.anyRequest().permitAll());
			return http.build();
		}

	}
	
}
