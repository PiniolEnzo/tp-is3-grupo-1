# WhatsApp Chat Analyzer

Aplicación web para el análisis estadístico de chats grupales exportados de WhatsApp, permitiendo visualizar métricas relevantes sobre la interacción entre los participantes mediante un dashboard interactivo.

## Objetivo del Proyecto

Desarrollar una plataforma web que permita:

- Cargar un archivo de chat exportado de WhatsApp (.txt)
- Procesar la información contenida en el chat
- Generar estadísticas automáticas sobre la actividad del grupo
- Visualizar los resultados de forma clara e intuitiva mediante un dashboard interactivo

Este proyecto fue desarrollado como trabajo práctico académico, priorizando la lógica de procesamiento, la organización del proyecto y la correcta gestión del desarrollo por sobre la estética visual.

## Funcionalidades Implementadas

El sistema calcula y muestra como mínimo:

- **Usuario con mayor cantidad de mensajes**: identificación del participante más activo dentro del chat.
- **Emoji más utilizado**: detección y conteo de emojis más frecuentes.
- **Franja horaria con mayor actividad**: análisis de horarios con mayor volumen de mensajes.
- **Días con mayor cantidad de mensajes**: agrupación y visualización de actividad por día.
- **Nube de palabras (word cloud)**: visualización de las palabras más repetidas dentro de la conversación.

## Tecnologías Utilizadas

### Backend

| Tecnología   | Versión |
| ------------ | ------- |
| Java         | 21      |
| Spring Boot  | 3.5.14  |
| Maven        | -       |
| Lombok       | 1.18.38 |

### Frontend

| Tecnología     | Versión |
| -------------- | ------- |
| Next.js        | 16.2.6  |
| React          | 19      |
| TypeScript     | 5.7.3   |
| Tailwind CSS   | 4.2     |
| Recharts       | 2.15.0  |
| shadcn/ui      | -       |
| Lucide React   | 0.564   |

## Requisitos

Antes de ejecutar el proyecto, asegurate de tener instalado:

- **Java 21** o superior
- **Maven** (o usar el wrapper `mvnw` incluido en el proyecto)
- **Node.js** 18 o superior
- **npm** (incluido con Node.js)

## Estructura del Proyecto

```
whatsapp-analyzer/
├── frontend/                    # Aplicación frontend (Next.js)
│   ├── app/                     # Páginas y layouts (App Router)
│   ├── components/              # Componentes React (shadcn/ui)
│   ├── lib/                     # Utilidades y helpers
│   ├── services/                # Servicios de comunicación con API
│   └── styles/                  # Estilos globales
├── src/
│   └── main/
│       ├── java/com/group/whatsapp_analyzer/
│       │   ├── config/          # Configuraciones de la app
│       │   ├── controllers/     # Controladores REST
│       │   ├── dto/             # Objetos de transferencia de datos
│       │   ├── exceptions/      # Manejo de excepciones
│       │   ├── logger/          # Logging
│       │   ├── mapper/          # Mapeo entre entidades y DTOs
│       │   ├── model/           # Modelos de dominio
│       │   └── services/        # Lógica de negocio
│       └── resources/
│           └── application.properties
├── docs/                        # Documentación adicional
│   ├── test/                    # Archivos de prueba
│   └── Validación_de_métricas.txt
├── logs/                        # Logs de la aplicación
├── pom.xml                      # Configuración de Maven
├── mvnw / mvnw.cmd              # Maven wrapper
└── start-dev.bat                # Script para iniciar el proyecto
```

## Entregables de Gestión

- [Planilla de gestión (WBS, cronograma, estimación)](https://docs.google.com/spreadsheets/d/13MIaHewHIP--hEewH7AW_7CD-TREs-mQ/edit?usp=sharing&ouid=107355737136003291484&rtpof=true&sd=true)
- [Carpeta de Drive con documentación y video explicativo](https://drive.google.com/drive/folders/1-s5WsRnbbjhoey6unIAjz2E3j0oa_QWk?usp=drive_link)

## Cómo Ejecutar el Proyecto

### 1. Backend (Spring Boot)

```bash
# Compilar y empaquetar (opcional, saltea tests)
mvn clean install -DskipTests

# Iniciar el servidor
mvn spring-boot:run
```

El backend se levanta en `http://localhost:8080`.

### 2. Frontend (Next.js)

En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

El frontend se levanta en `http://localhost:3000`.

## Cómo Exportar un Chat de WhatsApp

Desde WhatsApp:

1. Abrir el chat grupal
2. Ir a _Más opciones_ → _Exportar chat_
3. Elegir _Sin archivos multimedia_
4. Guardar el archivo `.txt`

> ⚠️ El sistema trabaja con el formato estándar de exportación de WhatsApp.

## Gestión del Proyecto

Este trabajo prioriza especialmente:

- Historial de commits claro y consistente
- Uso correcto de ramas
- Integración mediante merge
- Organización modular del código
- Separación de responsabilidades
- Documentación técnica

## Decisiones Técnicas

### Separación Frontend / Backend

Se optó por una arquitectura separada para facilitar: mantenimiento, escalabilidad, testing y trabajo colaborativo por ramas.

### Procesamiento de los Archivos

Se implementó un parser específico para el formato estándar de WhatsApp, permitiendo: lectura eficiente, validación de estructura, limpieza de datos y normalización de mensajes.

### Dashboard Web

La visualización web permite una experiencia más intuitiva y accesible desde cualquier navegador sin necesidad de instalar software adicional.

## Integrantes

- [Ramiro Enzo Bogado León](https://github.com/RamiroBogado)
- [Ana Belén Vázquez](https://github.com/anavazquez141)
- [Dylan Thomas Lopez](https://github.com/Dylan-Lopez)
- [Enzo Ignacio Piñol](https://github.com/PiniolEnzo)



