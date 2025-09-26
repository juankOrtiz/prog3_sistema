# Programacion 3 - Sistema

Ejemplo de sistema de gestión desarrollado con JavaFX para la interfaz de usuario y JDBC para la conexión a la base de datos. El código pertenece a la materia Programación III de la carrera Analista de Sistemas del IPESMI (Sede Oberá) - 2025.

## 🛠️ Requisitos previos

Para poder ejecutar este proyecto en tu computadora, necesitas tener instalado lo siguiente:

- Java Development Kit (JDK) 8 o superior: la versión usada en desarrollo fue la 24.0.2.
- Base de datos MySQL, version 8 o superior.
- IDE: el proyecto fue desarrollado con IntelliJ IDEA.

## 🚀 Pasos para la instalación local

1. Clonar el repositorio con el siguiente comando:

 ```sh
git clone https://github.com/juankOrtiz/prog3_sistema.git
```

2. Configurar la base de datos, utilizando el archivo ```db.sql``` para crear la base de datos y las tablas necesarias en el servidor local.

3. Configurar el proyecto en tu IDE: abre el proyecto con IntelliJ IDEA y configura la versión apropiada de la SDK, en caso de ser necesario.

4. Instalar las dependencias de Maven: asegurate que una vez instaladas figuren en la carpeta de librerías del proyecto.

5. Configurar la conexión a base de datos: crear una copia del archivo ```config.example.properties``` y renombrar como ```config.properties```, agregando las credenciales de la base de datos local

6. Ejecutar el proyecto: ejecuta el archivo ```Main.java``` y confirma que ves la ventana de login.
