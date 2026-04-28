📊 WhatsApp Chat Analyzer

Aplicación web para el análisis estadístico de chats grupales exportados de WhatsApp, permitiendo visualizar métrtricas relevantes sobre la interacción entre los participantes mediante un dashboard interactivo.

🎯 Objetivo del Proyecto

Desarrollar una plataforma web que permita:

Cargar un archivo de chat exportado de WhatsApp (.txt)
Procesar la información contenida en el chat
Generar estadísticas automáticas sobre la actividad del grupo
Visualizar los resultados de forma clara e intuitiva mediante un dashboard interactivo

Este proyecto fue desarrollado como trabajo práctico académico, priorizando la lógica de procesamiento, la organización del proyecto y la correcta gestión del desarrollo por sobre la estética visual.

Referencia funcional inspirada en: WhatsAnalyze

🚀 Funcionalidades Implementadas

El sistema calcula y muestra como mínimo:

👤 Usuario con mayor cantidad de mensajes

Identificación del participante más activo dentro del chat.

😂 Emoji más utilizado

Detección y conteo de emojis más frecuentes.

⏰ Franja horaria con mayor actividad

Análisis de horarios con mayor volumen de mensajes.

📅 Días con mayor cantidad de mensajes

Agrupación y visualización de actividad por día.

☁️ Nube de palabras (Word Cloud)

Visualización de las palabras más repetidas dentro de la conversación.

🧩 Tecnologías Utilizadas
Backend
  Java
  Spring Boot
  Maven
Frontend
  HTML
  CSS
  JavaScript

📥 Cómo Exportar un Chat de WhatsApp

Desde WhatsApp:

Abrir el chat grupal
Ir a Más opciones
Seleccionar Exportar chat
Elegir Sin archivos multimedia
Guardar el archivo .txt

⚠️ El sistema trabaja con el formato estándar de exportación de WhatsApp.

Este trabajo prioriza especialmente:

Historial de commits claro y consistente
Uso correcto de ramas
Integración mediante merge
Organización modular del código
Separación de responsabilidades
Documentación técnica

La evaluación académica se enfoca principalmente en la gestión del desarrollo y la aplicación de buenas prácticas de ingeniería de software.

📚 Decisiones Técnicas
Separación Frontend / Backend

Se optó por una arquitectura separada para facilitar:

mantenimiento
escalabilidad
testing
trabajo colaborativo por ramas
Procesamiento por Parser

Se implementó un parser específico para el formato estándar de WhatsApp, permitiendo:

lectura eficiente
validación de estructura
limpieza de datos
normalización de mensajes
Dashboard Web

La visualización web permite una experiencia más intuitiva y accesible desde cualquier navegador sin necesidad de instalar software adicional.

👨‍💻 Integrantes
Enzo Ignacio Piñol 
Ramiro Enzo Bogado Leon 
Ana Belén Vázquez 
Dylan Thomas Lopez
