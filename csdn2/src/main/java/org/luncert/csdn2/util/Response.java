package org.luncert.csdn2.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

public class Response {

    private int statusCode;
    private String reasonPhrase;
    private Map<String, String> headers = new HashMap<>();
    private byte[] content;

    Response(CloseableHttpResponse rep) throws UnsupportedOperationException, IOException {
        StatusLine statusLine = rep.getStatusLine();
        statusCode = statusLine.getStatusCode();
        reasonPhrase = statusLine.getReasonPhrase();

        for (Header header : rep.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }

        HttpEntity entity = rep.getEntity();
        content = IO.read(entity.getContent());
    }

    public int statusCode() {
        return statusCode;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }

    public byte[] content() {
        return content;
    }

    public String text() {
        return new String(content);
    }

    public String header(String headerName) {
        return headers.get(headerName);
    }

    public Map<String, String> headers() {
        return headers;
    }

}