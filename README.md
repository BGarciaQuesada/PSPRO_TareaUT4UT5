# Sincronizador de Carpetas con Servidor FTP

## Descripción

Esta aplicación en Java permite sincronizar una carpeta local con una carpeta remota en un servidor FTP. La sincronización incluye la creación, modificación y eliminación de ficheros, y utiliza cifrado AES para proteger los ficheros almacenados en el servidor. La aplicación está diseñada para ejecutarse en segundo plano y utiliza programación multiproceso y multihilo para mejorar la eficiencia.

## Características Clave

- **Monitoreo de Carpeta Local**: Utiliza `WatchService` de Java NIO para detectar cambios en la carpeta local.
- **Sincronización con Servidor FTP**: Sube, modifica y elimina ficheros en el servidor FTP en respuesta a los cambios locales.
- **Cifrado AES**: Cifra los ficheros antes de subirlos al servidor FTP para mejorar la seguridad.
- **Programación Multiproceso y Multihilo**: Utiliza hilos especializados para manejar las operaciones de sincronización de manera eficiente.
- **Sincronización de Hilos**: Asegura que las operaciones de sincronización no bloqueen el monitoreo de la carpeta local.
- **Gestión de Comunicaciones y Servicios**: Maneja las conexiones FTP y las operaciones de cifrado/descifrado de manera robusta.

## Funcionamiento

### 1. Monitoreo de la Carpeta Local

La aplicación utiliza `WatchService` para monitorear una carpeta local específica. Cuando se detecta un cambio (creación, modificación o eliminación de un fichero), se crea un hilo especializado para manejar la operación correspondiente.

### 2. Sincronización con el Servidor FTP

- **Creación de Ficheros**: Cuando se crea un nuevo fichero en la carpeta local, se cifra utilizando AES y se sube al servidor FTP.
- **Modificación de Ficheros**: Cuando se modifica un fichero existente, se cifra y se sube al servidor FTP, reemplazando la versión anterior.
- **Eliminación de Ficheros**: Cuando se elimina un fichero, se elimina también del servidor FTP.

### 3. Programación Multiproceso y Multihilo

La aplicación utiliza un `ExecutorService` para manejar los hilos especializados. Esto permite que las operaciones de sincronización se realicen en segundo plano sin bloquear el monitoreo de la carpeta local.

- **FileCreateHandler**: Maneja la creación de ficheros.
- **FileModifyHandler**: Maneja la modificación de ficheros.
- **FileDeleteHandler**: Maneja la eliminación de ficheros.

### 4. Sincronización de Hilos

El uso de `ExecutorService` asegura que las operaciones de sincronización se realicen de manera concurrente y eficiente. Esto evita que el monitoreo de la carpeta local se bloquee mientras se realizan operaciones de sincronización.

### 5. Gestión de Comunicaciones y Servicios

La aplicación utiliza la biblioteca Apache Commons Net para manejar las conexiones FTP. Las operaciones de cifrado y descifrado se realizan utilizando la clase `Cipher` de Java.

## Estructura del Proyecto

- **Main.java**: Punto de entrada de la aplicación. Inicializa el monitoreo de la carpeta local.
- **FolderWatcher.java**: Monitorea la carpeta local y crea hilos especializados para manejar los eventos.
- **FileCreateHandler.java**: Maneja la creación de ficheros.
- **FileModifyHandler.java**: Maneja la modificación de ficheros.
- **FileDeleteHandler.java**: Maneja la eliminación de ficheros.
- **FTPManager.java**: Maneja las operaciones FTP.
- **AESUtil.java**: Maneja el cifrado y descifrado de ficheros.
- **FTPDownloader.java**: Permite descargar y descifrar ficheros desde el servidor FTP.

## Configuración

1. **Añadir Dependencias**: Asegúrate de añadir la dependencia de Apache Commons Net a tu proyecto. Si estás utilizando Maven, añade la siguiente dependencia a tu `pom.xml`:

    ```xml
    <dependency>
        <groupId>commons-net</groupId>
        <artifactId>commons-net</artifactId>
        <version>3.8.0</version>
    </dependency>
    ```

2. **Configurar el Servidor FTP**: Actualiza las credenciales del servidor FTP en el archivo `Main.java`.

3. **Ejecutar la Aplicación**: Compila y ejecuta la aplicación. La carpeta local especificada será monitoreada y sincronizada con el servidor FTP.
