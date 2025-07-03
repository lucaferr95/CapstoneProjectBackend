package it.epicode.CapstoneProjectBackend;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties") // consigliato usare classpath:
public class AppConfig {

    /*
    Questo metodo serve per creare un oggetto Cloudinary, che quindi ci permette di uploadare file.
    Va creata una mappa con all'interno le proprietà per accedere al servizio,
    e poi questa mappa va passata al costruttore di Cloudinary per istanziarlo.
    */
    @Bean
    public Cloudinary getCloudinary(@Value("${cloudinary.cloud_name}") String cloudName,
                                    @Value("${cloudinary.api_key}") String apiKey,
                                    @Value("${cloudinary.api_secret}") String apiSecret) {

        Map<String, String> configCloudinary = new HashMap<>();
        configCloudinary.put("cloud_name", cloudName);
        configCloudinary.put("api_key", apiKey);
        configCloudinary.put("api_secret", apiSecret); // corretto qui

        return new Cloudinary(configCloudinary);
    }
    @Bean
    public JavaMailSenderImpl getJavaMailSender(@Value("${gmail.mail.transport.protocol}" )String protocol,
                                                @Value("${gmail.mail.smtp.auth}" ) String auth,
                                                @Value("${gmail.mail.smtp.starttls.enable}" )String starttls,
                                                @Value("${gmail.mail.debug}" )String debug,
                                                @Value("${gmail.mail.from}" )String from,
                                                @Value("${gmail.mail.from.password}" )String password,
                                                @Value("${gmail.smtp.ssl.enable}" )String ssl,
                                                @Value("${gmail.smtp.host}" )String host,
                                                @Value("${gmail.smtp.port}" )String port){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));

        mailSender.setUsername(from);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);
        props.put("smtp.ssl.enable",ssl);

        return mailSender;

    }
}
