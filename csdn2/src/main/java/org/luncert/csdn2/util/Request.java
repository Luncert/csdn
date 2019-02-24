package org.luncert.csdn2.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

public class Request {

    public enum Method {
        GET, HEAD, DELETE, POST, PUT
    }

    private String url;
    private Method method = Method.GET;
    private List<Header> headers = new ArrayList<>();
    private HttpEntity entity;

    private Request(String url) {
        this.url = url;
    }

    public static Request url(String url)
    {
        return new Request(url);
    }

    public Request method(Method method)
    {
        this.method = method;
        return this;
    }

    public Request header(String name, String value)
    {
        headers.add(new BasicHeader(name, value));
        return this;
    }

    public Request data(List<NameValuePair> nvps) throws UnsupportedEncodingException
    {
        entity = new UrlEncodedFormEntity(nvps);
        return this;
    }

    /**
     * add entity as json data
     */
    public Request data(Object data)
    {
        entity = new JSONEntity(data);
        return this;
    }

    public Request data(String data) throws UnsupportedEncodingException
    {
        entity = new StringEntity(data);
        return this;
    }

    public Response execute() throws Exception {
        HttpRequestBase req;
        if (method == Method.GET) req = new HttpGet();
        else if (method == Method.HEAD) req = new HttpHead(url);
        else if (method == Method.DELETE) req = new HttpDelete();
        else if (method == Method.POST) req = new HttpPost();
        else if (method == Method.PUT) req = new HttpPut();
        else throw new Exception("unrecognized method: " + method);
        req.setURI(new URI(url));

        CloseableHttpClient httpclient = HttpClients.createDefault();

        if (entity != null) {
            if (method == Method.POST || method == Method.PUT) {
                ((HttpEntityEnclosingRequestBase) req).setEntity(entity);
            }
            else throw new Exception("only POST or PUT method could set request body");
        }

        try (CloseableHttpResponse rep = httpclient.execute(req)) {
            assert rep.getStatusLine().getStatusCode() == 200;
            return new Response(rep);
        }
    }

    private static class JSONEntity extends StringEntity {
        
        public JSONEntity(final Object payload) throws UnsupportedCharsetException
        {
            super(JSON.toJSONString(payload), ContentType.APPLICATION_JSON);
        }

    }

}


/*
public Article getArticle(String url) throws Exception
{
    Response rep = Request.url(url)
        .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")
        .execute();
    assert rep.statusCode() == 200;

    HTMLElem doc = Html.parse(rep.text());

    String title = doc.find(FindFilterChain
        .start().clazz("article-title-box")
        .next().clazz("title-article")).content();

    HTMLElem info = doc.find(FindFilterChain
        .start().clazz("article-info-box")
        .next().clazz("article-bar-top"));
    
    String createTime = info.find(
        new FindFilter().clazz("time")).content();
    
    String author = info.find(
        new FindFilter().clazz("follow-nickName")).content();
    
    String readCount = info.find(
        new FindFilter().clazz("read-count")).content();
    
    String[] tags = null;
    HTMLElem tagsBox = info.find(new FindFilter().clazz("tags-box"));
    if (tagsBox != null)
    {
        tags = tagsBox.content().split(" ");
    }

    String copyright = null;
    HTMLElem copyrightElem = info.find(new FindFilter().clazz("article-copyright"));
    if (copyrightElem != null)
    {
        copyright = copyrightElem.content();
    }

    String content = info.find(new FindFilter().id("content_views")).content();
    
    return Article.builder()
        .title(title)
        .createTime(createTime)
        .author(author)
        .readCount(Integer.valueOf(readCount))
        .tags(tags)
        .copyright(copyright)
        .content(content)
        .build();
}
*/