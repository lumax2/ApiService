package com.stream.nz.config.redis;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PreDestroy;
import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfigure {
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.expire}")
    private long expire;
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.pool.max-wait}")
    private String maxWait;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdl;
    @Value("${spring.redis.pool.min-idle}")
    private int minIdl;



    @Bean(name = "RedisConnectionFactory")
    @Primary
    public LettuceConnectionFactory RedisConnectionFactory() {

        // 配置连接池
        GenericObjectPoolConfig<DefaultPooledObject<RedisClient>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdl);
        poolConfig.setMinIdle(minIdl);

        // Redis 客户端配置
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setDatabase(database);
        if (StrUtil.isNotEmpty(password)) {
            redisConfig.setPassword(RedisPassword.of(password));
        }

        // 使用 Lettuce 客户端
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(poolConfig)
                .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig, clientConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        log.info("初始化Redis成功Host[{}],Port[{}],Database[{}]", host, port, database);
        return lettuceConnectionFactory;
    }

    @Bean(name = "RedisTemplate")
    public RedisTemplate<String, Object> dmsRedisTemplate(@Qualifier("RedisConnectionFactory") LettuceConnectionFactory partOrderRedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<>();
        redisTemplateObject.setConnectionFactory(partOrderRedisConnectionFactory);
        setSerializer(redisTemplateObject);
        redisTemplateObject.afterPropertiesSet();
        return redisTemplateObject;
    }

    @Bean("StringRedisTemplate")
    public StringRedisTemplate dmsStringRedisTemplate(@Qualifier("RedisConnectionFactory") LettuceConnectionFactory partOrderRedisConnectionFactory) {
        return new StringRedisTemplate(partOrderRedisConnectionFactory);
    }

    private void setSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    @PreDestroy
    public void shutdown() {
        RedisConnectionFactory().destroy();
    }
}
