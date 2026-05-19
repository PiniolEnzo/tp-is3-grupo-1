# Test salida consola

**Objetivo**: Verificar que los resultados mostrados por consola sean correctos, completos y consistentes.

---

## Casos de Prueba

### 1. Chat Válido
- **Qué se espera probar**: Procesamiento de un flujo de chat estándar con múltiples participantes y mensajes de sistema.
- **Resultado esperado**: Cálculo correcto de mensajes por usuario, identificación del usuario más activo y extracción precisa de palabras y emojis.
- **Archivo**: `fixtures/chat_valido.txt`
- **Conclusión**: PASÓ. El sistema procesó correctamente 7 mensajes distribuidos entre Juan, Maria y Pedro. Se identificó correctamente a Juan como el más activo (3 msjs) y se generaron las estadísticas de día y hora correctamente.

### 2. Chat Vacío
- **Qué se espera probar**: Comportamiento del sistema ante un archivo que no contiene mensajes (solo el encabezado de cifrado).
- **Resultado esperado**: El sistema no debe crashear y debe reportar estadísticas en cero o vacías.
- **Archivo**: `fixtures/chat_vacio.txt`
- **Conclusión**: PASÓ. El sistema manejó el archivo vacío sin errores, devolviendo mapas vacíos y valores por defecto (0 mensajes, hora -1).

### 3. Formato Inválido
- **Qué se espera probar**: Resiliencia del parser ante líneas que no cumplen con el formato de WhatsApp.
- **Resultado esperado**: El sistema debe ignorar las líneas mal formadas, generar logs de advertencia (`WARN`) y procesar solo lo que sea válido.
- **Archivo**: `fixtures/chat_formato_invalido.txt`
- **Conclusión**: PASÓ. El parser ignoró correctamente las líneas de texto aleatorio, tratando el resultado como un chat sin mensajes válidos, evitando cualquier crash.

### 4. Un Solo Usuario
- **Qué se espera probar**: Generación de estadísticas cuando el chat contiene mensajes de un único participante.
- **Resultado esperado**: El usuario debe ser identificado como el más activo con el 100% de la participación.
- **Archivo**: `fixtures/chat_un_usuario.txt`
- **Conclusión**: PASÓ. Se procesaron correctamente los 3 mensajes de Juan, identificándolo como el único y más activo usuario.
