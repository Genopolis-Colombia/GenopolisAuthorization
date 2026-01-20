# Genopolis Colombia Auth-Microservicio

# Descripción del Proyecto

En el ámbito biotecnológico y científico, la gestión y análisis de información sobre proteínas son fundamentales para avanzar en la investigación y el desarrollo de nuevas tecnologías y terapias. En Colombia, sin embargo, no existe una plataforma web dedicada a la gestión de proteínas ni a la oferta de información biotecnológica especializada. Esta carencia representa un desafío significativo para investigadores, científicos y profesionales de las áreas de ciencias exactas, de la salud, entre otras, quienes enfrentan la dificultad de acceder a herramientas integradas y accesibles para la gestión de datos proteicos.
Genopolis surge como una solución integral y accesible para abordar estos problemas. La plataforma permitirá a los usuarios buscar, gestionar y analizar información sobre proteínas de manera eficiente, centralizando datos clave en una única interfaz. Este repositorio corresponde al microservicio Proteins, un API Rest que mantiene toda la información relacionada con las proteínas, incluyendo el formato FASTA.

# Características Principales

- Arquitectura de Adaptadores y Puertos: El microservicio implementa una estructura de subproyectos utilizando Gradle para garantizar una arquitectura limpia y mantenible.
- Pruebas de Integración con Testcontainers: Se utilizan Testcontainers para las pruebas de integración, asegurando que el microservicio funcione correctamente en un entorno similar al de producción.
- Gestión de Proteínas: Centraliza la información relacionada con proteínas, incluyendo secuencias en formato FASTA.
- Base de Datos MySQL: Utiliza MySQL para almacenar y gestionar la información de manera eficiente.

# Beneficios de Genopolis

- Optimización de la Investigación: Facilita el acceso a información crítica sobre proteínas, permitiendo a los investigadores centrarse en el análisis y desarrollo en lugar de buscar datos dispersos.
- Colaboración Mejorada: Ofrece una base de datos centralizada que fomenta la colaboración entre investigadores al permitir el intercambio de información y resultados.
- Actualización y Gestión Eficientes: Permite la actualización continua de la base de datos con nuevos descubrimientos y la gestión efectiva de datos existentes, asegurando que la información esté siempre actualizada y relevante.
- Acceso Ampliado: Proporciona una plataforma accesible para usuarios en toda Colombia, democratizando el acceso a recursos biotecnológicos de alta calidad y promoviendo la igualdad de oportunidades en la investigación.

## Set up
* Instalar Docker
* Instalar gradle
* Instalar MySQL

# Configuración de la base de datos

1. Ingrese a la consola de MySQL:
```
mysql -u root -p
```
2. Crear la base de datos:
```
create database authorization_genopolis;
```
3. Crear usuario para spring:
```
create user 'springuser'@'%' identified by "p4t1App";
```
4. Dar permisos al nuevo usuario sobre la base de datos creada:
```
grant all on authorization_genopolis.* to 'springuser'@'%';
```

## Ejecución de la aplicación

Para ejecutar la aplicación, sigue los siguientes pasos:

1. Clonar el repositorio
```
git clone https://github.com/genopolis/genopolis-colombia-api.git
cd genopolis-colombia-api
```
2. Construir el proyecto con Gradle
```
./gradlew build
```
3. Ejecutar la aplicación
```
./gradlew bootRun
```
4. Ejecutar las pruebas de integración
```
./gradlew test
```

# Contribución

¡Contribuciones son bienvenidas! Si deseas contribuir al proyecto, por favor sigue los siguientes pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama (git checkout -b feature/nueva-funcionalidad).
3. Realiza tus cambios y commitea (git commit -am 'Añade nueva funcionalidad').
4. Haz push a la rama (git push origin feature/nueva-funcionalidad).
5. Abre un Pull Request.

# Contacto

Si tienes alguna pregunta o sugerencia, no dudes en contactarnos a través de [correo electrónico].

---
**Genopolis Colombia API - Microservicio Proteins**
*Innovando en la gestión de información biotecnológica para un futuro mejor.*