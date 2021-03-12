**Requisitos:**
- docker
- docker-compose
- OpenJDK 11

## Para iniciar os servidores
```aidl
docker-compose up
```

Links para os containers:
- RabbitMQ:  http://localhost:15672/ (user e password: guest)
  Queues: http://localhost:15672/#/queues/%2F/products-processor (so funciona apos iniciar a aplicacao pela primeira vez);
- Adminer: http://127.0.0.1:8000/ (user: user e password: dev)
- Por enquanto todos os arquivos serao salvos na pasta ./src/main/resource/files/ e no banco de dados respectivamente;

**Api Documentation:** http://localhost:8080/api/swagger-ui.html

**Para fazer o upload das Listas de Productos**: http://localhost:8080/api/file/



