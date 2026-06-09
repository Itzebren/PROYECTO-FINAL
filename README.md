# Code Slasher 🚀

**Code Slasher** es una innovadora versión del clásico juego "Fruit Ninja", adaptada con una temática de programación y desarrollo de software. Los jugadores deben "cortar" errores (bugs, nulls, errores) y recolectar elementos útiles (café, credenciales IPN) mientras evitan obstáculos en un entorno de laboratorio virtual.

Este proyecto integra una aplicación móvil nativa en Android con un backend robusto para la gestión de puntuaciones y rankings globales.

## 🏗️ Arquitectura del Proyecto

El sistema está dividido en dos componentes principales:

### 1. Frontend (Android Application)
Desarrollado íntegramente en **Kotlin** utilizando **Jetpack Compose** para una interfaz de usuario moderna y reactiva.

- **Motor de Juego (Game Engine):** Implementación personalizada de lógica de física, detección de colisiones (slash) y gestión de frames en tiempo real.
- **Gestión de Estado:** Arquitectura basada en estados inmutables (`FruitNinjaGameState`), donde cada frame del juego es una transformación del estado anterior.
- **Comunicación en Red:** Utiliza **Retrofit** para la sincronización de puntuaciones con el servidor backend de forma asíncrona.
- **Navegación:** Implementada mediante Compose Navigation para una transición fluida entre el menú principal, el juego y el panel de puntuaciones.

### 2. Backend (API REST)
Desarrollado con **FastAPI** (Python), enfocado en el alto rendimiento y la escalabilidad.

- **Servicios:** API RESTful para el registro de puntuaciones y consulta de rankings globales.
- **Persistencia de Datos:** Utiliza **SQLAlchemy** como ORM para la interacción con la base de datos, garantizando la integridad de los registros.
- **Contenedorización:** Configurado con **Docker** y **Docker Compose** para facilitar el despliegue y la consistencia del entorno de ejecución.

## 🛠️ Tecnologías Utilizadas

### Frontend
- **Lenguaje:** Kotlin 1.9+
- **UI:** Jetpack Compose
- **Network:** Retrofit / OkHttp
- **Inyección de Dependencias:** Patrón Repository / Manual Injection
- **Gráficos:** Canvas API (Compose)

### Backend
- **Lenguaje:** Python 3.10+
- **Framework:** FastAPI
- **ORM:** SQLAlchemy
- **Base de Datos:** MySQL 8.0
- **Servidor:** Uvicorn
- **Infraestructura:** Docker / Docker Compose

## 🎮 Funcionamiento del Juego

En **Code Slasher**, los elementos que aparecen en pantalla representan conceptos de programación:

- **Bugs, Errores y Nulos:** Deben ser eliminados para ganar puntos.
- **Café y Credenciales (IPN Card):** Elementos especiales que otorgan bonificaciones o vidas.
- **Bombas (Explosiones):** Representan fallos críticos del sistema; cortarlas resta vidas o termina la partida según la dificultad.

### Modos de Juego
- **Modo Clásico:** Supervivencia pura. Se pierde al fallar 3 cortes o tocar el café.
- **Modo Salvar el semestre:** Desafío contrarreloj de 60 segundos con bonus por combos.
- **Modo Relax:** Práctica libre de 90 segundos sin penalizaciones por caídas.

## 📂 Estructura del Proyecto

```text
PROYECTO-FINAL/
├── app/                # Aplicación Android (Frontend)
│   ├── src/main/java/  # Código fuente Kotlin
│   │   └── .../fruitninja/
│   │       ├── engine/ # Motor lógico del juego
│   │       ├── ui/     # Componentes Compose
│   │       └── data/   # Repositorios y API Service
│   └── res/            # Recursos (Imágenes, temas, strings)
├── backend/            # API REST (Backend)
│   ├── app/            # Lógica FastAPI (CRUD, Models, Schemas)
│   ├── Dockerfile      # Configuración de contenedor API
│   └── docker-compose.yml # Orquestación API + MySQL
└── README.md           # Documentación del proyecto
```

## 🚀 Instalación y Configuración

### Backend
1. Navegar a la carpeta `backend/`.
2. Ejecutar con Docker:
   ```bash
   docker-compose up --build
   ```
   La API estará disponible en `http://localhost:8000`.

### Frontend
1. Abrir el proyecto en **Android Studio**.
2. Sincronizar Gradle.
3. **Configuración de API:** Es posible que necesites ajustar la constante `BASE_URL` en `RetrofitGameService.kt` (o `RetrofitClient`) para que apunte a la dirección IP de tu máquina local si estás probando en un dispositivo físico.
4. Ejecutar en un emulador o dispositivo físico (API 26+ recomendado).

---
Desarrollado como parte de un proyecto final de programación móvil. 💻📱
