package com.akkafun.common.event.config;

import com.akkafun.common.spring.cloud.stream.CustomChannelBindingService;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binding.BindableChannelFactory;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.binding.ChannelBindingService;
import org.springframework.cloud.stream.binding.DynamicDestinationsBindable;
import org.springframework.cloud.stream.config.ChannelBindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by liubin on 2016/4/8.
 */
@EnableAsync
public class EventConfiguration extends AsyncConfigurerSupport {


    @Bean
    @DependsOn("binderAwareChannelResolver")
    public ChannelBindingService bindingService(ChannelBindingServiceProperties channelBindingServiceProperties,
                                                BinderFactory<MessageChannel> binderFactory) {

        return new CustomChannelBindingService(channelBindingServiceProperties, binderFactory);

    }

    @Bean
    public BinderAwareChannelResolver binderAwareChannelResolver(BinderFactory<MessageChannel> binderFactory,
                                                                 ChannelBindingServiceProperties channelBindingServiceProperties,
                                                                 DynamicDestinationsBindable dynamicDestinationsBindable,
                                                                 BindableChannelFactory bindableChannelFactory) {

        return new BinderAwareChannelResolver(binderFactory, channelBindingServiceProperties,
                dynamicDestinationsBindable, bindableChannelFactory);
    }


    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("EventExecutor-");
        executor.initialize();
        return executor;
    }

}
