/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Shailesh
 */
public class UpdateSoftware {
    public static boolean checkForUpdate() throws IOException{
        boolean result = false;
        String remoteProductVersion = getRemoteProductVersion();
//        if(SoftwareDetails.product_version)
        return result;
    }

    private static String getRemoteProductVersion() throws IOException {
        URL remoteFile = new URL(SoftwareDetails.SOFTWARE_DETAILS_URL);
        Scanner scanner = new Scanner(remoteFile.openStream());
        Pattern pat = Pattern.compile("");
        String str = scanner.findInLine("\\d+\\.\\d+");
        System.out.println(str);
        return "";
    }
}
