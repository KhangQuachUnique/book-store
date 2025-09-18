# 🚀 BookieCake Development Setup

## Community Server Connector Configuration

Your VS Code workspace is now configured with the Community Server Connector for easy Tomcat server management with hyperlink display.

### 📋 Quick Start Guide

#### 1. **Server Setup**
- Open Command Palette (`Ctrl+Shift+P`)
- Type "Server Connector: Create New Server"
- Select "Download Server" → "Apache Tomcat" → Choose version
- Configure server location and settings

#### 2. **Deploy Application**
- Right-click on your project in Explorer
- Select "Run on Server" or "Deploy to Server"
- Choose your Tomcat server
- The application will be deployed as `BookieCake.war`

#### 3. **Hyperlink Access**
The following hyperlinks will be available in your terminal and output:

- **Application Home**: `http://localhost:8080/BookieCake`
- **Login Page**: `http://localhost:8080/BookieCake/login`
- **Books Catalog**: `http://localhost:8080/BookieCake/books`
- **Shopping Cart**: `http://localhost:8080/BookieCake/cart`
- **API Endpoints**: `http://localhost:8080/BookieCake/api/*`

#### 4. **VS Code Tasks**
Use the Command Palette (`Ctrl+Shift+P`) → "Tasks: Run Task":

- **Maven: Clean Build** - Compile the project
- **Maven: Package WAR** - Build deployable WAR file
- **Open BookieCake in Browser** - Launch application in browser
- **Deploy to Server** - Build and deploy to Tomcat

### 🔧 Features Enabled

#### **Terminal Hyperlinks**
- File links are enabled in terminal output
- Click on any `http://localhost:8080/BookieCake/*` URL to open in browser
- Stack trace file links for easy debugging

#### **Auto-Build Integration**
- Automatic Maven configuration updates
- Format on save enabled
- Organize imports on save
- Auto-save after 1 second

#### **Server Dashboard**
Open `server-links.html` in your browser for a visual dashboard with:
- Server status monitoring
- Quick navigation links
- Development tools access
- Real-time server health checks

### 🎯 Development Workflow

1. **Start Development**:
   ```
   Ctrl+Shift+P → "Server Connector: Start Server"
   ```

2. **Build & Deploy**:
   ```
   Ctrl+Shift+P → "Tasks: Run Task" → "Maven: Package WAR"
   ```

3. **Test Application**:
   ```
   Ctrl+Shift+P → "Tasks: Run Task" → "Open BookieCake in Browser"
   ```

4. **View Logs**:
   - Check the "Server Connector" output panel
   - Hyperlinks in logs are clickable for easy debugging

### 🔗 Hyperlink Features

- **Clickable URLs**: All `http://localhost:8080/BookieCake/*` URLs in terminal output
- **File Navigation**: Click on file paths in stack traces to open files
- **Auto-Preview**: Files open in preview mode for quick viewing
- **Browser Integration**: URLs automatically open in your default browser

### 📱 Mobile-Friendly Dashboard

The `server-links.html` dashboard is responsive and includes:
- Real-time server status
- Quick access to all application endpoints
- Visual feedback for server health
- Modern, professional interface

### 🛠️ Troubleshooting

**Server Not Starting?**
- Check Java installation: `java -version`
- Verify JAVA_HOME environment variable
- Ensure Tomcat is properly downloaded and configured

**Hyperlinks Not Working?**
- Restart VS Code to apply new settings
- Check that Community Server Connector extension is installed
- Verify terminal file links are enabled in settings

**Build Issues?**
- Run "Maven: Clean Build" task
- Check for compilation errors in Problems panel
- Verify all dependencies are resolved

---

**Happy Coding! 🎉**