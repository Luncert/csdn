package org.luncert.csdn2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class App extends SpringBootServletInitializer implements CommandLineRunner {

	// https://blog.csdn.net/weixin_42478901/article/details/82793571
	// @Bean
    // public ThreadPoolTaskScheduler taskScheduler(){
    //     ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    //     return taskScheduler;
	// }
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

	@Override
	public void run(String... args) throws Exception {
	}

}
