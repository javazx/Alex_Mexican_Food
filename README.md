# Alex Mexican Food - Android POS App

Aplicación Android de punto de venta (POS) para gestionar productos, carrito y ventas de Alex Mexican Food. Este documento ofrece una introducción completa, cómo instalar y ejecutar, versiones utilizadas, arquitectura y recursos.

## Introducción
- Objetivo: facilitar la gestión de catálogo de productos, ventas y su historial desde un dispositivo Android.
- Público: pequeñas y medianas operaciones que requieren un POS sencillo y offline.
- Tecnologías: Android nativo (Java), AndroidX, SQLite, ViewBinding.

## Características
- CRUD de productos: alta, edición, listado y eliminación.
- Carrito de compras: agregar/quitar productos, cálculo de totales.
- Ventas: confirmación de venta y detalle de artículos vendidos.
- Historial: consulta de ventas anteriores y detalle de cada venta.

## Arquitectura General
- Actividades para pantallas principales (menú, productos, carrito, ventas).
- Adaptadores para listas/RecyclerView.
- Modelos de datos (Producto, Carrito, Venta, Item de venta).
- Persistencia local con SQLite mediante `DatabaseHelper`.
- ViewBinding activado para acceso seguro a vistas.

## Clases Principales (Java)
Ubicación: `app/src/main/java/com/example/alexmexicanfood/`
- `MainActivity.java`: Pantalla inicial y navegación al menú.
- `MenuActivity.java`: Menú principal (opciones de gestión y venta).
- `ProductListActivity.java`: Lista de productos (usa `ProductAdapter`).
- `ProductFormActivity.java`: Alta/edición de productos (con `DatabaseHelper`).
- `CartActivity.java`: Carrito de compra (usa `CartAdapter`).
- `SaleDetailActivity.java`: Detalle de una venta (usa `SaleItemAdapter`).
- `SalesHistoryActivity.java`: Historial de ventas (usa `SalesHistoryAdapter`).

### Adaptadores
- `ProductAdapter.java`, `MenuItemAdapter.java`, `CartAdapter.java`, `SaleItemAdapter.java`, `SalesHistoryAdapter.java`.

### Modelos de Datos
- `Producto.java`, `Cart.java`, `Sale.java`, `SaleItem.java`.

### Persistencia
- `DatabaseHelper.java`: acceso a SQLite (tablas y CRUD).

## Recursos (`res`)
Ubicación: `app/src/main/res/`
- Layouts: `activity_main.xml`, `activity_menu.xml`, `activity_product_list.xml`, `activity_product_form.xml`, `activity_cart.xml`, `activity_sale_detail.xml`, `activity_sales_history.xml`, `item_product.xml`, `item_menu_product.xml`, `item_cart.xml`, `item_sale.xml`.
- Drawables: `amf_logo.png`, `ic_cart.xml`, `ic_launcher_background.xml`, `ic_launcher_foreground.xml`.
- Mipmap: `mipmap-anydpi-v26/ic_launcher.xml`, `ic_launcher_round.xml`.
- Values: `colors.xml`, `strings.xml`, `themes.xml`.
- Manifiesto: `AndroidManifest.xml` (declaración de actividades y permisos).

## Versiones Utilizadas
- Android Gradle Plugin (AGP): `8.13.0` (en `build.gradle` raíz).
- Gradle Wrapper: `9.0-milestone-1` (en `gradle/wrapper/gradle-wrapper.properties`).
- Compile SDK: `34`.
- Target SDK: `34`.
- Min SDK: `24`.
- Lenguaje Java: `sourceCompatibility/targetCompatibility = 1.8`.
- AndroidX: `android.useAndroidX=true`, `android.enableJetifier=true`.
- Principales dependencias:
  - `androidx.appcompat:appcompat:1.6.1`
  - `com.google.android.material:material:1.11.0`
  - `androidx.constraintlayout:constraintlayout:2.1.4`
  - `androidx.cardview:cardview:1.0.0`
  - `androidx.recyclerview:recyclerview:1.3.2`
  - Tests: `junit:4.13.2`, `androidx.test.ext:junit:1.1.5`, `espresso-core:3.5.1`

## Requisitos Previos
- Android Studio reciente compatible con AGP 8.x.
- JDK 17+ instalado y configurado (`JAVA_HOME`).
- Android SDK instalado (incluye Plataforma 34). El archivo `local.properties` debe apuntar a `sdk.dir` (ejemplo: `C:\Users\israe\AppData\Local\Android\Sdk`).
- Emulador o dispositivo físico con depuración USB.

## Instalación
1. Clona el repositorio: `git clone <URL_DEL_REPO>`.
2. Abre Android Studio → "Open" → selecciona el directorio del proyecto.
3. Espera a que Gradle sincronice las dependencias.
4. Verifica el SDK 34 instalado (Android Studio → SDK Manager).

## Ejecución
- Desde Android Studio: selecciona un dispositivo/emulador y pulsa "Run 'app'".
- Por línea de comandos:
  - Windows: `gradlew.bat assembleDebug`
  - macOS/Linux: `./gradlew assembleDebug`
- Instala y ejecuta el APK generado (`app/build/outputs/apk/debug/`).

## Pruebas
- Unit tests: `./gradlew test`.
- Instrumentation tests (requiere dispositivo): `./gradlew connectedAndroidTest`.

## Flujo Típico de Uso
1. `MainActivity` → `MenuActivity`.
2. `ProductListActivity` para ver productos.
3. `ProductFormActivity` para crear/editar.
4. `CartActivity` para revisar y confirmar compra.
5. `SaleDetailActivity` para detalle de venta.
6. `SalesHistoryActivity` para histórico.

## Solución de Problemas
- Error `JAVA_HOME is not set`: configura `JAVA_HOME` a tu JDK 17.
- Error de sincronización por Gradle: asegúrate de descargar el wrapper (`gradle-9.0-milestone-1`) o cambia a una versión estable si prefieres (por ejemplo: `./gradlew wrapper --gradle-version 8.9`).
- Falta SDK 34: instala la plataforma desde SDK Manager.
- `local.properties` ausente: Android Studio lo genera automáticamente al abrir el proyecto, o crea el archivo con `sdk.dir` apuntando a tu SDK.

## Contribución
- Haz fork, crea una rama (`feature/...`), realiza cambios y abre un Pull Request.
- Sigue el estilo del proyecto y agrega pruebas si aplica.

Si necesitas diagramas de flujo de pantallas, ejemplos de uso de `DatabaseHelper` o detalles de tablas, indícalo y los añadimos aquí.
---

> Desarrollado por Jesus Alexander Valeriano para el CBT02, Octubre 2025
