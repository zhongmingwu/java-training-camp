# 启动ActiveMQ

```bash
$ docker-compose up -d
Creating network "docker_default" with the default driver
Creating activemq ... done

$ docker-compose ps -a
  Name                Command               State                                             Ports
----------------------------------------------------------------------------------------------------------------------------------------------
activemq   /bin/sh -c bin/activemq co ...   Up      1883/tcp, 5672/tcp, 61613/tcp, 61614/tcp, 0.0.0.0:61616->61616/tcp, 0.0.0.0:8161->8161/tcp
```

# Queue模式
终端
```bash
$ curl 'http://localhost:8080/sendQueueMsg?id=1&name=Java'
```

控制台
```
queueConsumer2 receive message : Book(id=1, name=Java)
```

# Topic模式
修改`application.properties`
```yaml
spring.jms.pub-sub-domain=true
```

终端
```bash
$ curl 'http://localhost:8080/sendTopicMsg?id=2&name=Netty'
```

控制台
```
topicConsumer1 receive message : Book(id=2, name=Netty)
topicConsumer2 receive message : Book(id=2, name=Netty)
```
