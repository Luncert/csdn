package org.luncert.csdn2.util;

import org.apache.http.HttpHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RequestTests {

    @Test
    public void test() throws Exception {
        Response rep = Request.url("http://www.baidu.com")
            .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36")
            .execute();
        assert rep.statusCode() == 200;
        System.out.println(rep.text());
    }

}