package com.lahmamsi.librarymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LibraryManagementSystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
		
	}


	@Bean
	public WebMvcConfigurer crosConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//						.allowedMethods("*")
//						.allowedOrigins("http://localhost:3000");
				 registry.addMapping("/**")
                 .allowedMethods("GET", "POST", "PUT", "DELETE")
                 .allowedOrigins("http://localhost:3000")			//for local host
//                 .allowedOrigins("http://thelibraryms.s3-website-us-west-1.amazonaws.com/") //for deployed web app on AWS S3
                 .allowedHeaders("*")  // Allow all headers
                 .exposedHeaders("Authorization")  // Expose the Authorization header
                 .allowCredentials(true);  // Allow sending cookies and authentication headers

				 // Handle preflight requests
//         registry.addMapping("/**")
//                 .allowedMethods("OPTIONS")
//                 .allowedOrigins("http://localhost:3000")
//                 .allowedHeaders("*")
//                 .allowCredentials(true);
			}
		};
	}
}
