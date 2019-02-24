package org.luncert.csdn2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO: shutdown with shutcut
 */
@SpringBootApplication
public class App implements CommandLineRunner {

	@Autowired
	Spider spider;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// spider.start();
	}

}
