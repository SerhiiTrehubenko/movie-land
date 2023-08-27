package com.tsa.movieland.config;

import com.tsa.movieland.logging.DataRequestInterceptor;
import com.tsa.movieland.security.service.ActiveUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ApplicationConfiguration implements WebMvcConfigurer {

    private final DataSource dataSource;
    private final ActiveUserHolder activeUserHolder;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MovieRequestMethodArgumentResolver());
        resolvers.add(new RatingRequestMethodArgumentResolver(activeUserHolder));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DataRequestInterceptor());
    }
}
