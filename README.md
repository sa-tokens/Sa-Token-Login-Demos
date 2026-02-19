# 🎯 Sa-Token-Login-Demos

> 📚 一个**教学示例仓库**，通过极简代码演示 Sa-Token 在各种登录方式下的整合与写法  
> 每种登录方式 = 一个后端 Controller + 一个前端 HTML 页面，清晰直观，便于学习 ✨

---

## 🌟 核心理念

| 理念 | 说明 |
|------|------|
| **极简** 🎨 | 只写关键步骤，无关代码尽量省略 |
| **清晰** 📖 | 代码注释详尽，目录结构直观，快速理解 |
| **独立** 🧩 | 每种登录方式相互独立，互不干扰 |

---

## 🚀 已支持的登录方式

| 登录方式 | 后端 Controller | 前端页面 | 状态 |
|----------|-----------------|----------|------|
| 账号密码登录 | `LoginByPasswordController` | `login-by-password.html` | ✅ 已实现 |
| 手机号验证码登录 | `LoginByPhoneCodeController` | `login-by-phone-code.html` | ✅ 已实现 |
| 图形验证码登录 | - | - | 📋 待开发 |
| 邮箱验证码登录 | - | - | 📋 待开发 |
| 扫码登录 | - | - | 📋 待开发 |
| 邮箱点击链接注册登录 | - | - | 📋 待开发 |

---

## 🛠️ 技术栈

### 🔧 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 开发语言 |
| Spring Boot | 3.5.10 | Web 框架 |
| Sa-Token | 1.44.0 | 轻量级权限认证框架 |
| Redis | - | 会话持久化（分布式支持） |
| Lombok | - | 简化样板代码 |
| Maven | - | 项目构建 |

### 🎨 前端

| 技术 | 说明 |
|------|------|
| HTML + CSS + JavaScript | 纯静态，无需 Node.js 构建 |
| jQuery | Ajax 通信 |
| layer | 弹窗 / 提示 |
| sa.js | 公共工具封装 |
| 本地依赖 | 所有库使用本地文件，不使用 CDN |

---

## 📁 项目结构

```
sa-token-login-demos/
├── stld-server/                    # 🔙 后端（Spring Boot）
│   ├── src/main/java/com/pj/stld/
│   │   ├── controller/             # 控制器层（LoginByXxxController）
│   │   ├── services/               # 业务逻辑层
│   │   ├── mock/                  # 模拟层（模拟数据库 / 短信服务）
│   │   ├── model/                 # 实体类（SysUser 等）
│   │   ├── config/                # 全局配置
│   │   └── utils/                 # 工具类
│   ├── src/main/resources/
│   │   └── application.yml        # 配置文件
│   └── pom.xml
│
├── stld-html/                      # 🖼️ 前端（纯 HTML）
│   ├── login-pages/               # 各种登录页
│   │   ├── login-by-password.html
│   │   └── login-by-phone-code.html
│   ├── static/
│   │   ├── css/                   # 样式文件
│   │   ├── kj/                    # jQuery、layer 等第三方库
│   │   └── sa.js                  # 公共工具（所有页面需引入）
│   ├── index.html                 # 主页（需登录）
│   └── login-select.html          # 登录方式选择页
│
└── README.md
```

---

## ⚡ 快速开始

### 1️⃣ 环境要求

- **JDK 17+**
- **Maven 3.x**
- **Redis**（默认 `127.0.0.1:6379`，数据库索引 1）

### 2️⃣ 启动后端

```bash
cd stld-server
mvn spring-boot:run
```

后端默认运行在 **http://localhost:8081** 🎉

### 3️⃣ 访问前端

前端为纯静态 HTML，可直接用浏览器打开，或用任意静态服务器托管：

```bash
# 方式一：直接用浏览器打开
# 打开 stld-html/login-select.html

# 方式二：使用 Python 起一个简单服务器（示例）
cd stld-html
python -m http.server 8080
# 访问 http://localhost:8080/login-select.html
```

> ⚠️ **注意**：前端 Ajax 会请求 `localhost:8081`，需确保后端已启动；若端口或地址不同，需修改前端的 `sa.js` 或接口地址配置。

### 4️⃣ 测试账号

登录方式选择页会展示**系统用户列表**，包含测试用的账号、密码、手机号等，便于本地联调。

手机号验证码登录在 `stld.sms.test-mode: true` 时，验证码固定为 **123456**。

---

## 📝 开发规范

- 登录相关 Controller 命名：`LoginByXxxController`（如 `LoginByPasswordController`）
- 对应前端页面命名：`login-by-xxx.html`（如 `login-by-password.html`）
- 新增登录方式时，在后端 `controller/` 和前端 `login-pages/` 各新增一个文件即可

---

## 📚 相关链接

- [Sa-Token 官方文档](https://sa-token.cc) 📖
- [Sa-Token Gitee](https://gitee.com/dromara/sa-token) 🇨🇳
- [Sa-Token GitHub](https://github.com/dromara/sa-token) 🐙
- [Sa-Token AtomGit](https://atomgit.com/dromara/sa-token) ⚛️

---

## 📄 开源协议

本项目仅供学习与参考使用。⭐ 如果对你有帮助，欢迎 Star 支持~
