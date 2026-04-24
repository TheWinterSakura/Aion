# Aion

**Aion** 是一款基于 **Jetpack Compose** 开发的现代化课程表工具。专为 Android 12+ 设计，致力于提供极致的系统集成体验与多样化的智能导入方案。

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
    *   支持调用大模型 Vision 能力进行课表深度解析。
    *   **注**：需用户在设置中配置自己的 **通义千问 (DashScope) API Key**。解析过程约 10 秒，能够自动处理复杂的图片布局并转化为标准课程数据。
*   **🏫 教务系统同步**：支持一键同步教务系统课表。目前仅适配 **[河北师范大学]**，更多学校持续开发中。
*   **📄 PDF 智能导入**：支持将学校下发的 PDF 版课程表直接解析并录入。
*   **👥 好友文件分享**：支持 Aion 专属格式文件（.aion）导出与导入，方便同班同学快速共享。

### 2. 系统深度集成
*   **🧩 Glance 桌面小组件**：基于 Jetpack Glance 打造，无需进入 App 即可在主屏幕实时查看当前及下一节课程。
*   **⏰ 精准课前提醒**：利用 AlarmManager 实现高精度提醒，支持自定义提醒时间，确保重要课程不遗漏。
*   **🎨 Material You 体验**：充分利用 MinSDK 31 特性，支持原生动效与现代交互规范。

---

## 📸 界面预览
| 课表主页 | 导入选项 | 桌面小组件 |
| :---: | :---: | :---: |
| ![Main](https://via.placeholder.com/200x400?text=Main+UI) | ![Import](https://via.placeholder.com/200x400?text=AI+Import) | ![Widget](https://via.placeholder.com/200x400?text=Glance+Widget) |

---

## ⚠️ 开发与适配说明
*   **学校适配**：教务系统同步功能目前仅在 **河北师范大学** 完成测试与适配。
*   **AI 配置**：请前往“通义千问”官网获取 API Key，并在应用的设置界面中填入以激活 AI 识图功能。

---

## 💬 反馈与建议
如果您在使用过程中遇到任何问题，或有更好的建议，欢迎通过以下方式进行反馈：
*   在本项目 [Issues](https://github.com/yourusername/Aion/issues) 页面提交问题。
*   直接联系开发者。

收到您的反馈后，我会尽快进行评估与修复，感谢您的支持。

---
**Aion — 让课程管理更智能。**
如果喜欢这个项目，欢迎给一个 ⭐️ Star！
