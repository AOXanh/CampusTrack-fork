package com.jabai.campustrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
				// Temporary rani, removed if naa nay MySQL connection.
				DataSourceAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class
})
public class CampustrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampustrackApplication.class, args);
	}

}
