<p align="center">
  <img width="125" height="125" alt="2" src="https://github.com/user-attachments/assets/dde8c384-e52d-4f5f-b3d9-d106d9e7667b" />
</p>

<h1 align="center">Aion — 智能课程表</h1>

<p align="center">
  <strong>基于 Jetpack Compose + MVVM 架构的现代 Android 课程管理应用</strong>
  <br />
  支持 AI 视觉识别导入、教务系统直连爬虫、桌面小组件、多课表管理
</p>

<p align="center">
  <img src="https://img.shields.io/badge/API-26%2B-brightgreen" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Kotlin-2.0+-blue" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Compose-Material%203-ff69b4" alt="Compose"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License"/>
</p>

---

## 📱 预览

| 周视图 | 日视图 | AI导入 | 小组件 |
|:---:|:---:|:---:|:---:|
| ![周视图](screenshots/grid_view.png) | ![日视图](screenshots/list_view.png) | ![AI导入](screenshots/ai_import.png) | ![小组件](screenshots/widget.png) |

---

## ✨ 功能特性

### 🗓️ 课表展示
- **双模式视图**：周视图（网格布局）与日视图（列表布局）一键切换
- **周次导航**：左右滑动切换周次，支持整学期课表概览
- **冲突自动检测**：Union-Find 算法自动识别并高亮时间冲突课程
- **自适应卡片**：课程卡片根据课时跨度自动调整高度，信息一目了然

### 🤖 AI 智能导入
- **图片识别**：对接通义千问 Vision 模型，拍照/截图导入课表，准确率 90%+
- **PDF 解析**：支持 PDF 格式课表解析，提取结构化课程数据
- **智能 Prompt 工程**：精心设计的系统提示词，确保 AI 输出严格 JSON，零人工干预
- **本地解析**：无需上传敏感数据，API Key 由用户自行配置

### 🌐 教务系统直连
- **WebView 登录**：内置浏览器直达教务系统，登录后一键抓取
- **JSoup 解析**：智能解析 HTML 表格，自动提取课程字段
- **预置高校列表**：支持多所高校（可扩展 CSV 配置）

### 📦 数据导入/导出
- **JSON 导入导出**：课程表与作息时间均支持 JSON 格式备份与恢复
- **批量操作**：支持按课程名/教师名快速批量编辑课程信息

### ⏰ 提醒与小组件
- **桌面小组件**：基于 Jetpack Glance 开发，支持亮/暗模式，自适应宽高显示今日课程
- **课前提醒**：AlarmManager 精确闹钟，兼容 Android 12-14 各版本权限要求
- **前台服务**：保证提醒可靠送达

### 🎨 主题与个性化
- **Material You**：完美适配 Android 12+ 动态主题色
- **8 套预设配色**：靛蓝、天蓝、青绿、草绿、橙、粉、紫、红
- **HSL 颜色选择器**：支持为每门课程自定义颜色，16 种预设色板快速选择
- **深色模式**：跟随系统设置自动切换

### 📚 多课表管理
- **多课表支持**：创建/重命名/切换多个课程表（如不同学期）
- **多作息时间表**：支持同时管理多套作息时间，灵活适配课程变动
- **学期起始设置**：自定义开学日期与总周数

---

## 🏗️ 架构设计

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                           │
│  ┌───────────┐  ┌───────────┐  ┌───────────────────┐   │
│  │ HomeScreen │  │  Settings  │  │  Import Screens   │   │
│  │ (Grid/List)│  │  (15+)     │  │ (AI/Web/JSON/PDF)│   │
│  └─────┬─────┘  └─────┬─────┘  └────────┬──────────┘   │
└────────┼───────────────┼─────────────────┼──────────────┘
         │               │                 │
┌────────┼───────────────┼─────────────────┼──────────────┐
│        ▼               ▼                 ▼               │
│                   ViewModel Layer                        │
│  ┌──────────┐ ┌────────────┐ ┌──────────────────────┐   │
│  │HomeVM    │ │SettingHVM  │ │IdentifyVM / SpiderVM  │   │
│  │Add/EditVM│ │15+ VMs     │ │Export/ImportVM        │   │
│  └────┬─────┘ └─────┬──────┘ └────────┬─────────────┘   │
└───────┼─────────────┼──────────────────┼─────────────────┘
        │             │                  │
┌───────┼─────────────┼──────────────────┼─────────────────┐
│       ▼             ▼                  ▼                  │
│                   Repository Layer                        │
│  ┌────────────────┐ ┌──────────────┐ ┌────────────────┐  │
│  │CourseRepo      │ │ScheduleRepo  │ │ PreferencesRepo│  │
│  │CourseTableRepo │ │TimeTableRepo │ │ (DataStore)    │  │
│  └───────┬────────┘ └──────┬───────┘ └────────┬───────┘  │
└──────────┼─────────────────┼──────────────────┼──────────┘
           │                 │                  │
