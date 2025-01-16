package org.example.booktrackerservice.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class GlobalTimezone implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+03:00"));
    }
}
