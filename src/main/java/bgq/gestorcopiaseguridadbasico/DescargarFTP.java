/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bgq.gestorcopiaseguridadbasico;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import javax.crypto.SecretKey;

/**
 *
 * @author Belén García Quesada
 */
public class DescargarFTP {
    static Scanner keyboard = new Scanner(System.in);
    private final MonitorFTP monitorFtp;

    // Constructor
    public DescargarFTP(MonitorFTP monitorFtp) {
        this.monitorFtp = monitorFtp;
    }

    // Descargar y descifrar fichero del servidor FTP
    public void descargarArchivoDescifrado(String rutaArchivoRemoto, String rutaArchivoLocal, SecretKey key) {
        try (OutputStream outputStream = new FileOutputStream(rutaArchivoLocal + ".enc")) {
            boolean exito = monitorFtp.getFtpClient().retrieveFile(rutaArchivoRemoto, outputStream);
            if (exito) {
                CifradoAES.descifrarArchivo(rutaArchivoLocal + ".enc", key);
                System.out.println("Archivo descargado y descifrado correctamente.");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Introduce el host FTP:");
        String host = keyboard.nextLine();
        System.out.println("Introduce el nombre de usuario FTP:");
        String username = keyboard.nextLine();
        System.out.println("Introduce la contraseña FTP:");
        String password = keyboard.nextLine();

        MonitorFTP monitorFtp = new MonitorFTP(host, username, password);
        DescargarFTP descargarFtp = new DescargarFTP(monitorFtp);

        System.out.println("Introduce la ruta del archivo remoto:");
        String rutaArchivoRemoto = keyboard.nextLine();
        System.out.println("Introduce la ruta del archivo local:");
        String rutaArchivoLocal = keyboard.nextLine();

        SecretKey key = CifradoAES.generarClave();
        descargarFtp.descargarArchivoDescifrado(rutaArchivoRemoto, rutaArchivoLocal, key);

        monitorFtp.desconectar();
    }
}