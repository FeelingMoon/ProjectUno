# 🃏 ProjectUno - Juego de Cartas UNO en la Web

**ProjectUno** es un juego web interactivo basado en el clásico juego de cartas UNO, desarrollado en Java con JSF y PrimeFaces. La aplicación permite a los usuarios jugar partidas con una interfaz intuitiva y rica, completamente web.

---

## 🚀 Características Principales

- Simulación completa del juego UNO.
- Interfaz web moderna con PrimeFaces.
- Gestión de lógica de juego mediante Beans JSF.
- Sistema de persistencia básico con clases modelo para cartas y jugadores.
- Compatible con cualquier servidor de aplicaciones que soporte WAR (ej. Tomcat, Payara).

---

## 🛠️ Tecnologías Utilizadas

| Herramienta               | Versión     |
|---------------------------|-------------|
| Java                      | 11+         |
| Jakarta Faces (JSF)       | 3.0.0       |
| PrimeFaces                | 12.0.0      |
| PrimeFaces Extensions     | 12.0.7      |
| Guava (Google Core Libs)  | 31.1-jre    |
| Weld CDI (Servlet)        | 4.0.0.Final |
| Apache Commons IO         | 2.11.0      |
| Maven                     | Configurado |

---

## 📂 Estructura del Proyecto

```
ProjectUno/
├── src/main/java/co/edu/unbosque/beans/       # Lógica de juego (JSF Beans)
│     └── JuegoBean.java
├── src/main/java/co/edu/unbosque/persistence/ # Clases modelo
│     ├── Carta.java
│     └── CartaConverter.java
├── src/main/webapp/                           # Vistas JSF (.xhtml)
│     └── WEB-INF/
├── pom.xml                                    # Configuración Maven
└── README.md
```

---

## ⚙️ Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/usuario/ProjectUno.git
   cd ProjectUno
   ```

2. **Compilar el proyecto con Maven:**
   ```bash
   mvn clean package
   ```

3. **Desplegar el archivo WAR en un servidor de aplicaciones:**
   - Archivo generado: `target/ProjectUno.war`
   - Copiarlo al directorio `webapps/` de Tomcat o el servidor de tu elección.
   - Acceder en el navegador: `http://localhost:8080/ProjectUno`

---

## 💻 Interfaz Web

La aplicación utiliza **PrimeFaces** y **PrimeFaces Extensions** para ofrecer una experiencia de usuario avanzada, incluyendo:
- Tableros interactivos.
- Actualización dinámica con AJAX.
- Estilizado responsivo y moderno.
