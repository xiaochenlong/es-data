## 安装启动elasticsearch
### 版本：8.7.1
### 修改配置文件config/elasticsearch.yaml 禁用https:
```
xpack.security.enabled: false
```
### 启动： ./bin/elasticsearch


### 查询教程
https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.operations.queries