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

## 四、主要功能演示

### 凭证管理
1. 顶部切换公司（COMP-A / COMP-B）
2. 提交凭证表单输入金额
3. 查看凭证列表和汇总金额

### 税额计算
- 输入含税金额（分）和税率
- 点击"计算税额"查看结果

### 批量入账
- 点击"执行批量入账"模拟 50 个会计同时记账
- 查看预期余额 vs 实际余额对比

### 月度报表
- 点击"生成月度报表"模拟 30 笔凭证统计
- 查看预期笔数 vs 实际笔数对比

## 五、目录结构

```
.
├── README.md              # 项目说明
├── QUICKSTART.md          # 本文件
├── pom.xml                # Maven 配置
├── src/                   # 后端源码
│   └── main/
│       ├── java/          # Java 代码
│       └── resources/     # 配置和静态资源
├── frontend/              # 前端源码
│   ├── src/               # Vue 组件和页面
│   │   ├── components/    # 可复用组件
│   │   ├── views/         # 页面视图
│   │   ├── router/        # 路由配置
│   │   └── styles/        # 样式文件
│   ├── dist/              # 编译后的前端（已包含在 Spring Boot 中）
│   └── package.json       # npm 依赖
├── doc/                   # 文档
│   └── ENV-setup.md       # 环境说明
└── scripts/               # 辅助脚本
    └── trigger-bug-d.sh   # 并发测试脚本
```

## 六、技术栈

**后端**
- Spring Boot 2.7.18
- MyBatis 2.3.1
- H2 Database（内存数据库）
- Lombok 1.18.36

**前端**
- Vue 3.3.4
- Vue Router 4.6.4
- Vite 4.4.5

**要求**
- JDK 21+
- Maven 3.6+
- Node.js 18+（仅前端开发需要）

## 七、常见问题

**Q: 启动后端报错 "Lombok 找不到"**

A: 确保使用 Lombok 1.18.36+，并在 IntelliJ 中安装 Lombok 插件。

**Q: 数据重启后丢失**

A: H2 是内存数据库，应用重启后数据会重新初始化。这是正常行为。

**Q: 端口被占用**

A: 修改 `src/main/resources/application.yml` 中的 `server.port` 配置。

**Q: 前端开发模式跨域问题**

A: 已配置 CORS 和 Vite 代理，默认无需额外配置。
