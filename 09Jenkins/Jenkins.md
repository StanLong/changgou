https://www.bilibili.com/video/BV1TY4y1a7Gn?p=1&vd_source=165a812497dd3d7dfba718ae4ef14867



```yaml
[root@node04 gitlab_docker]# cat docker-compose.yml 
version: '3.1'
services:
  gitlab:
    image: 'gitlab/gitlab-ce:latest'
    container_name: gitlab
    restart: always
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://192.168.235.14:8929'
        gitlab_rails['gitlab_shell_ssh_port'] = 2224
    ports:
      - '8929:8929'
      - '2224:2224'
    volumes:
      - './config:/etc/gitlab'
      - './logs:/var/log/gitlab'
      - './data/var/opt/gitlab'
```

