# Build 階段，使用 node 最新官方映像
FROM node:20-alpine AS build

WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm install

COPY . .

RUN npm run build

# Production 階段，使用 nginx 來服務靜態頁面
# FROM nginx:alpine

# COPY --from=build /app/dist /usr/share/nginx/html

# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
