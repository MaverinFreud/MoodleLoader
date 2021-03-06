/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodleloader;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.CookieStore;

import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;

import org.apache.http.util.EntityUtils;

import org.apache.http.client.protocol.HttpClientContext;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class Connection {

    /**
     * Logs into moodle and gives the httpclient the recevied cookie
     *
     * @param httpclient handler for connection to moodle
     * @return handler with cookie for session
     * @throws Exception
     */
    public static String loginAndGetHTML(HttpClient httpclient, String username, String password, String downloadUrl, String moodleUrl) throws Exception {
        HttpPost httpPost;
        String html = "";
        httpPost = new HttpPost(Connection.getMoodleUrl(moodleUrl));
        httpPost.setEntity(new UrlEncodedFormEntity(Connection.getLoginData(username, password)));
        httpPost.addHeader("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        HttpClientContext context = HttpClientContext.create();
        HttpResponse response = httpclient.execute(httpPost, context);
        try {
            CookieStore cookieStore = context.getCookieStore();
            HttpClient httpclient2 = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            HttpPost httpPost2 = new HttpPost(downloadUrl);
            List<Cookie> cookies = cookieStore.getCookies();
            HttpResponse response2 = httpclient2.execute(httpPost2);
            HttpEntity entity2 = response2.getEntity();
            html = EntityUtils.toString(entity2);
            System.out.println("COOKIES:" + cookies.toString());
        } finally {

        }

        System.out.println(html);
        return html;
    }

    /**
     * Creates the login-url to the moodle-portal
     *
     * @param moodleUrl url to the moodle-portal
     * @return url to login into the moodle portal
     */
    public static String getMoodleUrl(String moodleUrl) {
        if (moodleUrl.contains("https")) {
            if (moodleUrl.endsWith("/")) {
                moodleUrl = moodleUrl.substring(0, moodleUrl.length() - 1);
            }
            return moodleUrl + "/login/index.php";
        } else {
            if (moodleUrl.startsWith("www.")) {
                moodleUrl = moodleUrl.substring(4, moodleUrl.length());
                return "https://" + moodleUrl + "/login/index.php";
            }
            return "https://" + moodleUrl + "/login/index.php";
        }
    }

    /**
     * Create form for transmitting username and password
     * @param username in moodle
     * @param password in moodle
     * @return Form with username and password
     */
    public static List<NameValuePair> getLoginData(String username, String password) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("password", password));
        return nvps;
    }
}
