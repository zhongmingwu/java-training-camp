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

