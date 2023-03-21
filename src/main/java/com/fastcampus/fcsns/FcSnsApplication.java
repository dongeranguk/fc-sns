package com.fastcampus.fcsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FcSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FcSnsApplication.class, args);
	}

}