┌──────────┼─────────────────┼──────────────────┼──────────┐
│          ▼                 ▼                  ▼           │
│                    Data Source Layer                       │
│  ┌────────────────┐ ┌──────────────┐ ┌────────────────┐  │
│  │  Room Database │ │ Retrofit API │ │  DataStore     │  │
│  │  (2 DBs, 4 DAOs)│ │ (AI + GitHub)│ │  (20+ Prefs)  │  │
│  └────────────────┘ └──────────────┘ └────────────────┘  │
└──────────────────────────────────────────────────────────┘
```

### 关键技术决策

| 决策 | 选择 | 理由 |
|------|------|------|
| **UI 框架** | Jetpack Compose + Material 3 | 现代声明式 UI，减少样板代码 |
| **架构** | MVVM + UDF + Repository | 关注点分离，可测试性强 |
| **依赖注入** | 手动 DI（AppContainer） | 项目规模适中，避免 Hilt/Dagger 编译开销 |
| **导航** | Navigation Compose（类型安全） | 编译时路由检查，支持 Serializable |
| **本地存储** | Room（2 DB）+ DataStore | 结构化数据用 Room，偏好用 DataStore |
| **网络** | Retrofit + OkHttp + Gson | 成熟稳定，拦截器机制强大 |
| **AI 集成** | DashScope API（Qwen 3.5+） | 国产大模型中文识别能力优秀 |
| **小组件** | Jetpack Glance | Compose 友好，声明式 Widget 开发 |
| **爬虫** | JSoup | 轻量级 HTML 解析，无需浏览器引擎 |

---

## 🛠️ 技术栈

```kotlin
// 核心语言
- Kotlin 2.0+      // 协程 + Flow + 序列化

// UI
- Jetpack Compose  // Material 3 + Animation + Pager
- Glance           // 桌面小组件

// 架构
- MVVM + UDF       // 单向数据流
- Repository       // 数据源抽象
- AppContainer     // 手动 DI

// 持久化
- Room             // 2 个数据库，6 张表
- DataStore        // 偏好存储

// 网络
- Retrofit + OkHttp  // RESTful API
- Gson + Kotlin Serialization

// AI
- 通义千问 Qwen 3.5+ (DashScope API)
- 视觉识别 + 文本解析

// 其他
- JSoup            // HTML 解析
- PDFBox Android   // PDF 文本提取
- Coil             // 图片加载
- Navigation Compose (类型安全路由)
```

---

## 📂 项目结构

```
app/
├── java/com/example/classschedule/
│   ├── MainActivity.kt                    # 单 Activity 入口
│   ├── ClassScheduleApplication.kt        # Application + DI 容器
│   ├── Screen.kt                          # 类型安全路由定义
│   ├── AppViewModelProvider.kt            # ViewModel 工厂
│   │
│   ├── analytical_method/                 # 教务系统解析
│   │   ├── CourseParser.kt
│   │   ├── DefaultSchoolParser.kt         # JSoup 解析器
│   │   └── ParserFactory.kt
│   │
│   ├── data/                              # 数据层
│   │   ├── course/                        # 课表数据库
│   │   ├── schedule/                      # 作息时间数据库
│   │   └── user_preferences/              # DataStore
│   │
│   ├── home_screen/                       # 主页 UI
│   ├── home_viewmodel/                    # 主页  ViewModel
│   ├── setting_screen/                    # 设置页 UI（16 个页面）
│   ├── setting_viewmodel/                 # 设置页 ViewModel
│   │
│   ├── ui/theme/                          # 主题系统
│   ├── ui/ColorPickerBottomSheet.kt       # HSL 颜色选择器
│   ├── ui/OnboardingGuide.kt             # 新手引导
│   │
│   ├── tools/                             # 工具类
│   ├── notifications/                     # 通知与提醒
│   ├── retrofit/                          # 网络层
│   ├── widget/                            # Glance 桌面组件
│   └── school/                            # 高校数据
│
├── assets/
│   └── table_school.csv                   # 高校列表
│
├── res/
│   ├── mipmap-*/                          # 应用图标
│   ├── drawable/                          # 背景图
│   ├── xml/                               # Widget / Backup 配置
│   └── values/                            # 字符串与主题
│
└── AndroidManifest.xml
```

---

## 🚀 快速开始

### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- Android SDK 34+
- Gradle 8.x
- JDK 17+

### 构建与运行

1. **克隆项目**
   ```bash
   git clone https://github.com/TheWinterSakura/ClassSchedule.git
   cd ClassSchedule
   ```

2. **配置 AI API Key（可选）**
   - 注册[阿里云百炼平台](https://bailian.console.aliyun.com/)
   - 获取 DashScope API Key
   - 在应用设置中输入 API Key 即可使用 AI 导入功能

3. **构建**
   ```bash
   ./gradlew assembleDebug
   ```

4. **运行**
   ```bash
   ./gradlew installDebug
   ```

### ⚙️ 首次使用

1. 启动应用，进入 **设置** → **开学日期**，设置本学期起始日期与总周数
2. 进入 **设置** → **作息时间**，添加课程时间段
3. 通过以下任一方式导入课表：
   - **AI 图片识别**：拍照或选取课表截图
   - **教务系统直连**：登录后一键抓取
   - **手动添加**：逐门录入课程信息
   - **JSON 导入**：从备份文件恢复

---

## 🤝 贡献指南

欢迎参与项目贡献！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feat/amazing-feature`)
3. 提交更改 (`git commit -m 'feat: add amazing feature'`)
4. 推送到分支 (`git push origin feat/amazing-feature`)
5. 发起 Pull Request

### 代码规范

- 遵循 [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- 使用 `ktlint` 进行代码格式化检查
- 单元测试覆盖率不低于 70%
- ViewModel 中不持有任何 Android 框架引用

---

## 📄 开源协议

本项目基于 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。


---

<p align="center">
  <sub>如果你觉得这个项目有帮助，请给一个 ⭐️ 吧！</sub>
</p>
