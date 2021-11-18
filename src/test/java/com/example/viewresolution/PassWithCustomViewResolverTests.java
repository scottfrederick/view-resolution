package com.example.viewresolution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.viewresolution.PassWithCustomViewResolverTests.ViewConfiguration;
import com.example.viewresolution.ViewResolutionApplication.ApplicationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = { ApplicationController.class, ViewConfiguration.class })
class PassWithCustomViewResolverTests {

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

	@Test
	public void other() throws Exception {
		this.mockMvc.perform(get("/other"))
				.andExpect(status().isOk())
				.andExpect(view().name("other"));
	}

	@Configuration(proxyBeanMethods = false)
	static class ViewConfiguration {

		private static final String TEMPLATE_PREFIX = "classpath:/templates/";

		private static final String TEMPLATE_SUFFIX = ".html";

		@Bean
		@Order(Ordered.HIGHEST_PRECEDENCE)
		SimpleTemplateViewResolver simpleTemplateViewResolver() {
			return new SimpleTemplateViewResolver();
		}

		static class SimpleTemplateViewResolver extends AbstractTemplateViewResolver {

			public SimpleTemplateViewResolver() {
				setViewClass(SimpleTemplateView.class);
			}

			@Override
			protected AbstractUrlBasedView buildView(String viewName) throws Exception {
				return super.buildView(viewName);
			}

			@Override
			protected AbstractUrlBasedView instantiateView() {
				return (getViewClass() == SimpleTemplateView.class) ? new SimpleTemplateView() : super.instantiateView();
			}

		}

		static class SimpleTemplateView extends AbstractTemplateView {

			@Override
			protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				Resource resource = getTemplateResource();
				String reader = new BufferedReader(
						new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines()
						.collect(Collectors.joining("\n"));
				response.getWriter().write(reader);
			}

			private Resource getTemplateResource() {
				String templateUrl = TEMPLATE_PREFIX + getUrl() + TEMPLATE_SUFFIX;
				return getApplicationContext().getResource(templateUrl);
			}

		}

	}

}