/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Shailesh
 */
public class UpdateSoftware {
    public static boolean isUpdateAvailable = false;
    public static boolean isUpdateAvailable() throws IOException{
        String remoteProductVersion = getRemoteProductVersion();
        if(!SoftwareDetails.PRODUCT_VERSION.equals(remoteProductVersion)){
            isUpdateAvailable = true;
        }
        return isUpdateAvailable;
    }

    private static String getRemoteProductVersion() throws IOException {
        URL remoteFile = new URL(SoftwareDetails.SOFTWARE_DETAILS_URL);
        Scanner scanner = new Scanner(remoteFile.openStream());
        String str = "",ver = SoftwareDetails.PRODUCT_VERSION;
        while(scanner.hasNext()){
            str+=(scanner.nextLine());
        }
        Pattern pat = Pattern.compile("PRODUCT_VERSION.*?\"(.*?)\";");
        Matcher mat = pat.matcher(str);
        if(mat.find()){
            ver = (mat.group(1));
        }
        return ver;
    }
    public static void install(){
        try {
            Class<?> installUpdateClass = UpdateSoftware.class.getClassLoader().loadClass("update.InstallUpdate");
            installUpdateClass.getMethod("downloadAndInstallUpdate").invoke(null);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println(ex);
        }
    }
}
