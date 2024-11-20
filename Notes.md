# 笔记

## 常用注释解析
### 1. @Tag
> 接口分组注解,用于Controller类上

- **作用**: 对接口进行分组和描述
- **位置**: Controller类上方
- **示例**:
  ```java
  @Tag(name = "用户管理", description = "用户管理相关接口")
  @RestController
  public class UserController {
  }
  ```

### 2. @Operation
> 接口描述注解,用于Controller方法上

- **作用**: 描述接口的功能
- **位置**: Controller方法上方
- **示例**:
  ```java
  @Operation(summary = "获取用户信息")
  @GetMapping("/{id}")
  public User getUser() {
  }
  ```

### 3. @Schema
> 实体描述注解,用于实体类及其字段

- **作用**: 描述数据模型的属性
- **位置**: 实体类或字段上
- **示例**:
  ```java
  @Schema(description = "用户ID", example = "1")
  private Long id;
  ```

### 4. @PathVariable
> Spring MVC注解,用于获取URL路径参数

- **作用**: 获取URL中的路径变量
- **位置**: Controller方法参数
- **示例**:
  ```java
  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
  }
  ```

### 5. @RequestBody
> Spring MVC注解,用于接收请求体数据

- **作用**: 接收POST/PUT请求的JSON数据
- **位置**: Controller方法参数
- **示例**:
  ```java
  @PostMapping
  public boolean createUser(@RequestBody User user) {
  }
  ```

## 6. SLF4J 日志使用
> 日志门面框架，提供统一的日志记录API

### 基础用法
- **方式一**：使用Lombok注解（推荐）
  ```java
  @Slf4j
  public class UserService {
      public void method() {
          log.info("信息");
      }
  }
  ```

- **方式二**：手动声明
  ```java
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  ```

### 日志级别（从低到高）
- **TRACE**：追踪信息，最详细
- **DEBUG**：调试信息，开发使用
- **INFO**：一般信息，默认级别
- **WARN**：警告信息，潜在问题
- **ERROR**：错误信息，需要处理

### 常用方法
- **基本打印**：
  ```java
  log.debug("调试信息");
  log.info("一般信息");
  log.warn("警告信息");
  log.error("错误信息");
  ```

- **占位符使用**：
  ```java
  String name = "张三";
  int age = 18;
  log.info("用户名: {}, 年龄: {}", name, age);
  ```

- **异常记录**：
  ```java
  try {
      // 业务代码
  } catch (Exception e) {
      log.error("操作失败: {}", e.getMessage(), e);
  }
  ```

### 使用规范
- **选择合适级别**：
  - ERROR：系统错误，影响功能
  - WARN：潜在问题，需要关注
  - INFO：重要业务信息
  - DEBUG：调试信息，开发使用
  - TRACE：详细信息，很少使用
