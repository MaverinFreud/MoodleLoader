/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodleloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author Musiker
 */
public class Downloader {

    /**
     * downloads pdf files into an folder with correct file-name
     * maybe play with the new byte size to get more perfomant result
     * @param httpClient client with cookie to enter the course
     * @param urlList list of download urls
     * @param folderName name of folder
     * @param nameList list of the names of the download-files
     */
    public static void download(HttpClient httpClient, List<String> urlList, List<String> nameList, String folderName) {

        for (int i = 0; i < urlList.size(); i++) {
            try {
                HttpGet request = new HttpGet(urlList.get(i) + "&redirect=1");
                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                File dir = new File(Downloader.removeSignsFromFolderNames(folderName));
                dir.mkdir();
                String filePath = folderName + "/" + nameList.get(i) + ".pdf";
                BufferedInputStream in = new BufferedInputStream(entity.getContent());
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                byte[] buff = new byte[4096];
                int len = 0;
                while ((len = in.read(buff)) > 0) //If necessary readLine()
                {
                    out.write(buff, 0, len);
                }
                in.close();
                out.close();

            } catch (Exception ex) {

            }
        }

    }
    
    /**
     * Removes characters which are not allowed in folder-names
     * @param folderName
     * @return cleaned name
     */
    public static String removeSignsFromFolderNames(String folderName){
    return HTMLParser.removeSignsFromFileNames(folderName);
}

}
