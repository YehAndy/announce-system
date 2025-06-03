# Announce System

## Features
- 新增、修改、刪除公告
- 上傳附件
- 使用 SpringMVC + Hibernate + JSP + Bootstrap

## Build
```bash
mvn clean package
```

## Deploy on CentOS
1. 安裝 Java, MySQL, Tomcat
2. 建立資料庫 `announce_db`
3. 部署 WAR 至 Tomcat 的 `webapps` 目錄
4. 訪問：`http://your-server-ip:8080/announce-system`