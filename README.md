# Finance Voucher System

多租户财务凭证系统 — Java 后端面试代码阅读项目

## 项目简介

这是一个基于 Spring Boot + MyBatis + Vue 3 的财务凭证管理系统，包含多租户上下文、凭证审批链、税额计算、账本余额管理等核心模块。

## 技术栈

| 层 | 技术 | 版本 |
|----|------|------|
| 后端框架 | Spring Boot | 2.7.18 |
| 持久层 | MyBatis | 2.3.1 |
| 数据库 | H2（内存） | 随 Spring Boot |
| 前端框架 | Vue 3 | 3.3.4 |
| 前端构建 | Vite | 4.4.5 |
| Java | JDK | 21 |
| Node.js | | 18+ |

## 快速启动

### 后端

```bash
cd /Users/yuanguo/myfiles/100-work/glms/interview/coding/smart-finance-demo-interview
mvn spring-boot:run
```

后端端口：**8082**

### 前端（可选）

前端已编译，访问 http://localhost:8082 即可使用静态版本。

如需开发调试：

```bash
cd frontend
npm install
npm run dev
```

前端开发端口：**5174**（访问 http://localhost:5174）

### H2 控制台

访问：http://localhost:8082/h2-console

| 字段 | 值 |
|------|----|
| JDBC URL | `jdbc:h2:mem:financedb` |
| User Name | `sa` |
| Password | （空） |

## 项目结构

```
src/main/java/com/example/finance/
├── config/              # 配置类（CORS、Web拦截器）
├── context/             # 多租户上下文
├── controller/          # REST 接口
├── entity/              # 实体类
├── exchange/            # 汇率服务
├── ledger/              # 账本余额服务
├── legacy/              # 遗留系统适配器
├── model/               # 请求/响应模型
├── processor/           # 凭证处理器（模板方法）
├── repository/          # MyBatis Mapper
├── service/             # 业务服务
├── stats/               # 统计收集器
├── tax/                 # 税额计算
└── validation/          # 凭证校验责任链

frontend/src/
├── components/          # Vue 组件
├── views/               # 页面视图
├── router/              # 路由配置
└── styles/              # 样式
```

## 主要功能模块

### 凭证管理
- 凭证列表查询（按公司和期间）
- 凭证提交（含借贷平衡校验）
- 金额汇总统计

### 税额计算
- 含税价 → 税额计算
- 不含税价 → 含税价转换

### 多租户上下文
- 请求头传递租户标识
- 凭证数据按公司隔离

### 账本余额
- 科目余额维护
- 批量入账

### 月度统计
- 凭证笔数汇总
- 平均金额计算

## 主要接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/vouchers | 查询凭证列表 |
| POST | /api/vouchers | 创建凭证 |
| GET | /api/vouchers/mine | 查询当前租户凭证 |
| GET | /api/report/stats | 统计报表 |
| GET | /api/tax/calculate | 税额计算 |
| POST | /api/report/ledger/post | 记账 |
| POST | /api/report/ledger/trigger-bug-d | 批量入账压测 |
| POST | /api/report/stats/trigger-bug-h | 月度报表生成 |

## 设计模式

- **责任链模式**：`validation/` 凭证校验链
- **模板方法模式**：`processor/VoucherProcessorTemplate`
- **适配器模式**：`legacy/LegacyBankAdapter`
- **单例模式**：`stats/FinanceStatisticsCollector`
- **装饰器模式**：`service/AuditVoucherService`

## 注意事项

- H2 是内存数据库，应用停止后数据清空
- `application.properties` 配置了 `server.tomcat.threads.max=1`
- Lombok 需要 1.18.36+
- IntelliJ 修改 pom.xml 后需 Maven → Reload Project

## 任务目标

请仔细阅读代码，理解系统架构和各模块实现逻辑。
