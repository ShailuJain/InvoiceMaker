/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import ui.MainFrame;
import static update.SoftwareDetails.DIST_DOWNLOAD_URL;

/**
 *
 * @author Shailesh
 */
public class UpdateSoftware {
    private static boolean isUpdateAvailable = false;
    private static URL url;
    private static HttpURLConnection httpConnection;
    private static volatile long progress;
    private static Thread downloadThread;
    static{
        try {
            url = new URL(DIST_DOWNLOAD_URL);
            httpConnection = (HttpURLConnection) (url.openConnection());
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public static long getCompleteFileSize(){
        return httpConnection.getContentLengthLong();
    }
    public static boolean isUpdateAvailable() throws IOException{
        String remoteProductVersion = getRemoteProductVersion();
        System.out.println("version: " + remoteProductVersion);
        if(!SoftwareDetails.PRODUCT_VERSION.equals(remoteProductVersion)){
            isUpdateAvailable = true;
        }
        System.out.println(isUpdateAvailable);
        return isUpdateAvailable;
    }

    private static String getRemoteProductVersion() throws IOException {
        URL productVersion = new URL(SoftwareDetails.SOFTWARE_DETAILS_URL);
        Scanner scanner = new Scanner(productVersion.openStream());
        String str = "",ver = SoftwareDetails.PRODUCT_VERSION;
        while(scanner.hasNext()){
            str+=(scanner.nextLine());
        }
        System.out.println(str);
        Pattern pat = Pattern.compile("PRODUCT_VERSION.*?\"(.*?)\";");
        Matcher mat = pat.matcher(str);
        if(mat.find()){
            ver = (mat.group(1));
        }
        return ver;
    }
    public static void downloadAndInstallUpdate(){
        if(isUpdateAvailable){
            downloadFile(SoftwareDetails.DIST_DOWNLOAD_URL,System.getProperty("user.home") + "\\Documents");
            System.out.println(MainFrame.class.getProtectionDomain().getCodeSource().getLocation());
        }
    }
    public static void downloadFile(String DIST_DOWNLOAD_URL, String location) {
        downloadThread = new Thread(()->{
           try {
                byte[] b = new byte[1024];
                int bytesRead = 0;
                long downloadedFileSize = 0l;
                long totalFileSize = getCompleteFileSize();
                /**
                 * Downloading file from the remote location
                 */

                BufferedInputStream br = new BufferedInputStream(httpConnection.getInputStream());

                /**
                 * Saving the file locally
                 */
                String fileName = url.getFile().substring(url.getFile().lastIndexOf("/"));
                File file = new File(location,fileName);
                FileOutputStream pw = new FileOutputStream(file);

                while((bytesRead = br.read(b, 0, b.length))!=-1){
                    progress+=bytesRead;
                    pw.write(b,0,bytesRead);
                }
                br.close();
                pw.close();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Download has stopped!");
            } 
        });
        downloadThread.start();
    }
    
    public static long getProgress(){
        return progress;
    }
    public static void stopUpdate(){
        downloadThread.interrupt();
    }
}
