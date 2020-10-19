package com.screw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源属性映射
 *
 * @author wanglp
 * @date 2020/10/16 11:34
 */
@Configuration
@Data
public class JdbcConfig {
    /**
     * 驱动类
     */
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    /**
     * url
     */
    @Value("${jdbc.jdbcUrl}")
    private String jdbcUrl;
    /**
     * 用户名
     */
    @Value("${jdbc.username}")
    private String username;
    /**
     * 密码
     */
    @Value("${jdbc.password}")
    private String password;

}
