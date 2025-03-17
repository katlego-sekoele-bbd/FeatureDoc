package com.featuredoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class FeaturedocApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeaturedocApplication.class, args);
	}

}

@Component
class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		System.out.println("FeatureDoc Available Endpoints:");
		applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
				.forEach((requestMappingInfo, handlerMethod) -> {
					System.out.println(requestMappingInfo);
				});
	}
}