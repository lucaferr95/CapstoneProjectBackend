package it.epicode.CapstoneProjectBackend.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // abilita la classe a essere responsabile della sicurezza dei servizi
@EnableMethodSecurity // abilita l'utilizzo della preautorizzazione direttamente sui metodi dei controller
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // primo metodo: serve per creare in automatico una pagina di login, che qui disabilitiamo
        httpSecurity.formLogin(http -> http.disable());

        // csrf serve per evitare la possibilità di utilizzi di sessioni lasciate aperte, ma per le API REST si disattiva
        httpSecurity.csrf(http -> http.disable());

        // non ci interessa perché i servizi REST non hanno sessione
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // abilita il CORS per accettare richieste da domini diversi (es. frontend separato)
        httpSecurity.cors(Customizer.withDefaults());

        // autorizzazioni per le richieste:
        httpSecurity.authorizeHttpRequests(http -> http
                // sblocca l’accesso alle pagine statiche frontend
                .requestMatchers("/login.html", "/register.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                // permette l'accesso ai servizi pubblici
                .requestMatchers("/auth/**").permitAll() // login, registrazione
                .requestMatchers("/api/lyrics/**").permitAll() // testi + traduzioni
                .requestMatchers(HttpMethod.GET, "/api/feedback/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/feedback").authenticated()
                .requestMatchers("/auth/me").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/users/me/avatar").authenticated()
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/feedback/**").hasRole("ADMIN")
                .requestMatchers("/api/feedback/backoffice").hasRole("ADMIN")
                .requestMatchers("/quiz/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/punti/manuale").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/punti/classifica").hasRole("ADMIN")
                .requestMatchers("/punti/**").authenticated()

                // blocca tutte le altre richieste
                .anyRequest().denyAll()
        );

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // encoder per le password, verrà applicato 10 volte
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // cors
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("https://capstoneproject-woad.vercel.app"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfiguration.setAllowCredentials(true); // fondamentale se usi token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
