# client-service
Client Service Quarkus Project

## Compilacion

Se compararan los tiempos de los ejecutables

### Comparacion tiempos

#### Usando ejecutable nativo GraalVM
```mvn clean package -Dnative -Dskiptests=true```

-Compilación
![Imagen de compilación](./src/main/resources/static/nativo1.png)


#### Usando ejecutable nativo

```mvn clean package -Dnative -Dskiptests=true -Dquarkus.native.container.build=true```
