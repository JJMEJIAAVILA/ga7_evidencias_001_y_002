# Sistema de Gestión de Biblioteca

## Descripción del Proyecto

El **Sistema de Gestión de Biblioteca** es una aplicación de escritorio desarrollada en **Java** que permite administrar el catálogo de una biblioteca. El sistema facilita la gestión de diferentes tipos de elementos bibliográficos: **libros**, **revistas** y **DVDs**. Con esta aplicación, los bibliotecarios pueden realizar operaciones básicas como agregar, editar, eliminar y buscar elementos en el catálogo.

## Tecnologías Utilizadas

- **Java**: Lenguaje de programación principal
- **Swing**: Biblioteca gráfica para la interfaz de usuario
- **JDBC**: API para la conexión con la base de datos
- **MySQL**: Sistema gestor de base de datos
- **Patrón MVC**: Arquitectura de diseño utilizada (Modelo-Vista-Controlador)

## Estructura del Proyecto

El proyecto sigue una arquitectura de **tres capas**:

### 1. Capa de Modelo

Contiene las clases que representan los objetos del dominio y el acceso a datos:

- `ElementoBiblioteca`: Clase base para todos los elementos del catálogo
- `Libro`, `Revista`, `DVD`: Clases específicas para cada tipo de elemento
- Paquete `dao`: Clases para el acceso a la base de datos (Data Access Objects)

### 2. Capa de Controlador

Gestiona la comunicación entre la vista y el modelo:

- `BibliotecaController`: Controlador base con funcionalidad común
- `LibroController`, `RevistaController`, `DVDController`: Controladores específicos

### 3. Capa de Vista

Interfaces gráficas para la interacción con el usuario:

- `MainFrame`: Ventana principal de la aplicación
- `PanelPrincipal`: Panel contenedor para la navegación
- `PanelLibros`, `PanelRevistas`, `PanelDVDs`: Paneles específicos para cada tipo de elemento
- `DialogoAgregarElemento`, `DialogoDetallesElemento`: Diálogos para operaciones específicas

## Configuración de la Base de Datos

### Paso 1: Crear la Base de Datos

Ejecuta el siguiente script SQL para crear la estructura de la base de datos y datos de prueba:

1. Abre tu gestor de MySQL (MySQL Workbench, HeidiSQL, phpMyAdmin, etc.)
2. Crea una nueva conexión a tu servidor MySQL si es necesario
3. Ejecuta el script SQL proporcionado en el archivo `biblioteca.sql`

### Paso 2: Configurar la Conexión

Crea un archivo `config.properties` en la carpeta `src/main/resources/` con el siguiente contenido:

```properties
db.url=jdbc:mysql://localhost:3306/biblioteca
db.user=tu_usuario
db.password=tu_contraseña
```

1. Reemplaza tu_usuario y tu_contraseña con tus credenciales de MySQL.

2. Importante: El archivo config.properties contiene información sensible y no debe compartirse ni subirse a repositorios de código. Está incluido en el archivo .gitignore para evitar que se suba accidentalmente.

## Ejecución de la Aplicación
Para ejecutar la aplicación:

1. Asegúrate de tener instalado JDK 8 o superior

2. Compila el proyecto

3. Ejecuta la clase com.biblioteca.Main

La aplicación iniciará mostrando la pantalla principal desde donde podrás navegar a las diferentes secciones del sistema.

## Funcionalidades Principales
- Gestión de Libros: Agregar, editar, eliminar y buscar libros por título
- Gestión de Revistas: Agregar, editar, eliminar y buscar revistas por categoría
- Gestión de DVDs: Agregar, editar, eliminar y buscar DVDs por género
- Interfaz intuitiva: Navegación sencilla entre las diferentes secciones

## Diagrama de Clases Simplificado
```
ElementoBiblioteca
      ↑
┌─────┼─────┐
│     │     │
Libro Revista DVD

BibliotecaController<T>
      ↑
┌─────┼─────┐
│     │     │
LibroController RevistaController DVDController

ElementoBibliotecaDAO<T>
      ↑
┌─────┼─────┐
│     │     │
LibroDAO RevistaDAO DVDDAO
```

## Notas Adicionales
- El sistema utiliza transacciones para garantizar la integridad de los datos
- La seguridad de la conexión a la base de datos se maneja mediante el archivo de propiedades
- La aplicación implementa validaciones para evitar errores de usuario

## Requisitos del Sistema
- Java JDK 8 o superior
- MySQL 5.7 o superior
- 4GB de RAM mínimo
- 100MB de espacio en disco duro

## Para soporte
- Para soporte `joseluis836ps@gmail.com`
- Para ver mas proyectos revise github `joseluis1061`
