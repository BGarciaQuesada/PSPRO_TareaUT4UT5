/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bgq.gestorcopiaseguridadbasico;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.crypto.SecretKey;

/**
 *
 * @author Belén García Quesada
 */
public class GestorCopiaSeguridad {

    public static void main(String[] args) throws Exception {
        // --- CONFIGURACIÓN SERVIDOR FTP ---
        // Rellenar con los datos correspondientes v
        String ftpHost = "";
        String ftpUsername = "";
        String ftpPassword = "";

        // Creo mi monitorFTP y clave para usarlas a continuación
        MonitorFTP monitorFtp = new MonitorFTP(ftpHost, ftpUsername, ftpPassword);
        SecretKey encryptionKey = CifradoAES.generarClave();

        // Poner path al archivo local v
        Path pathCarpetaLocal = Paths.get("");
        MonitorCarpetaLocal carpetaLocal = new MonitorCarpetaLocal(pathCarpetaLocal, monitorFtp, encryptionKey);

        carpetaLocal.monitorear();
    }
}
