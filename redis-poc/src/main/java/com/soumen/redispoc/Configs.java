package com.soumen.redispoc;

import org.springframework.context.annotation.Configuration;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@Configuration
public class Configs {

    /*@Bean
    public ReactiveRedisTemplate<String, Task> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Task> valueSerializer = new Jackson2JsonRedisSerializer<>(Task.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Task> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Task> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }*/
}
