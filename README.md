Este es un desarrollo de aplicación en Java con Swing que define un Sistema de Gestión de Vacaciones con el cálculo de prima vacacional con base en las actualizaciones a la LFT del año 2024.
las cuales efectúan su aplicación a partir del 01/01/2025 con la ampliación de días de periodo Vacacional al empleado, determinadas en los Artículos 76 al 81 de la Propia LFT.

Descripcion.
EL desarrollo para este programa, es relacionado con la falta de un sistema que le permita a la organización el poder llevar un registro de las Vacaciones de su personal, asi como el respectivo pago de las Primas Vacacionales conforme a la ley mexicana establecida, dentro de los Art. 76 al 81 de la Ley Federal del Trabajo actual y reformado el 27 de Diciembre de 2022.
La empresa requiere un sistema que le brinde el apoyo para poder llevar el registro dentro de su área de RH y brindar los pagos correspondientes a cada empleado.
La soluciona brindada para poder dar el apoyo a la organización, es la creación de un Sistema de Gestión de Vacaciones, el cual de el seguimiento y seguridad de que está siguiendo conforme a ley para con su personal laboral. Desarrollando dicho sistema con herramientas Java y un modelo MVC que sea amigable con el personal de RH.
Descripción del Programa
El programa es un Sistema de Gestión de Vacaciones desarrollado en Java que permite:
1.	Autenticación de usuarios (administradores y empleados regulares)
2.	Gestión de empleados (CRUD completo con validaciones)
3.	Gestión de usuarios del sistema (solo para administradores)
4.	Cálculo de vacaciones según la Ley Federal del Trabajo de México
5.	Cálculo de prima vacacional con porcentajes configurables
El sistema está diseñado para empresas que necesitan administrar los registros de empleados y calcular automáticamente sus derechos vacacionales según su antigüedad y salario.
Arquitectura del Sistema
El programa sigue una arquitectura Modelo-Vista-Controlador (MVC) con las siguientes capas:
1. Modelo (Models)
•	Database.java: Maneja la conexión a SQLite y creación de tablas
•	Employee.java: Modelo de datos para empleados con validaciones
•	User.java: Modelo de datos para usuarios del sistema
2. Controladores (Controllers)
•	AuthController.java: Maneja autenticación y gestión de usuarios
•	EmployeeController.java: Gestiona operaciones CRUD de empleados
•	VacationCalculator.java: Lógica de cálculo de vacaciones y prima vacacional
3. Vistas (Views)
•	LoginView.java: Interfaz de autenticación
•	AdminView.java: Panel de administración con todas las funcionalidades
•	EmployeeView.java: Vista limitada para empleados regulares
•	EmployeeForm.java: Formulario para crear/editar empleados
•	VacationCalculatorView.java: Interfaz especializada para cálculos de vacaciones
Características Arquitectónicas Clave
1.	Separación clara de responsabilidades: Cada componente tiene una única responsabilidad bien definida.
2.	Patrón DAO (Data Access Object): Los controladores actúan como intermediarios entre las vistas y los modelos.
3.	Seguridad:
o	Hash de contraseñas con BCrypt
o	Migración automática de contraseñas en texto plano
o	Validación de credenciales
4.	Validaciones:
o	Formato de NSS y CURP
o	Fechas coherentes
o	Salarios positivos
o	Restricciones de integridad
5.	Base de Datos SQLite:
o	Almacenamiento local en archivo
o	Transacciones para operaciones críticas
o	Inicialización automática con usuario admin por defecto
6.	Interfaz Gráfica (Swing):
o	Look and Feel consistente
o	Tablas con ordenamiento
o	Formularios con validación
o	Feedback visual al usuario
7.	Concurrencia:
o	Uso de SwingWorker para operaciones largas
o	EDT (Event Dispatch Thread) para actualizaciones de UI
Flujo Principal
1.	Inicio: La aplicación inicia con App.java que configura la base de datos y muestra el login.
2.	Autenticación: LoginView usa AuthController para validar credenciales.
3.	Redirección: Según el rol (admin/empleado), muestra AdminView o EmployeeView.
4.	Operaciones:
o	Los administradores pueden gestionar empleados y usuarios
o	Todos los usuarios pueden calcular vacaciones
5.	Persistencia: Todos los cambios se guardan en la base de datos SQLite.
Requerimientos
1. Requerimientos de Hardware
•	Mínimos:
o	Procesador: Dual Core 2.0 GHz o superior
o	Memoria RAM: 4 GB (8 GB recomendado para mejor rendimiento)
o	Almacenamiento: 500 MB de espacio libre (para aplicación y base de datos)
2. Requerimientos de Software
Entorno de Ejecución
•	Java Runtime Environment (JRE):
o	Versión mínima: Java 8 (1.8)
o	Versión recomendada: Java 11 LTS o superior
o	OpenJDK u Oracle JDK
Base de Datos
•	Sistema de base de datos:
o	SQLite (embebida - incluida en la aplicación)
o	Alternativas para implementación cliente-servidor:
	MySQL 5.7+ / MariaDB 10.3+
	PostgreSQL 12+
