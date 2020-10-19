> 🚀 screw (螺丝钉) 英:[`skruː`] ~ 简洁好用的数据库表结构文档生成工具

偶然发现了一款screw数据库文档生成插件，支持Oracle、MySQL、PG等主流数据库，配置很简单，支持导出HTML、MD、Word多种格式。官方地址：https://gitee.com/leshalv/screw/

先看一下生成的效果：（HTML）

![](https://img2020.cnblogs.com/blog/1672862/202010/1672862-20201016193726046-915325990.png)

----


使用SpringBoot结合官方文档，写了个demo，代码如下

首先pom文件需要引入screw的依赖（还需要引入对应数据库的依赖，就不详细列举了）：

**pom.xml**

```xml
<dependency>
    <groupId>cn.smallbun.screw</groupId>
    <artifactId>screw-core</artifactId>
    <version>1.0.5</version>
</dependency>
```

配置文件添加几个自定义配置，这样做主要是方便修改，而不是直接硬编码在代码中，凭个人喜好吧~

**application.yml**

```yaml
jdbc:
  driverClassName: com.mysql.cj.jdbc.Driver
  jdbcUrl: jdbc:mysql://localhost:3306/meeting_450?serverTimezone=UTC
  username: root
  password: root

screw:
  fileOutputDir: D:\\dataBaseNode
  version: 1.0.0
  description: 数据库文档生成
```

screw：

​	fileOutputDir：文档生成路径

​	version：版本号

​	description：描述

**JdbcConfig.java**

用来读取application.yml中的jdbc属性

```java
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
```

**ScrewConfig.java**

用来读取application.yml中的screw相关属性

```java
/**
 * screw基础配置映射
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
```

**DocumentGeneration.java**

这个是从官方示例copy过来的，只需要改几个参数就可以直接用。

`fileType(EngineFileType.HTML)` 指定了生成文件类型为html，同时也可以指定为word或markdown

```java
/**
 * 文档导出主类
 *
 * @author wanglp
 * @date 2020/10/16 11:37
 */
@Component
public class DocumentGeneration {

    @Autowired
    private JdbcConfig jdbcConfig;
    @Autowired
    private ScrewConfig screwConfig;

    /**
     * 文档生成
     */
    public void documentGeneration() {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(jdbcConfig.getDriverClassName());
        hikariConfig.setJdbcUrl(jdbcConfig.getJdbcUrl());
        hikariConfig.setUsername(jdbcConfig.getUsername());
        hikariConfig.setPassword(jdbcConfig.getPassword());
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir(screwConfig.getFileOutputDir())
                //打开目录
                .openOutputDir(true)
                //文件类型
                .fileType(EngineFileType.HTML)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName("dbNote").build();

        //忽略表
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        ignoreTableName.add("test_group");
        //忽略表前缀
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("test_");
        //忽略表后缀
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");
        ProcessConfig processConfig = ProcessConfig.builder()
                //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                //根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                //根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
        //配置
        Configuration config = Configuration.builder()
                //版本
                .version(screwConfig.getVersion())
                //描述
                .description(screwConfig.getDescription())
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();
        //执行生成
        new DocumentationExecute(config).execute();
    }
}
```

最后附上测试类

**ScrewApplicationTests.java**

```java
@RunWith(SpringRunner.class)
@SpringBootTest
class ScrewApplicationTests {

    @Autowired
    DocumentGeneration documentGeneration;

    @Test
    void contextLoads() {
        documentGeneration.documentGeneration();
    }
}
```