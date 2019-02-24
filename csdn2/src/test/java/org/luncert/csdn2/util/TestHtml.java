package org.luncert.csdn2.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.csdn2.util.Html.FindFilter;
import org.luncert.csdn2.util.Html.FindFilterChain;
import org.luncert.csdn2.util.Html.HTMLElem;

@RunWith(JUnit4.class)
public class TestHtml
{

    HTMLElem doc;

    @Before
    public void before() throws FileNotFoundException, IOException
    {
        String path = "/home/luncert/Project/csdn2/src/test/java/org/luncert/csdn2/util/abc.com.html";
        byte[] source = IO.read(new FileInputStream(path));
        doc = Html.parse(new String(source));
    }

    @Test
    public void testFindFilter()
    {
        HTMLElem target = doc.find(new FindFilter()
            .tagName("div")    
            .clazz("adStub"));
        assert target != null;
    }

    @Test
    public void testFindFilterChain()
    {
        HTMLElem target = doc.find(FindFilterChain
            .start().id("wrapper")
            .next().id("navAuth")
            .next().clazz("navProvider"));
        assert target != null;
        System.out.println(target);
    }

    // @Test
    // public void testFindFilter()
    // {
    //     HTMLElem elem = new HTMLElem();
    //     elem.setTagName("div");
    //     elem.getAttrs().put("href", "../");
    //     elem.setClasses(new String[]{ "listing" });
    //     HTMLElem target = elem.find(new FindFilter()
    //         .tagName("div")
    //         .classes("listing")
    //         .attr("href", "../"));
    //     assert target != null;
    // }

}