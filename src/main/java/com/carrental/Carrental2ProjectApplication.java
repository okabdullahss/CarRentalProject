package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Carrental2ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Carrental2ProjectApplication.class, args);
	}
	
	//CORS(Cross Origin Resource Sharing) - allow Backend and Frontend communication from different origins	
		@Bean
		public WebMvcConfigurer corsConfigurer() {
		    return new WebMvcConfigurer() {
		        
		        @Override
		        public void addCorsMappings(CorsRegistry registry) {
		            registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
		        }
		    };
		}

}
