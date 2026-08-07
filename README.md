# 快速启动指南

## 一、仅启动后端（推荐）

```bash
cd /Users/yuanguo/myfiles/100-work/glms/interview/coding/smart-finance-demo-interview
mvn spring-boot:run
```

启动后访问：http://localhost:8082

> 前端已编译为静态资源，直接访问后端端口即可使用完整功能。

## 二、启动前端开发模式（可选）

如果需要修改前端代码或调试：

```bash
cd /Users/yuanguo/myfiles/100-work/glms/interview/coding/smart-finance-demo-interview/frontend
npm install
npm run dev
```

访问：http://localhost:5174

## 三、H2 数据库控制台

访问：http://localhost:8082/h2-console

连接信息：
- JDBC URL: `jdbc:h2:mem:financedb`
- 用户名: `sa`
- 密码: （留空）

