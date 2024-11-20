# 笔记

## 1. @Tag
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

## 2. @Operation
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

## 3. @Schema
> 实体描述注解,用于实体类及其字段

- **作用**: 描述数据模型的属性
- **位置**: 实体类或字段上
- **示例**:
  ```java
  @Schema(description = "用户ID", example = "1")
  private Long id;
  ```

## 4. @PathVariable
> Spring MVC注解,用于获取URL路径参数

- **作用**: 获取URL中的路径变量
- **位置**: Controller方法参数
- **示例**:
  ```java
  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
  }
  ```

## 5. @RequestBody
> Spring MVC注解,用于接收请求体数据

- **作用**: 接收POST/PUT请求的JSON数据
- **位置**: Controller方法参数
- **示例**:
  ```java
  @PostMapping
  public boolean createUser(@RequestBody User user) {
  }
  ```