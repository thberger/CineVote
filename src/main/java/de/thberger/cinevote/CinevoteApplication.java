package de.thberger.cinevote;

import com.vaadin.spring.boot.VaadinAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = VaadinAutoConfiguration.class)
public class CinevoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinevoteApplication.class, args);
	}
}
