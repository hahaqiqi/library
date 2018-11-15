package com.november;

import com.november.common.ApplicationContextHelper;
import com.november.common.SpringExceptionResolver;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author skrT
 * @create 2018/11/15 20:23
 */
@Configuration
public class SpringComponents {

    @Bean
    public SpringExceptionResolver springExceptionResolver(){
        return new SpringExceptionResolver();
    }

    @Bean
    @Lazy(value = false)
    public ApplicationContextHelper applicationContextHelper(){
        return new ApplicationContextHelper();
    }

    @Bean
    public MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }

}
