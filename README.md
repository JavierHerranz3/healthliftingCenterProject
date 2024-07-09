# Healthlifting Center

Healthlifting center es un proyecto con la finalidad de proporcionar una plataforma eficiente y organizada para la gestión de citas entre atletas y entrenadores. La aplicación permite a los usuarios crear, consultar, modificar y borrar información asociada a estas entidades de manera efectiva, mejorando la comunicación y la planificación entre los participantes. La plataforma está diseñada para ser robusta, fácil de usar y accesible desde diferentes dispositivos.
Características Principales
•	Gestión de Usuarios: Registro de entrenadores y atletas. Los usuarios pueden crear sus perfiles proporcionando información relevante como nombre, edad, documento, altura…
•	Gestión de Citas: Funcionalidades para la creación, visualización, edición y eliminación de citas. Los entrenadores y atletas pueden programar citas para sesiones de entrenamiento, ajustar detalles según sea necesario y cancelar citas si es necesario.
•	Gestión de Fichas de Entrenamiento: En cada cita se pueden vincular fichas de entrenamiento que contienen detalles sobre las actividades realizadas, observaciones y recomendaciones para futuras sesiones. Esto facilita el seguimiento del progreso y la planificación de entrenamientos personalizados.
•	Perfil de Usuarios: Información detallada de entrenadores y atletas, incluyendo historial de citas, y estadísticas relevantes (como número de citas realizadas.).
•	Perfil de Citas: Información detallada de cada cita, incluyendo la fecha, hora o fichas de entrenamiento asociadas.
•	Búsqueda y Filtrado: Funcionalidades avanzadas de búsqueda y filtrado que permiten a los usuarios encontrar citas, atletas y entrenadores según diferentes criterios como documento y tipo de entrenamiento.


## Comenzando 🚀

_Estas instrucciones te permitirán obtener una copia del proyecto en tu máquina local para propósitos de desarrollo y pruebas._

### Tecnologías utilizadas
_Lenguaje: Java 17_

_Framework: Spring Boot 3.1.9_

_BBDD: MongoDB_

_Librerias: Maven, Mapstruct, Lombok_

### Pre-requisitos 📋

Requisitos Previos
Antes de comenzar, asegúrate de tener los siguientes requisitos:

_Java Development Kit (JDK) 17_

_Maven_

_Git_

_MongoDB (si no deseas usar la base de datos embebida)_

### Instalación 🔧

_clona el repositorio_

```
git clone https://github.com/tu-usuario/demo-healthlifting.git
cd demo-healthlifting
```

_Compila el proyecto usando Maven_

```
mvn clean install
```

_El proyecto está configurado para usar una base de datos MongoDB embebida para propósitos de desarrollo. Si deseas conectarte a una base de datos MongoDB real, edita el archivo application.yml en el directorio src/main/resources con la configuración adecuada_

```
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: healthlifting-ddbb
      #username: user
      #password: pass
```

_Ejecución del proyecto_

```
mvn spring-boot:run

```

## Despliegue 📦

_Si quieres desplegar la aplicación en una máquina local. Usa Maven para empaquetar tu aplicación en un archivo JAR ejecutable_


```
mvn clean package

```

_Ejecuta el archivo JAR generado_


```
java -jar target/demo-healthlifting-0.0.1-SNAPSHOT.jar

```

## Construido con 🛠️

_Herramientas utilizadas en el proyecto_

* [Spring Boot](https://spring.io/projects/spring-boot) - El framework web usado
* [Maven](https://maven.apache.org/) - Manejador de dependencias
* [MongoDB](https://www.mongodb.com/es) - Base de datos NoSQL usada
* [Lombok](https://projectlombok.org/) - Usado para reducir el código boilerplate en Java.
* [Jackson](https://www.baeldung.com/category/json/jackson) - Usado para la manipulación de JSON.

## Arquitectura 🖇️

_Arquitectura Hexagonal_

_La arquitectura hexagonal, también conocida como arquitectura de puertos y adaptadores, es un patrón de diseño de software que tiene como objetivo principal separar las responsabilidades de cada componente de un sistema. Esta arquitectura se basa en la idea de que las aplicaciones deben ser independientes de la tecnología subyacente y, por lo tanto, fácilmente intercambiables. Se estructura de la siguiente manera_

```
o	Dominio
          o	Contiene la lógica de negocio central y las reglas.
                  o	Appointment, Athlete, PersonalInformation, TraininSheets…
o	Aplicación
          o	Coordina la lógica de negocio y maneja los casos de uso.
                  o	CoachServiceInputPort, AppointmentRepositoryOutputPort…
o	Infraestructura
          o	 Implementa interfaces y adaptadores necesarios para interactuar con sistemas externos.
                  o	AthleteRepository, AppointmentController, CoachEntity…

```


## Endpoints ✒

```
Athletes (Atletas)

	Post/athletes: Crear un nuevo atleta.
	Get/athletes: Obtener todos los atletas.
	Get/athletes/{id}: Obtener un atleta por su ID.
	Get/athletes/list/{document}: Obtener un atleta por su documento.
	Patch/athletes/{id}: Modificar un atleta por su ID.
	Delete/athletes/{id}: Eliminar un atleta por su ID.

Coaches (Entrenadores)

	Post/coaches: Crear un nuevo entrenador.
	Get/coaches: Obtener todos los entrenadores.
	Get/coaches/{id}: Obtener un entrenador por su ID.
	Get/coaches/list/{document}: Obtener un entrenador por su documento.
	Patch/coaches/{id}: Modificar un entrenador por su ID.
	Delete/coaches/{id}: Eliminar un entrenador por su ID.

TrainingSheets (Fichas de entrenamiento)

	Post/trainingsheets: Crear una nueva ficha de entrenamiento.
	Get/trainingsheets: Obtener todas las fichas de entrenamiento.
	Get/trainingsheets/{id}: Obtener una ficha de entrenamiento por su ID.
	Get/trainingsheets/athletes/{id}: Obtener las fichas de entrenamiento de un atleta por su ID. 
	Get/trainingsheets/coaches/{id}: Obtener las fichas de entrenamiento de un entrenador por su ID.
	Patch/trainingsheets/{id}: Modificar una ficha de entrenamiento por su ID.
	Delete/trainingsheets/{id}: Eliminar una ficha de entrenamiento por su ID.

Appointments (citas)

	Post/appointments: Crear una nueva cita.
	Get/appointments: Obtener todas las citas.
	Get/appointments/{id}: Obtener una cita por su ID.
	Get/appointments/athletes/{id}: Obtener las citas de un atleta por su ID. 
	Get/appointments /coaches/{id}: Obtener las citas de un entrenador por su ID.
	Get/appointments/coaches/searchByDocument{document}: Obtener las citas de un entrenador por su documento
	Get/appointments/athletes/searchByDocument{document}: Obtener las citas de un atleta por su documento
	Patch/ appointments/{id}: Modificar una ficha de entrenamiento por su ID.
	Delete/appointments/{id}: Eliminar una ficha de entrenamiento por su ID.

```

## Autor ✒️

* **Javier Herranz** - *Healthlifting Center* - [JavierHerranz3](https://github.com/JavierHerranz3)




