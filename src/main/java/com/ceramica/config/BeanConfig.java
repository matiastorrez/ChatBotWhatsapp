package com.ceramica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class BeanConfig {


    @Value("${whatsapp.baseurl}")
    private String whatsappBaseUrl;

    @Value("${whatsapp.phone-number-identifier}")
    private String whatsappPhoneNumberIdentifier;

    @Value("${whatsapp.token}")
    private String whatsappToken;

    @Bean
    public RestClient restClientWhatsapp() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        String url = whatsappBaseUrl+whatsappPhoneNumberIdentifier + "/messages";
        String tokenAuthorization = "Bearer " + whatsappToken;
        System.out.println(url);
        return RestClient.builder()
                .baseUrl(url)
                .messageConverters(converters -> converters.add(converter))
                .defaultHeader("Authorization", tokenAuthorization)
                .build();
    }

}
