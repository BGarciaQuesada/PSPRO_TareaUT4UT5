/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bgq.gestorcopiaseguridadbasico;

import java.nio.file.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.SecretKey;

/**
 *
 * @author Belén García Quesada
 */
public class MonitorCarpetaLocal {

    // --- VARIABLES PARA PODER MONITOREAR (WATCH) LA CARPETA ---
    private final WatchService watchService;
    private final Path path;
    private final MonitorFTP monitorFtp;
    private final SecretKey key;
    private final ExecutorService executorService;

    // Constructor que inicializa servicio de monitoreo y ejecutor de hilos
    public MonitorCarpetaLocal(Path path, MonitorFTP monitorFtp, SecretKey key) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.path = path;
        this.monitorFtp = monitorFtp;
        this.key = key;
        this.executorService = Executors.newCachedThreadPool();
        this.path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
    }

    // Método principal que monitorea la carpeta y maneja eventos
    public void monitorear() {
        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path nombreArchivo = (Path) event.context();

                // Crear hilos especializados para manejar cada tipo de evento
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    executorService.submit(new CrearArchivo(path.resolve(nombreArchivo)));
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    executorService.submit(new ModificarArchivo(path.resolve(nombreArchivo)));
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    executorService.submit(new EliminarArchivo(nombreArchivo.toString()));
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    // --- MÉTODOS SOBRE FICHEROS ---
    private class CrearArchivo implements Runnable {

        private final Path rutaArchivo;

        public CrearArchivo(Path rutaArchivo) {
            this.rutaArchivo = rutaArchivo;
        }

        @Override
        public void run() {
            try {
                // Cifrar el fichero y subirlo al servidor FTP
                CifradoAES.cifrarArchivo(rutaArchivo.toString(), key);
                monitorFtp.subirArchivo(rutaArchivo.toString() + ".enc", rutaArchivo.getFileName().toString() + ".enc");
                // Eliminar el fichero cifrado local
                Files.delete(rutaArchivo.resolveSibling(rutaArchivo.getFileName() + ".enc"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ModificarArchivo implements Runnable {

        private final Path rutaArchivo;

        public ModificarArchivo(Path filePath) {
            this.rutaArchivo = filePath;
        }

        @Override
        public void run() {
            try {
                // Cifrar el fichero y subirlo al servidor FTP
                CifradoAES.cifrarArchivo(rutaArchivo.toString(), key);
                monitorFtp.subirArchivo(rutaArchivo.toString() + ".enc", rutaArchivo.getFileName().toString() + ".enc");
                // Eliminar el fichero cifrado local
                Files.delete(rutaArchivo.resolveSibling(rutaArchivo.getFileName() + ".enc"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class EliminarArchivo implements Runnable {

        private final String rutaArchivo;

        public EliminarArchivo(String fileName) {
            this.rutaArchivo = fileName;
        }

        @Override
        public void run() {
            try {
                // Eliminar el fichero del servidor FTP
                monitorFtp.borrarArchivo(rutaArchivo + ".enc");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
