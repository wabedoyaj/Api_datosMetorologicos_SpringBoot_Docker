# 🌦 API Meteorológica con Spring Boot

Este proyecto es una API REST desarrollada en Java con Spring Boot que proporciona datos meteorológicos actuales y pronósticos a través de la API de OpenWeatherMap. Además, cuenta con autenticación JWT, caché con Spring Boot, documentación con Swagger y está contenerizada con Docker.

### 🚀 Tecnologías utilizadas

    Java 17

    Spring Boot 3 (Spring Web, Spring Security, Spring Data JPA, Spring Cache)

    OpenWeatherMap API (para obtener los datos meteorológicos)

    JWT (JSON Web Tokens) para autenticación y seguridad

    MySQL para persistencia de datos

    Swagger para documentación interactiva

    Maven para la gestión de dependencias

    Docker para contenerización

# 📦 Instalación y configuración

1️⃣ Clonar el repositorio https://github.com/wabedoyaj/Api_datosMetorologicos_SpringBoot_Docker.git

     git clone 

2️⃣ Configurar la API Key de OpenWeatherMap

    Regístrate en OpenWeatherMap y obtén una API Key.

    Agrega tu API Key en src/main/resources/application.properties:

    weather.api.key=TU_API_KEY

3️⃣ Configurar la base de datos (Ejemplo con MySQL)

En application.properties, configura la conexión a la base de datos:

    spring.datasource.url=jdbc:mysql://db:3306/api_meteorologica
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update

4️⃣ Ejecutar la API

    mvn clean package
    mvn spring-boot:run

# 🔑 Autenticación JWT

Este proyecto usa JSON Web Tokens (JWT) para la autenticación de usuarios.

    Registrar un usuario

    POST /api/auth/register

    Ejemplo de cuerpo JSON:
    
        {
            "username": "usuario123",
            "password": "123456"
        }

Iniciar sesión y obtener un token

    POST /api/auth/login
    
    Ejemplo de respuesta:
    
        {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        }

Para acceder a los endpoints protegidos, incluye el token en la cabecera Authorization:

    Authorization: Bearer TU_TOKEN_AQUI

# 🌦️ Endpoints de la API (Swagger)

La API está documentada con Swagger. Puedes acceder a la documentación en:

http://localhost:9090/swagger-ui

Ejemplo de endpoints:

📍 Clima actual:

    GET /api/weather/current?city=London

📍 Pronóstico de 5 días:

    GET /api/weather/forecast/London

📍Datos de contaminación del aire:

    GET /api/weather/air-quality?city=London

📄 Para más detalles sobre los endpoints:

Consulta la documentación completa en Swagger en:

http://localhost:9090/swagger-ui

 # 🔐 Permisos de usuario y accesos

| Endpoint                               | Usuarios Registrados | Administradores  |
|----------------------------------------|----------------------|------------------|
| `/api/weather/current?city=London`     | ✅ Sí                | ✅ Sí            |
| `/api/weather/forecast/London`         | ✅ Sí                | ✅ Sí            |
| `/api/weather/air-quality?city=London` | ✅ Sí                | ✅ Sí            |
| `/api/cache`                           | ❌ No                | ✅ Sí            |
| `/api/audit/user/{username}`           | ❌ No                | ✅ Sí            |
| `/api/audit/alls`                      | ❌ No                | ✅ Sí            |

🔹 Usuarios estándar pueden acceder a datos meteorológicos.

🔹 Solo los administradores pueden gestionar caché y ver auditorías.


# 🐳 Contenerización con Docker

Para construir y ejecutar la API en un contenedor Docker:

1️⃣ Construir la imagen de Docker:

    docker build -t api-meteorologica .

2️⃣ Ejecutar el contenedor:

    docker run -p 9090:9090 api-meteorologica

Ahora la API estará disponible en:

http://localhost:9090/swagger-ui

# 📄 Documentación con Swagger

Swagger permite probar la API de forma interactiva.

Acceso a Swagger:

http://localhost:9090/swagger-ui

Acceso a la documentación JSON:

http://localhost:9090/v3/api-docs

# 📄 Documentación Completa:

Puedes descargar la documentación oficial en el siguiente enlace:  
[📑 Documentación Completa de la API](https://github.com/wabedoyaj/Api_datosMetorologicos_SpringBoot_Docker/releases/download/v1.0.0/Documentacion_API_Meteorologica.pdf)

## 📌 Autor

    Desarrollado por: 
                Wilson Alexis Bedoya Jajoy

## 📅 Fecha: 
                Marzo 2025

## 🚀 Contacto: 

    http://www.linkedin.com/in/wilson-bedoya-8b004b2a1

