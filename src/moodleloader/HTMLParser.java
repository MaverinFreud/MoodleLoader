/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodleloader;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 *
 * @author Musiker
 */
public class HTMLParser {

    /**
     * Extracts the download-links and names of the download files and appends
     * it to the lists
     *
     * @param html html-content of the webiste
     * @param resourceList list for appending downloadlinks
     * @param nameList list for appending the name of the download-files
     */
    public static void parse(String html, List<String> resourceList, List<String> nameList) {

        Document htmlDoc = Jsoup.parse(html);
        Node startNode = htmlDoc.childNode(0);
        Elements elements = htmlDoc.getElementsByAttribute("onclick");

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).attr("href").contains("resource")) {
                nameList.add(HTMLParser.removeSignsFromFileNames(HTMLParser.getFileName(elements.get(i).children())));
                resourceList.add(elements.get(i).attr("href"));
            }
        }

    }

    /**
     * Removes characters which are not allowed in file-names
     *
     * @param name file-name
     * @return cleaned name
     */
    public static String removeSignsFromFileNames(String name) {
        name = name.replaceAll("[^a-zA-zäüöÄÜÖ1-9 ]", "");
        return name;
    }

    /**
     * Get the child node of the download link, which contains the
     * download-file-name
     *
     * @param elements children elements of the download-link element
     * @return file-name
     */
    public static String getFileName(Elements elements) {
        Element name = elements.get(1);
        return name.text();
    }

}
