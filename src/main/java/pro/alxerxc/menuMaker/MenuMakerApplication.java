package pro.alxerxc.menuMaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MenuMakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuMakerApplication.class, args);
	}

}
