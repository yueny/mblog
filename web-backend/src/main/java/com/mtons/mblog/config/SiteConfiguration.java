package com.mtons.mblog.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mtons.mblog.modules.template.TemplateDirective;
import com.mtons.mblog.modules.template.method.DatetimeMinuteMethod;
import com.mtons.mblog.modules.template.method.TimeAgoMethod;
import com.mtons.mblog.shiro.ShiroRuleFuncNamesUtil;
import com.mtons.mblog.shiro.tags.ShiroTags;
import com.yueny.rapid.lang.thread.executor.MonitorThreadPoolExecutor;
import com.yueny.rapid.lang.thread.factory.NamedThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : langhsu
 * @since 3.0
 */
@Configuration
@EnableAsync
public class SiteConfiguration {
    //        Configuration cfg = FreeMarkers.defaultConfiguration();
    @Autowired
    private freemarker.template.Configuration configuration;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * freemark 常量设置
     */
    @PostConstruct
    public void setSharedVariable() {
        Map<String, TemplateDirective> map = applicationContext.getBeansOfType(TemplateDirective.class);

        /* 注册 TemplateModel */
        map.forEach((k, v) -> configuration.setSharedVariable(v.getName(), v));

        configuration.setSharedVariable("shiro", new ShiroTags());
        // 输出格式为  6天前, 3月前, 2小时前
        configuration.setSharedVariable("timeAgo", new TimeAgoMethod());
        // 输出格式为 yyyy-MM-dd HH:mm
        configuration.setSharedVariable("datetimeMinute", new DatetimeMinuteMethod());

        /* 设置常量 */
        try{
            configuration.setSharedVariable("shiroRuleMap", ShiroRuleFuncNamesUtil.getShiroRuleMap());
        }catch (Exception e){
            e.printStackTrace();
        }

        // 在工具类中， 上下文初始化
        ApplicationContextHolder.setApplicationContext(applicationContext);
    }

    @Bean
    public ListeningExecutorService commonExecutorService() {
        NamedThreadFactory threadFactory = new NamedThreadFactory("common-executorService-pool");

        ExecutorService es = new MonitorThreadPoolExecutor(2, 8,
                60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100),
                threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(es);

        return executor;

        // or
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(8);
//        executor.setQueueCapacity(100);
//        executor.setKeepAliveSeconds(60);
//        executor.setThreadNamePrefix("siteConfiguration.logThread-");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        return executor;
    }

    @Bean
    @ConditionalOnClass({JSON.class})
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return fastConverter;
    }

    @Bean
    public HttpMessageConverters httpMessageConverters(){
        FastJsonHttpMessageConverter jsonHttpMessageConverter = fastJsonHttpMessageConverter();
        return new HttpMessageConverters(jsonHttpMessageConverter);
    }
}
