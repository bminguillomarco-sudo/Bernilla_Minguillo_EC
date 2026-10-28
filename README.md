# Denuncias App - Inseguridad Ciudadana (Kotlin)

Aplicación Android nativa desarrollada en Kotlin para la gestión y seguimiento de denuncias de inseguridad ciudadana. Consume un servicio API REST mediante Retrofit2, OkHttp, Corrutinas de Kotlin, ViewModel y LiveData con interfaz basada en XML + ViewBinding y Material Components.

---

## 🚀 Requisitos del Sistema y Tecnologías
- **Lenguaje:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Compile SDK:** 37
- **Arquitectura:** MVVM (Model - View - ViewModel) + Repository
- **Librerías principales:**
  - `Retrofit2` & `GsonConverterFactory`
  - `OkHttp3` & `HttpLoggingInterceptor`
  - `AndroidX Lifecycle` (`ViewModel`, `LiveData`)
  - `KotlinX Coroutines`
  - `ViewBinding`
  - `Material Components` (`RecyclerView`, `CardView`, `TextInputLayout`, `FloatingActionButton`)

---

## 🛠️ Configuración de la API REST (`BASE_URL`)

La URL base de la API está centralizada en un solo archivo: `app/src/main/java/com/bernilla/denunciasapp/data/remote/Constants.kt`.

### Opciones de Configuración:
```kotlin
object Constants {
    // 1. Servidor en la nube (Render) - Configuración activa por defecto
    const val BASE_URL = "https://denunciaservice-bernilla.onrender.com/"

    // 2. Servidor Local probado desde Emulador Android
    // const val BASE_URL = "http://10.0.2.2:8080/"

    // 3. Servidor Local probado desde Celular Físico en la misma red Wi-Fi
    // (Reemplazar con la IP IPv4 de tu computadora)
    // const val BASE_URL = "http://192.168.1.50:8080/"
}
```

> **Nota sobre Render:** El servidor gratuito de Render entra en modo de reposo tras inactividad. Se han configurado timeouts de **90 segundos** en `RetrofitClient` para asegurar que las peticiones esperen el tiempo necesario mientras el servidor despierta.

---

## 📱 Pantallas y Funcionalidades

1. **MainActivity (Lista Paginada):**
   - Muestra las denuncias en formato `CardView` con etiquetas dinámicas según estado (Pendiente en Naranja, Atendida en Verde).
   - Carga inicial de 5 elementos (`offset=0&limit=5`).
   - Botón *"Cargar 5 siguientes"* para paginar sin reemplazar la lista actual.
   - Botón Flotante (FAB) para ir a registrar y botón de búsqueda en la barra superior.

2. **RegistrarActivity (Nueva Denuncia):**
   - Formulario para ingresar DNI (8 dígitos), Tipo (Dropdown/Spinner), Dirección y Descripción.
   - Generación automática de Fecha y Hora en formato ISO (`yyyy-MM-dd'T'HH:mm:ss`) de solo lectura.
   - Validaciones de campos obligatorios antes de realizar el `POST`.

3. **DetalleActivity (Ver y Atender):**
   - Muestra los detalles completos de la denuncia mediante `GET denuncias/{id}`.
   - Permite marcar la denuncia como atendida ejecutando `PUT denuncias/{id}` previa confirmación con `AlertDialog`.
   - Deshabilita el botón automáticamente si la denuncia ya fue atendida (estado 0).

4. **BuscarActivity (Búsqueda por DNI):**
   - Permite ingresar un DNI de 8 dígitos para consultar sus denuncias asociadas mediante `GET denuncias/ciudadano/{dni}`.
   - Reutiliza el `DenunciaAdapter` para listar los resultados.

---

## 💻 Cómo Ejecutar el Proyecto

1. Clona o abre la carpeta del proyecto en **Android Studio (Ladybug / Jellyfish o posterior)**.
2. Asegúrate de tener conexión a Internet para la descarga de dependencias y la conexión con Render.
3. Ejecuta **Gradle Sync**.
4. Selecciona un emulador Android (API 24+) o un dispositivo físico conectado mediante USB debugging.
5. Presiona **Run 'app'** (`Shift + F10`).
