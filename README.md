# Aion

**Aion** 是一款基于 **Jetpack Compose** 开发的现代化课程表工具。专为 Android 12+ 设计，追求极致的系统集成体验与智能导入流程。

---

## 🛠 技术栈
*   **UI**: Jetpack Compose (Material 3)
*   **架构**: MVVM + Repository + Kotlin Coroutines & Flow
*   **持久化**: Room + DataStore
*   **网络**: Retrofit
*   **桌面组件**: Jetpack Glance (Widget)
*   **提醒**: AlarmManager
*   **导航**: Navigation Compose
*   **最低支持**: Android 12 (API 31)

---

## ✨ 核心功能

### 1. 多维度课程导入
*   **🤖 AI 图像识别**：
    *   支持调用大模型（LLM/Vision）进行课表深度解析。
    *   **注**：需用户自行配置 API Key，解析过程约 10 秒，支持从图片提取复杂课程逻辑。
*   **🏫 教务系统同步**：支持一键抓取教务系统课表（目前仅适配 **[河北师范大学]**）。
*   **📄 PDF 智能导入**：自动解析 PDF 文档中的课程结构。
*   **👥 好友文件分享**：支持 Aion 专属格式文件导入，实现班级课表快速同步。

### 2. 系统深度集成
*   **🧩 Glance 桌面小组件**：基于 Jetpack Glance 实现，支持在桌面直接查看今日课表，数据随 App 实时同步。
*   **⏰ 精准课前提醒**：利用 AlarmManager 调度系统级通知，确保提醒准时送达。
*   **🎨 Material You 适配**：由于 MinSDK 为 31，应用深度适配 Android 12+ 的特性与交互。

---

## 📸 界面预览
| 课表主页 | 导入选项 | 桌面小组件 |
| :---: | :---: | :---: |
| ![Main](https://via.placeholder.com/200x400?text=Main+UI) | ![Import](https://via.placeholder.com/200x400?text=AI+Import) | ![Widget](https://via.placeholder.com/200x400?text=Glance+Widget) |

---

## ⚠️ 当前状态
*   **学校适配**：目前教务系统同步功能仅完成 **[河北师范大学]** 的适配，后续将支持更多学校。
*   **AI 配置**：使用 AI 识图功能前，请在设置中填入您的 千问apikey。

---

## 📄 开源协议
[MIT License](LICENSE)

---
**Aion — 让课程管理更智能。**
如果喜欢这个项目，欢迎给一个 ⭐️ Star！
