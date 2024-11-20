# 集成MybatisPlus
## 1. 添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.4.1</version>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
## 2. 编写配置类
```java
package org.example.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
} 
```

## 3. 配置
```yaml
server:
  port: 8080
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/monomer_db?serverTimezone=Asia/Shanghai&characterEncoding=utf-8
    username: root
    password: 123456
  sql:
    init:
      mode: always
      schema-locations: classpath:db/init.sql
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: AUTO
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: org.example.entity
```
## 4. 在主类上添加@MapperScan注解
```java
@SpringBootApplication
@MapperScan("org.example.mapper")
public class MonomerStructureApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonomerStructureApplication.class, args);
    }
}
```

# 集成Swagger
## 1. 添加依赖
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```
## 2. 配置
```yaml
swagger:
  ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs
  packages-to-scan: org.example.controller
```
## 3. 创建Swagger配置类
```java
package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Monomer Structure API")
                        .description("Spring Boot项目 API 文档")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("ByronXiao")
                                .email("xiaoyihao001018@gmail.com")));
    }
} 
```
## 4. 在UserController中添加注解
```java
package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping
    public List<User> listUsers() {
        log.debug("开始获取用户列表");
        List<User> users = userService.listUsers();
        log.debug("获取到 {} 个用户", users.size());
        return users;
    }

    @Operation(summary = "根据ID获取用户")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.debug("获取用户信息, id: {}", id);
        return userService.getUser(id);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public boolean createUser(@RequestBody User user) {
        log.debug("创建用户: {}", user);
        return userService.saveUser(user);
    }

} 
```

## 5. 在User实体类上添加注解
```java
package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("user")
@Schema(description = "用户实体")
public class User {
    
    @Schema(description = "用户ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @Schema(description = "用户名称")
    private String name;
    
    @Schema(description = "用户年龄")
    private Integer age;
} 
```

## 6. 启动地址
```md
# Swagger UI 界面（推荐使用）
http://localhost:8080/swagger-ui.html
# OpenAPI 文档
http://localhost:8080/v3/api-docs
```
# 集成slf4j日志
## 1. 配置（由于Spring Boot 2.7.x及以上版本默认使用SLF4J作为日志门面，所以不需要额外配置）
```yaml
logging:
  level:
    root: info
    org.example: debug
  file:
    name: logs/application.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"
```
## 2. 创建logback-spring.xml配置文件，提供更详细的日志配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 引入Spring Boot默认的日志配置 -->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

    <!-- 定义日志文件的存储地址 -->
    <property name="LOG_HOME" value="logs"/>

    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 开发环境 -->
    <springProfile name="dev">
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="FILE"/>
        </root>
        <logger name="org.example" level="DEBUG"/>
    </springProfile>

    <!-- 生产环境 -->
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
        <logger name="org.example" level="INFO"/>
    </springProfile>
</configuration> 
```
## 3. 在UserController中使用日志
```java
package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping
    public List<User> listUsers() {
        log.debug("开始获取用户列表");
        List<User> users = userService.listUsers();
        log.debug("获取到 {} 个用户", users.size());
        return users;
    }

    @Operation(summary = "根据ID获取用户")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.debug("获取用户信息, id: {}", id);
        return userService.getUser(id);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public boolean createUser(@RequestBody User user) {
        log.debug("创建用户: {}", user);
        return userService.saveUser(user);
    }

} 
```
日志就被自动创建在根目录的logs文件夹下面


# 集成Redis
## 1. 添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## 2. 配置Redis
```yaml
spring:
  # 已有配置保持不变...
  
  redis:
    host: localhost
    port: 6379
    database: 0
    # password: 如果有密码，打开这行注释并设置
    timeout: 10000
    lettuce:
      pool:
        max-active: 8
        max-wait: -1
        max-idle: 8
        min-idle: 0
```

## 3. 创建Redis配置类
```java
package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        // 创建 ObjectMapper 并配置
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        
        // 创建序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(mapper);
        
        // 设置序列化规则
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        return template;
    }
} 
```
## 4. 创建Redis工具类，方便使用
```java
package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis设置缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置缓存并设置过期时间
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis设置缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            log.error("Redis删除缓存失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis判断key存在失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }
}
```
## 5. 使用RedisUtils
在UserService中使用Redis缓存示例
```java
package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisUtils redisUtils;
    
    private static final String USER_KEY_PREFIX = "user:";
    
    public User getUser(Long id) {
        String key = USER_KEY_PREFIX + id;
        
        // 先从Redis获取
        Object cached = redisUtils.get(key);
        if (cached != null) {
            log.debug("从Redis获取用户: {}, 数据是: {}", id, cached);
            return (User) cached;
        }
        
        // Redis没有，从数据库获取
        User user = userMapper.selectById(id);
        if (user != null) {
            // 放入Redis，设置1小时过期
            redisUtils.set(key, user, 3600);
            log.debug("用户数据放入Redis: {}, 数据是: {}", id, user);
        }
        
        return user;
    }
    
    public List<User> listUsers() {
        return userMapper.selectList(null);
    }
    
    public boolean saveUser(User user) {
        return userMapper.insert(user) > 0;
    }
} 
```

