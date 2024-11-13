# API de Gestión de Clientes

Esta API es parte del sistema **Gestión de Clientes** de **Buenrostro Asociados**, diseñada para gestionar clientes, actividades contables, litigios, archivos, usuarios y roles en un entorno seguro y eficiente.

## Características

- **Autenticación y Autorización**: Soporte completo para autenticación de usuarios mediante JWT (JSON Web Tokens).
- **Gestión de Clientes**: Administración de clientes con información detallada y soporte para gestión de archivos y actividades.
- **Roles y Permisos**: Creación y administración de roles de usuario con permisos personalizados.
- **API RESTful**: Estilo de arquitectura RESTful para todas las operaciones.
- **Eventos Asíncronos**: Integración de eventos para realizar tareas de forma asíncrona (ej., notificaciones por correo electrónico).

## Tecnologías Utilizadas

- **Java**: Lenguaje de programación principal.
- **Spring Boot**: Framework para crear aplicaciones Java basadas en microservicios.
- **Spring Security**: Gestión de autenticación y autorización.
- **JWT**: Para el manejo de tokens de acceso y autenticación.
- **Amazon S3**: Integración para almacenamiento de archivos.
- **MySQL**: Base de datos relacional para almacenar información de clientes y otras entidades.
- **Swagger/OpenAPI**: Documentación de la API de forma interactiva.

## Requisitos Previos

Para ejecutar esta API, necesitarás tener los siguientes componentes instalados en tu máquina:

- **Java 17+** (JDK)
- **Maven 3.6+** (Para la gestión de dependencias y construcción del proyecto)
- **MySQL** o cualquier otra base de datos compatible configurada
- **AWS S3** (Si utilizas almacenamiento en la nube)
- **Postman** o cualquier cliente HTTP para realizar pruebas de la API.

## Instalación

1. **Clonar el repositorio**:

```bash
git clone https://github.com/tu-usuario/gestion-clientes.git

cd gestion-clientes

## Configuracion de archivo app.prop o yaml

Asegúrate de configurar las credenciales de tu base de datos, SMTP, y otros parámetros necesarios en el archivo src/main/resources/application.properts

ejemplo :
--------------------------------------------------------------------
# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_clientes
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Configuración de correos
spring.mail.host=smtp.tucorreo.com
spring.mail.username=tu_usuario@tucorreo.com
spring.mail.password=tu_contraseña

# Configuración de AWS S3
aws.accessKeyId=tu_access_key
aws.secretAccessKey=tu_secret_key
aws.s3.bucketName=tu_bucket
--------------------------------------------------------------------

## Ejecutart Applicacttion
Si tienes Maven instalado, puedes construir y ejecutar el proyecto con los siguientes comandos:
'''bash

mvn clean install
mvn spring-boot:run

*** o ssi se encuentran en linux  ***

mvn clean package -D skipTest


La API estara disponible en http://localhost:4200


## Endpoints Principales ##

- **Autenticación**:
POST /api/auth/signin: Iniciar sesión de usuario con credenciales.
POST /api/auth/signup: Registrar un nuevo usuario.

- **Clientes**:
GET /api/cliente: Obtener todos los clientes.
POST /api/cliente: Crear un nuevo cliente.
PUT /api/cliente/{id}: Actualizar información de un cliente.
DELETE /api/cliente/{id}: Eliminar un cliente.

-  **Actividades**:
GET /api/actividad-contable: Obtener todas las actividades contables.
POST /api/actividad-contable: Crear una nueva actividad contable.
GET /api/actividad-litigio: Obtener todas las actividades de litigio.
POST /api/actividad-litigio: Crear una nueva actividad de litigio.

- **Archivos**:
GET /api/archivo: Obtener todos los archivos asociados a clientes.
POST /api/archivo: Subir un nuevo archivo.
DELETE /api/archivo/{id}: Eliminar un archivo.

- **Seguridad**:
Esta API utiliza JWT para la autenticación y autorización. Asegúrate de incluir el token de autenticación en los encabezados de las solicitudes como sigue:
Authorization: Bearer {token}

## Swagger UI ##
La documentación interactiva de la API está disponible a través de Swagger en la siguiente URL:

	http://localhost:4200/api/docs/swagger-ui.html

visualizar la ruta en el archivo app.prop o yaml.

## Contribución

¡Gracias por tu interés en contribuir a este proyecto! Para mantener el proceso de contribución organizado, por favor sigue estos pasos:

1. **Haz un fork del repositorio**: Haz una copia del repositorio en tu cuenta de GitHub para poder trabajar en él sin afectar el repositorio original.
   - Haz clic en el botón de "Fork" en la esquina superior derecha de la página del repositorio.

2. **Clona tu fork**: Una vez que hayas hecho el fork, clona el repositorio a tu máquina local para poder hacer cambios.
   - Usa el comando:  
   ```bash
   git clone https://github.com/TU_USUARIO/gestion-clientes.git


## Licencia ##
Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.
