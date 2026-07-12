package com.brandom.mipaginaweb;

import com.brandom.mipaginaweb.config.DotenvInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MipaginawebApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MipaginawebApplication.class);
		app.addInitializers(new DotenvInitializer());
		app.run(args);
	}

}
