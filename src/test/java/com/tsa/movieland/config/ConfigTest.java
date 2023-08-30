package com.tsa.movieland.config;

import com.tsa.movieland.common.Role;
import com.tsa.movieland.entity.Credentials;
import com.tsa.movieland.entity.User;
import com.tsa.movieland.security.service.ActiveUserHolder;
import com.tsa.movieland.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Profile("test")
@Configuration
public class ConfigTest {

    @Bean
    @Profile(value = {"no-secure"})
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().anyRequest();
    }

    @Bean
    @Profile(value = {"no-secure"})
    @Primary
    public ActiveUserHolder activeUserHolderTest() {
        final ActiveUserHolder activeUserHolder = Mockito.mock(ActiveUserHolder.class);
        when(activeUserHolder.getUserEmail(anyString())).thenReturn("tsa85.ca@gmail.com");
        return activeUserHolder;
    }

    @Bean
    @Profile(value = {"no-secure"})
    @Primary
    public UserService userDetailsTest() {
        final User user = User.builder()
                .id(1000012)
                .firstName("Serhii")
                .lastName("Trehubenko")
                .nickname("Trehubenko")
                .email("tsa85.ca@gmail.com")
                .credentials(Credentials.builder().userId(1000012).password("$2a$11$eS67ca7P7Bl5FyQTpCiQKuoY4FBZFESoTVcwJ63dh1lO2f0e200r2").role(Role.ADMIN).build())
                .build();
        final UserService userService = Mockito.mock(UserService.class);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        return userService;
    }

}
