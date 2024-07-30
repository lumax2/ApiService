
## 模块说明

    海外Api服务ApiDocs:
        https://streamnz.com/oversea/api/documents/
    
## 功能特性

    实体对象Get Set
        lombok
        
    对象属性驼峰命名、接口字段下划线命名
        
    用户权限
        redis, SA-TOKEN
        
    数据库
        mysql
    
    spring cloud
        consul center, config server, openfeign


## 启动方式
#   本机启动
    IDEA 或者 Eclipe 直接启动即可
    启动JVM 参数
    --spring.profiles.active=dev

## 接口文档

    Swagger ：
    http://localhost:port/swagger-ui/index.html
    knife4j ：
    http://localhost:port/doc.html

## Swagger2与OpenApi3.0注解区别
    @Api -> @Tag
    @ApiIgnore -> @Parameter(hidden = true) 或 @Operation(hidden = true) 或 @Hidden
    @ApiImplicitParam -> @Parameter
    @ApiImplicitParams -> @Parameters
    @ApiModel -> @Schema
    @ApiModelProperty(hidden = true) -> @Schema(accessMode = READ_ONLY)
    @ApiModelProperty -> @Schema
    @ApiOperation(value = "foo", notes = "bar") -> @Operation(summary = "foo", description = "bar")
    @ApiParam -> @Parameter
    @ApiResponse(code = 404, message = "foo") -> @ApiResponse(responseCode = "404", description = "foo")

- 参考资料：
  - lombok
    - 安装：https://blog.csdn.net/zhglance/article/details/54931430
    - 特性：https://projectlombok.org/features/all

  - swagger2
    - https://www.cnblogs.com/magicalSam/p/7197533.html

  - Alibaba Code IDEA 插件
    - IDEA plugin 搜索 Alibaba Code Guideline 安装即可
    - https://github.com/alibaba/p3c

## 开发约定


     异常处理
     业务需要处理的异常才处理，不处理的异常统统往上层抛。
     不允许try catch 后什么都不做隐藏异常导致无法定位错误
     必须e.printstack 或者log.error 记录。
     
     
     事务处理
     加了@Transactional 如果事务期望回滚则不允许try catch异常
     会导致无法回滚。默认只回滚runtime异常如果需要回滚指定异常
     或所有异常加上rollbackfor 
     例:rollbackFor = Exception.class
     
     错误提示
     任何异常错误信息必须配置在messages.properties中不允许在代码中写任何中文提示
     
     常量定义
     所有常量必须在SystemConstants定义
     
     不要映射无用的字段
     表中对应的时间记录如果没有特别操作不建议映射到实体，取数据库时间 SYSDATE
     
     采用充血模型
     在实体对象可以增加业务验证及判断实现充血模型
     在代码层面可以通过集合steam过滤,避免多次数据库交互处理


## DON'T DO THIS
     如无特殊需求不允许在代码中使用反射
     如无特殊需求不允许在代码中使用线程
     不允许在业务代码定义魔法数字例如 if (i == 1)
     因为附件存在开源S3实现minio中任何附件不允许落地到中间过程，比如本地或服务器硬盘


## 表名定义

       表名前缀
       TM	Master Data Table
       TT	Transaction Data Table
       TR	Relationship Table
       TA	Aggregation Table
       TC	Code list Table
       TI	Interface Table
       TS	System Administration Table
       TL	Log Table
       TG	Staging Table
       TD	Dimension Table
       TF	Fact Table
       TU	User Maintained Table
       
       
       表名后缀
       HS   History Data Table

     