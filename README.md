# Stalker PDA Network Backend

![Deploy dev](https://github.com/artux-net/pdanetwork/actions/workflows/master.yml/badge.svg)
![DB Backup](https://github.com/artux-net/pdanetwork/actions/workflows/backup.yml/badge.svg)
![Test](https://github.com/artux-net/pdanetwork/actions/workflows/test.yml/badge.svg)
![Version](https://img.shields.io/github/v/tag/artux-net/pdanetwork?style=flat-square&label=version&color=blue)
[![Google Play](https://img.shields.io/badge/Google_Play-414141?style=flat-square&logo=google-play&logoColor=white)](https://play.google.com/store/apps/details?id=net.artux.pda&hl=ru)
[![Downloads](https://img.shields.io/badge/downloads-50K+-success?style=flat-square)](https://play.google.com/store/apps/details?id=net.artux.pda&hl=ru)

**Turn your smartphone into a real Stalker PDA!**

This is the backend service for the Stalker PDA Network - a multifunctional Android application that transforms your smartphone into an authentic stalker's handheld computer from the post-apocalyptic Zone.

## 🎯 About

The Stalker PDA Network Backend is a monolithic Spring Boot application that powers the mobile PDA experience. It handles authentication, news aggregation, real-time chat, zone encyclopedia, player rankings, story quests, and interactive quest processing.

## ✨ Features

### For Stalkers
- **🗨️ Thematic Chat System**: Communicate with other stalkers in faction chats, discuss Zone situations, engage in roleplay, exchange private messages, or create custom conversations
- **📰 Post-Apocalyptic News**: Stay updated with the latest news from Stalker, Metro, Survarium, Fallout, and other post-apocalyptic gaming projects
- **🗺️ Interactive Quests & Zone Map**: Complete faction missions, trader tasks, and stalker assignments while exploring the dangerous Zone filled with mutants, anomalies, and hostile humans
- **👤 Comprehensive Profile System**: Build your inventory, track faction relationships, gain experience, and participate in stalker leaderboards
- **📝 Dynamic Notes**: Create personal notes and receive automatic quest-related notifications during your Zone exploration

### Technical Features
- RESTful API with comprehensive Swagger documentation
- Real-time WebSocket communication for chat and live updates
- Quest scripting engine with story progression tracking
- User management with role-based access control
- Faction reputation and relationship system
- News aggregation and content management
- Player statistics and ranking systems

## 🌐 API Documentation & Services

### Development Environment

<a href="https://dev.artux.net/pdanetwork/swagger-ui/index.html">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" />
</a>
<a href="https://grafana.artux.net/d/twqdYjziz/micrometer-spring-throughput?orgId=1&var-application=&var-instance=dev.artux.net:80">
    <img src="https://img.shields.io/badge/Grafana-F2F4F9?style=for-the-badge&logo=grafana&logoColor=orange&labelColor=F2F4F9" />
</a>

- [User Management Panel](https://dev.artux.net/panel)

### Production Environment

<a href="https://app.artux.net/pdanetwork/swagger-ui/index.html">
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white" />
</a>
<a href="https://grafana.artux.net/d/twqdYjziz/micrometer-spring-throughput?orgId=1&var-application=&var-instance=app.artux.net:80">
    <img src="https://img.shields.io/badge/Grafana-F2F4F9?style=for-the-badge&logo=grafana&logoColor=orange&labelColor=F2F4F9" />
</a>

- **Access Requirements**: Registration required with TESTER role or higher for Swagger UI and Grafana
- **User Management Panel**: https://app.artux.net/panel

## 🎮 Quest Management

Quest management is handled through the web console at https://story.artux.net/ (use DEV environment credentials).

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL database
- Docker (optional, for containerized deployment)

### 📱 Mobile Application

Download the official Stalker PDA application:
- [Android - Google Play Store](https://play.google.com/store/apps/details?id=net.artux.pda&hl=ru)

<!-- Screenshots and images from Google Play Store -->
<!-- TODO: Add app screenshots here -->
<!--
Example format:
<div align="center">
<img src="path/to/screenshot1.png" width="200" alt="App Screenshot 1">
<img src="path/to/screenshot2.png" width="200" alt="App Screenshot 2">
<img src="path/to/screenshot3.png" width="200" alt="App Screenshot 3">
</div>
-->

## 🛠️ Development Environment

### Local Setup

1. **Start the database**: Run PostgreSQL using Docker Compose
   ```bash
   cd pdanet
   docker-compose up -d
   ```

2. **Run the application**: Start the Spring Boot application
   ```bash
   ./gradlew :pdanet:bootRun
   ```
   
   Or run directly from your IDE using the main class: `PDANetworkApplication.main()`

### Building

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew test

# Generate test coverage report
./gradlew jacocoTestReport
```

## 📋 Semantic Versioning

This project uses automatic semantic versioning. After each successful deployment, a new tag is created in the format `v1.2.3`.

### Version Management

**Automatic versioning by commits:**

- **PATCH** (v1.0.1): Regular commits, bug fixes
- **MINOR** (v1.1.0): Commits with `feat:`, `feature:`, `[minor]`
- **MAJOR** (v2.0.0): Commits with `BREAKING CHANGE`, `!:`, `[major]`

### Examples:

```bash
git commit -m "fix: fix login error"                    # → PATCH
git commit -m "feat: add new feature"                   # → MINOR  
git commit -m "feat!: critical API changes"             # → MAJOR
```

Tags are created after production deployment or when pushing to the main branch.

## 🏗️ Architecture

This project consists of two main modules:

- **`pdanet`**: Main Spring Boot application with REST API, WebSocket support, and business logic
- **`pdanet-model`**: Shared data models and enums used across the application

### Technology Stack

- **Backend**: Spring Boot 3.1.4, Kotlin, Java
- **Database**: PostgreSQL with Liquibase migrations
- **Security**: Spring Security with role-based access control
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Monitoring**: Micrometer with Prometheus metrics
- **Testing**: JUnit 5, TestContainers
- **Build**: Gradle with Kotlin DSL

## 🤝 Contributing

We welcome contributions from the Stalker community! Here's how you can help:

### Ways to Contribute

- 🐛 **Bug Reports**: Found a bug? Open an issue with detailed reproduction steps
- ✨ **Feature Requests**: Have an idea for new Zone features? Let us know!
- 🔧 **Code Contributions**: Submit pull requests for bug fixes or new features
- 📚 **Documentation**: Help improve our documentation and guides
- 🌍 **Localization**: Help translate the application to other languages

### Development Guidelines

1. **Fork the repository** and create a feature branch
2. **Follow coding standards**: We use Detekt for Kotlin code quality
3. **Write tests**: Ensure your code is well-tested
4. **Update documentation**: Keep the docs in sync with your changes
5. **Submit a pull request** with a clear description of your changes

### Code Style

- Use meaningful commit messages following [Conventional Commits](https://www.conventionalcommits.org/)
- Follow Kotlin coding conventions
- Ensure all tests pass before submitting PR
- Run `./gradlew detekt` to check code quality

## 📞 Contact & Support

### Community

- **📱 Telegram News**: [PDA Stalkers News](https://t.me/pda_stalkers) - Stay updated with the latest Zone news
- **💬 Telegram Chat**: [PDA Stalker Chat](https://t.me/pda_staker_chat) - Development discussions and community chat
- **📖 Reddit**: [r/stalker](https://www.reddit.com/r/stalker/)

### Development Team

- **Artux Team**: Contact us through [our website](https://artux.net)
- **Issues**: Use GitHub Issues for bug reports and feature requests
- **Email**: [maxim@artux.net](mailto:maxim@artux.net)

### 💖 Support the Project

Help us continue developing and maintaining this Zone experience:

<div align="center">

[![Donate](https://img.shields.io/badge/💰_Donate-FF5F5F?style=for-the-badge&logo=heart&logoColor=white)](https://dalink.to/prygunovx)

**💳 Cryptocurrency:**
- **USDT (TRC20)**: `TEQGHBP6rRJHHFiMBxZZ3ZUGrBCBWcAYnz`

</div>

Your support helps us:
- 🖥️ Maintain and improve servers
- 🎮 Add new quest content and features  
- 🐛 Fix bugs and improve performance
- 📱 Develop new features

## 📄 License

This project is licensed under [License Name] - see the [LICENSE](LICENSE) file for details.

<!-- Add LICENSE file with appropriate open source license -->

## 🙏 Acknowledgments

- **GSC Game World** - for creating the Stalker universe
- **The Stalker Community** - for inspiration and feedback
- **Contributors** - everyone who has contributed to this project
- **Beta Testers** - stalkers who helped test and improve the application

## 🗺️ Zone Map

```
Project Structure:
┌─ pdanetwork/
│  ├─ pdanet/              # Main Spring Boot application
│  │  ├─ src/main/java/    # Application source code
│  │  ├─ src/main/resources/ # Configuration and static files
│  │  └─ src/test/         # Test cases
│  ├─ pdanet-model/        # Shared data models
│  ├─ docker/              # Docker configurations
│  └─ .github/workflows/   # CI/CD workflows
```

---

**Good hunting, stalker! 🎯**
