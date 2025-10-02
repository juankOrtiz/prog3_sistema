# Sistema JavaFX + JDBC

Este es un sistema de gestión desarrollado con JavaFX para la interfaz de usuario y JDBC (Java Database Connectivity) para la conexión a la base de datos, para la materia Programacion 3 del IPESMI (Sede Obera)

## 🛠️ Requisitos previos

Para poder ejecutar este proyecto en tu computadora, necesitas tener instalado lo siguiente:

- Java Development Kit (JDK) 8 o superior: la version usada en clases

    JavaFX SDK: El proyecto utiliza JavaFX para la interfaz gráfica. Necesitarás tener el SDK de JavaFX. Puedes descargarlo desde aquí.

    Base de datos: El proyecto se conecta a una base de datos a través de JDBC.

        Driver JDBC: Dependiendo de la base de datos que uses (por ejemplo, MySQL, PostgreSQL, SQLite), necesitarás el driver JDBC correspondiente. El driver debe ser incluido en la carpeta lib de tu proyecto o como una dependencia de tu IDE.

    IDE (Entorno de Desarrollo Integrado): Se recomienda usar un IDE como IntelliJ IDEA o Eclipse, ya que facilitan la configuración de las librerías de JavaFX y el driver JDBC.

🚀 Pasos para la instalación local

Sigue estos pasos para configurar y ejecutar el proyecto en tu máquina.

    Clonar el repositorio:
    Abre tu terminal o Git Bash y clona el repositorio a tu máquina local:
    Bash

git clone https://github.com/tu-usuario/nombre-del-repo.git

Configurar la base de datos:

    Crea una base de datos con el nombre nombre_de_la_db en tu sistema.

    Ejecuta el archivo script.sql (o el nombre que tenga tu script de base de datos) que se encuentra en la carpeta raíz del proyecto para crear las tablas y datos iniciales.

Configurar el proyecto en tu IDE:

    Abre tu IDE (IntelliJ IDEA o Eclipse) e importa el proyecto.

    Añadir las librerías de JavaFX y JDBC:

        JavaFX: Ve a la configuración de tu proyecto y agrega el SDK de JavaFX como una librería externa.

        Driver JDBC: Asegúrate de que el archivo .jar de tu driver JDBC esté incluido en las librerías de tu proyecto. Generalmente, esto se hace copiándolo a una carpeta lib y luego agregándolo a la ruta de clases del proyecto.

Configurar la conexión a la base de datos:

    Abre el archivo de configuración de la base de datos (por ejemplo, config/DatabaseConnection.java o similar).

    Actualiza la URL de la base de datos, el usuario y la contraseña según tu configuración local.

Java

private static final String DB_URL = "jdbc:mysql://localhost:3306/nombre_de_la_db";
private static final String USER = "tu_usuario";
private static final String PASS = "tu_contraseña";


*
5.  Ejecutar el proyecto:
    Una vez que todas las librerías estén configuradas y la conexión a la base de datos esté actualizada, puedes ejecutar la clase principal del proyecto (usualmente Main.java o App.java).

¡Y listo! Ya deberías ver la interfaz de usuario de la aplicación. Si tienes algún problema, por favor crea un issue en este repositorio.