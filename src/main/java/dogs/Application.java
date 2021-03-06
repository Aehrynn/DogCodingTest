package dogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import java.util.Arrays;

@SpringBootApplication
@EnableJms
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    private static ApplicationContext applicationContext;




    @Autowired
    void setContext(ApplicationContext applicationContext) {
        Application.applicationContext = applicationContext;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }


}