Servidores (para implementación web/nube)
•	Servidor de aplicaciones (opcional para versión web):
o	Apache Tomcat 9+
o	WildFly/JBoss 18+
o	Payara Server 5+
•	Servidor web (si se implementa frontend separado):
o	Nginx 1.18+
o	Apache HTTP Server 2.4+
3. Dependencias y Paquetes Adicionales
Bibliotecas Java (incluidas en el proyecto)
•	JBCrypt: Para hashing de contraseñas (ya incluido)
•	Controladores JDBC:
o	Para SQLite: sqlite-jdbc (ya incluido)
o	Para otros SGBD: MySQL Connector/J, PostgreSQL JDBC Driver
Dependencias del Sistema Operativo
•	Linux/Windows/macOS: Cualquier SO compatible con Java 8+
•	Paquetes adicionales:
o	Para Linux: libsqlite3-dev (si se compila manualmente)
o	Para Windows: Microsoft Visual C++ Redistributable (para algunas versiones de SQLite)
4. Requerimientos para Implementación Web (si se migra)
Opción 1: Arquitectura tradicional
•	Contenedor Java EE:
o	Jakarta EE 8+ compatible (GlassFish, Payara, WildFly)
o	Servidor de aplicaciones con soporte para JSP/Servlets
Opción 2: Arquitectura moderna
•	Backend:
o	Spring Boot 2.7+ (si se migra a este framework)
o	Java 17+ recomendado
•	Frontend:
o	Angular/React/Vue (si se separa el frontend)
o	Node.js 16+ (para construcción)
5. Requerimientos Adicionales
Para Desarrollo
•	IDE:
o	Eclipse, IntelliJ IDEA o NetBeans
•	Herramientas de construcción:
o	Maven 3.6+ (recomendado)
o	Gradle 7+ (alternativa)
Para Producción
•	Monitorización:
o	Java Mission Control (para profiling)
o	Prometheus + Grafana (para métricas)
•	Backup:
o	Sistema de backup para la base de datos SQLite
o	Cron jobs para copias de seguridad periódicas
6. Consideraciones Especiales
Para Implementación Empresarial
•	Red:
o	Acceso a red corporativa si es multi-usuario
o	Ancho de banda mínimo: 10 Mbps para 50 usuarios concurrentes
•	Seguridad:
o	Certificado SSL/TLS si se implementa web
o	Cortafuegos configurado para puertos de aplicación
Escalabilidad
•	Versión actual: Adecuada para 20-50 usuarios (debido a SQLite)
•	Versión escalable: Requeriría migración a:
o	Base de datos cliente-servidor (MySQL/PostgreSQL)
o	Arquitectura de microservicios para alta demanda


Instalación Ambiente de Desarrollo
Requisitos Previos
•	Java JDK 8+ (Recomendado: OpenJDK 11 o Oracle JDK 17)
•	Maven (para gestión de dependencias)
•	Git (para clonar el repositorio)
•	IDE (IntelliJ IDEA, Eclipse o VS Code)
Pasos para Configurar el Ambiente
1.	Clonar el repositorio (si está en GitHub/GitLab):
2.	Instalar dependencias con Maven:
3.	Configurar el IDE:
4.	Base de Datos SQLite (embebida):

Ejecución de Pruebas Manuales
Pruebas de Funcionalidad Básica
1.	Ejecutar la aplicación:
2.	Pruebas en modo Administrador:
3.	Pruebas en modo Empleado:
Pruebas de Base de Datos
•	Verificar que los datos persisten después de reiniciar la aplicación.
•	Probar con datos incorrectos (ejemplo: NSS inválido, fechas futuras).

Implementación en Producción
1.	Generar JAR ejecutable con Maven:
2.	Ejecutar en producción:
o	Asegurarse de que vacation system.db esté en el mismo directorio.
3.	Configurar como servicio (Linux/Windows):


Configuración del Producto (Archivos de Configuración)
Estructura de Archivos Clave
Archivo	Ubicación	Propósito
Database.java	src/main/java/models/	Configuración de conexión a BD (SQLite/PostgreSQL/MySQL).
pom.xml	Raíz del proyecto	Dependencias de Maven (JBCrypt, SQLite JDBC, etc.).
App.java	src/main/java/	Punto de entrada (configura Look & Feel y BD).
________________________________________
Configuración de la Base de Datos
SQLite (Por defecto)
•	URL de conexión:
java
private static final String URL = "jdbc:sqlite:vacation_system.db"; // Archivo local
•	Se crea automáticamente al iniciar la aplicación.
MySQL/PostgreSQL (Para producción)
1.	Modificar Database.java:
2.	Agregar dependencia en pom.xml:
xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.5.4</version>
</dependency>
________________________________________
Configuración de Usuarios Iniciales
•	Usuario admin por defecto:
o	Username: admin
o	Password: Admin (se hashea con BCrypt al iniciar la BD).
•	Personalización:
o	Editar createDefaultAdmin() en Database.java.
________________________________________Configuración de Requerimientos
Requerimientos Mínimos
Componente	Configuración
Java	JDK 8+ (OpenJDK 11 recomendado).
Memoria RAM	512 MB (1 GB recomendado).
Almacenamiento	100 MB para BD + aplicación.

Configuración para Entornos Específicos
•	Desarrollo Local
1.	Variables de entorno (opcional):
2.	Logs:

Personalización Avanzada
Cambiar Estilo de la Interfaz (Look & Feel)
•	Modificar App.java:
•	/ Ejemplo: Usar el tema "Nimbus" o "System"
•	UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

Configuración de Cálculo de Vacaciones
•	Editar VacationCalculator.java:
o	Ajustar días de vacaciones por antigüedad (Ley Mexicana).
o	Modificar porcentaje mínimo/máximo de prima vacacional (25%-100%).
