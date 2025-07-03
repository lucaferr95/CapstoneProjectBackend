package it.epicode.CapstoneProjectBackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // abilita la clase a essere responsabile della sicurezza dei servizi
@EnableMethodSecurity // abilita l'utilizzo della preautorizzazione direttamente sui metodi dei controller
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // primo metodo serve per creare in automatico una pagina di login, disabled
        httpSecurity.formLogin(http->http.disable());
        //csrf serve per evitare la possibilità di utilizzi di sessioni lasciate aperte
        httpSecurity.csrf(http->http.disable());
        // non ci interessa perchè i servizi rest non hanno sessione
        httpSecurity.sessionManagement(http->http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // non ho capito. serve per bloccare richiesta da indirizzi ip e porte diversi da dove si trova il servizio
        httpSecurity.cors(Customizer.withDefaults());

        // sblocca login.html
        httpSecurity.authorizeHttpRequests(http -> http
                .requestMatchers("/login.html", "/register.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll());


        // prevede la approvazione o negazione di un servizio endpoint
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/auth/**").permitAll());
//        httpSecurity.authorizeHttpRequests(http->http.requestMatchers(HttpMethod.GET,"/studenti/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/html/**").permitAll());

        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/html/**").permitAll());

        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/clienti/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/comuni/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/province/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/fatture/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/indirizzi/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/utenti/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers(HttpMethod.POST).permitAll());

        httpSecurity.authorizeHttpRequests(http->http.anyRequest().denyAll());

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10); // verrà applicato 10 volte
    }

    // cors
    @Bean//permette di abilitare l'accesso al servizio anche da parte di server diversi da quello su cui risiede
    //il servizio. In questo caso ho abilitato tutti i server ad accedere a tutti i servizi
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*")); // all "*" permette a qualsiasi origine di accedere al servizio, quando si parla di server pubblico. i router di casa bloccano in automatico le richieste http
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
