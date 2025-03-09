/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bgq.gestorcopiaseguridadbasico;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Belén García Quesada
 */
public class MonitorFTP {
    private final FTPClient ClienteFtp;

    // Constructor que inicializa la conexión FTP
    public MonitorFTP(String host, String username, String password) {
        this.ClienteFtp = new FTPClient();
        try {
            ClienteFtp.connect(host);
            ClienteFtp.login(username, password);
            ClienteFtp.enterLocalPassiveMode();
            ClienteFtp.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // --- MÉTODOS SOBRE FICHEROS ---
    public void subirArchivo(String localFilePath, String remoteFilePath) {
        try (FileInputStream fis = new FileInputStream(new File(localFilePath))) {
            boolean done = ClienteFtp.storeFile(remoteFilePath, fis);
            if (done) {
                System.out.println("Archivo subido correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void borrarArchivo(String remoteFilePath) {
        try {
            boolean done = ClienteFtp.deleteFile(remoteFilePath);
            if (done) {
                System.out.println("Archivo borrado correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            if (ClienteFtp.isConnected()) {
                ClienteFtp.logout();
                ClienteFtp.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FTPClient getFtpClient() {
        return ClienteFtp;
    }
}