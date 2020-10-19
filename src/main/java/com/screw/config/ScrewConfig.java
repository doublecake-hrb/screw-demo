package com.screw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 螺丝钉的基础配置映射
 *
 * @author wanglp
 * @date 2020/10/16 17:14
 */
@Configuration
@Data
public class ScrewConfig {
    /**
     * 文件生成路径
     */
    @Value("${screw.fileOutputDir}")
    private String fileOutputDir;
    /**
     * 版本号
     */
    @Value("${screw.version}")
    private String version;
    /**
     * 文档描述
     */
    @Value("${screw.description}")
    private String description;
}
