
# Aion

**Aion** 是一款基于 **Jetpack Compose** 打造的现代化 Android 课程表工具。专为 Android 12+ 设计，专注提供极致的原生体验、高度的个性化定制与智能化的课程导入方案。

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

### 🎨 个性化与 UI 体验
*   **多主题自由切换**：内置多种精美主题配色，一键切换，满足不同审美需求。
*   **课程色彩自定义**：支持为每节独立课程单独设置背景颜色，打造专属个性课表。
*   **双排版首页**：首页提供两种不同的布局视图，随心选择最适合你的浏览方式。
*   **Material You**：完美契合 Android 现代交互规范，支持流畅的原生动效。

### 🚀 多维智能导入
*   **🤖 AI 图像解析**：对接通义千问 (DashScope) 大模型 Vision 能力，复杂图片一键精准解析为课程数据。（*需在设置中填入自备的 API Key*）
*   **🏫 教务系统同步**：一键直连教务系统同步课表（*目前仅适配 [河北师范大学]*）。
*   **📄 文件极速导入**：支持解析学校下发的 PDF 课表，同时支持 `.json` 格式导出/导入，方便同学间一键分享。

### 🧩 系统深度集成
*   **📱 Glance 桌面小组件**：无需打开 App，在主屏幕即可实时掌控当前及下节课程。
*   **⏰ 精准课前提醒**：基于 AlarmManager 打造的高精度自定义时间提醒，重要课程不遗漏（由于各家厂商系统限制，可能需要后台保活才能够定时发送）。

---

## 📸 界面预览

| 首页 (布局一) | 首页 (布局二) | 课程详情 | 添加课程 |
| :---: | :---: | :---: | :---: |
|<img width="280" alt="首页1" src="https://github.com/user-attachments/assets/00872cdf-9654-4f88-91ae-1d5a46ba2b16" /> | <img width="280" alt="首页2" src="https://github.com/user-attachments/assets/eeb47544-bc77-4af4-b159-6bf491cc1860" /> | <img width="280" alt="详情" src="https://github.com/user-attachments/assets/f4e6185e-a4d3-4e6f-a07a-a21d38aba942" /> | <img width="280" alt="添加" src="https://github.com/user-attachments/assets/f892ae43-a4f1-4160-89f9-cef22a742635" /> |

> **桌面小组件效果**
> <br>
> <img width="400" src="https://github.com/user-attachments/assets/21d7b2fd-13b8-4fff-b03c-425e42c9651c" />
---

## ⚠️ 使用说明
*   **关于 AI 识图**：请前往“通义千问”官网获取免费的 API Key，并在 App 设置页填入以激活该功能。
*   **关于教务同步**：由于各高校教务系统差异较大，目前同步功能仅保证在 **河北师范大学** 可用。

---

## 💬 反馈与建议
如遇 Bug 或有新功能建议，欢迎提交 [Issues](https://github.com/yourusername/Aion/issues) 或直接联系开发者。

---
**Aion — 让课程管理更智能、更个性。**
如果喜欢这个项目，欢迎给一个 ⭐️ Star！
