# Docker file for MySQL Database

## Quick Information

- Image: MySQL 8.0
- Port: 3306

## Environment Variables

```
ENV MYSQL_ROOT_PASSWORD=rootPass@123n
ENV MYSQL_DATABASE=campus_track
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=devServer@123n
```

## Getting Started
1. Build the docker image ``docker build -t campus-tracker-mysql .``
2. Run the container ``docker run -d -p 3306:3306 --name campus-tracker-mysql campus-tracker-mysql``
3. Enjoy ;D
