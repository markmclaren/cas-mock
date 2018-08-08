package org.eurekaclinical.casmock.servlet;

/*-
 * #%L
 * CAS Mock
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.arp.javautil.io.IOUtil;

/**
 *
 * @author Andrew Post
 */
public abstract class AbstractValidateServlet extends HttpServlet {

    private String responsePath;
    private String responseWithPGTIOUPath;
    private String response;
    private String responseWithPGTIOU;
 
    // Create a trust manager that does not validate certificate chains
    private TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] certs, String authType) {
            }
        }
    };

    protected AbstractValidateServlet(String inResponsePath, String inResponseWithPGTIOUPath) {
        this.responsePath = inResponsePath;
        this.responseWithPGTIOUPath = inResponseWithPGTIOUPath;
    }

    @Override
    public void init() throws ServletException {
        try {
            this.response = IOUtil.readResourceAsString(getClass(), this.responsePath);
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException ex) {
        } catch (NoSuchAlgorithmException ex) {
        }

        try {
            this.responseWithPGTIOU = IOUtil.readResourceAsString(getClass(), this.responseWithPGTIOUPath);
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pgtUrlStr = req.getParameter("pgtUrl");
        if (pgtUrlStr == null) {
            resp.getWriter().write(this.response);
        } else {
            resp.getWriter().write(this.responseWithPGTIOU);
            URL pgtUrl = new URL(pgtUrlStr);
            doConnect(pgtUrl);

            URL pgtUrlWithParams = new URL(pgtUrlStr + "?pgtIou=PGTIOU-xxxxxxxxxxxxxxxx&pgtId=TGT-xxxxxxxxxxxxxxxx");
            doConnect(pgtUrlWithParams);
        }
    }

    void doConnect(URL url) throws IOException {
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()))) {
            while (in.readLine() != null) {
            }
        }
        int code = con.getResponseCode();
        if (code != 200) {
            throw new IOException("Response from proxy callback URL was " + code);
        }
    }
}
