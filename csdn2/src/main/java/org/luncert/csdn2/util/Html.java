package org.luncert.csdn2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.luncert.csdn2.util.antlr4.HTMLLexer;
import org.luncert.csdn2.util.antlr4.HTMLParser;
import org.luncert.csdn2.util.antlr4.HTMLParserBaseListener;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlAttributeContext;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlAttributeNameContext;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlAttributeValueContext;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlChardataContext;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlDocumentContext;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlElementContext;
import org.luncert.csdn2.util.antlr4.HTMLParser.HtmlTagNameContext;

import lombok.Getter;

/**
 * 不解析script、scriptlet、dtd、cdata、style
 */
public class Html {

    public static class FindFilter {

        private String tagName;
        private String id;
        private List<String> classes = new LinkedList<>();
        private Map<String, String> attrs = new LinkedHashMap<>();

        public FindFilter tagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public FindFilter id(String id) {
            this.id = id;
            return this;
        }

        public FindFilter clazz(String clazz) {
            Objects.requireNonNull(clazz);
            classes.add(clazz);
            return this;
        }

        public FindFilter attr(String name, String value) {
            attrs.put(name, value);
            return this;
        }

        boolean match(HTMLElem elem) {
            if (Objects.nonNull(tagName) && !tagName.equals(elem.getTagName())) {
                return false;
            }
            if (Objects.nonNull(id) && !id.equals(elem.getId())) {
                return false;
            }
            if (!classes.isEmpty()) {
                String[] tmp = elem.getClasses();
                if (tmp == null || tmp.length == 0)
                    return false;

                Set<String> set = new HashSet<>();
                for (String clazz : tmp)
                    set.add(clazz);
                for (String clazz : classes) {
                    if (!set.contains(clazz))
                        return false;
                }
            }
            if (attrs.size() > 0) {
                for (Map.Entry<String, String> attr : attrs.entrySet()) {
                    String tmp = elem.getAttr(attr.getKey());
                    if (tmp == null || !attr.getValue().equals(tmp))
                        return false;
                }
            }
            return true;
        }

    }

    public static class FindFilterChain implements Iterable<FindFilter> {
        private LinkedList<FindFilter> filters;

        private FindFilterChain() {
            filters = new LinkedList<>();
        }

        public static FindFilterChain start() {
            return new FindFilterChain().next();
        }

        public FindFilterChain next() {
            filters.add(new FindFilter());
            return this;
        }

        private FindFilter currentFilter() {
            return filters.getLast();
        }

        public FindFilterChain tagName(String tagName) {
            currentFilter().tagName(tagName);
            return this;
        }

        public FindFilterChain id(String id) {
            currentFilter().id(id);
            return this;
        }

        public FindFilterChain clazz(String clazz) {
            currentFilter().clazz(clazz);
            return this;
        }

        public FindFilterChain attr(String name, String value) {
            currentFilter().attr(name, value);
            return this;
        }

        @Override
        public Iterator<FindFilter> iterator() {
            return filters.iterator();
        }

    }

    @Getter
    public static class HTMLElem
    {
        private static final String INDENT = "  ";
        private String tagName;
        private String id;
        private String[] classes;
        private Map<String, String> attrs;
        private List<HTMLElem> children;

        private HTMLElem() {
            attrs = new HashMap<>();
            children = new ArrayList<>();
        }

        public String getAttr(String name)
        {
            return attrs.get(name);
        }

        public String content()
        {
            StringBuilder builder = new StringBuilder();
            for (HTMLElem child : children)
            {
                builder.append(child);
            }
            return builder.toString();
        }

        public HTMLElem find(FindFilter filter)
        {
            if (filter.match(this))
                return this;
            HTMLElem tmp;
            for (HTMLElem child : children) {
                tmp = child.find(filter);
                if (tmp != null)
                    return tmp;
            }
            return null;
        }

        /**
         * @return List<HTMLElem> always not null
         */
        public List<HTMLElem> findAll(FindFilter filter)
        {
            List<HTMLElem> results = new LinkedList<>();
            if (filter.match(this))
                results.add(this);
            for (HTMLElem child : children) {
                results.addAll(child.findAll(filter));
            }
            return results;
        }

        /**
         * 遍历FindFilterChain，每次根据一个FindFilter匹配到一个HTMLElem，
         * 就以该HTMLElem为基础，去匹配下一个FindFilter
         */
        public HTMLElem find(FindFilterChain chain)
        {
            HTMLElem base = this, tmp;
            for (FindFilter filter : chain)
            {
                tmp = base.find(filter);
                if (tmp == null)
                    return null;
                else
                    base = tmp;
            }
            return base;
        }

        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            parseElemHead(builder);
            for (HTMLElem child : children)
            {
                if (child instanceof HTMLTextElem)
                    builder.append(((HTMLTextElem) child).content);
                else
                    builder.append(child);
            }
            parseElemTail(builder);
            return builder.toString();
        }

        public String prettify(int indent)
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < indent; i++)
                builder.append(INDENT);
            parseElemHead(builder);
            if (!children.isEmpty())
            {
                if (children.size() == 1
                    && children.get(0) instanceof HTMLTextElem)
                {
                    HTMLTextElem textElem = (HTMLTextElem) children.get(0);
                    builder.append(textElem.content);
                }
                else {
                    builder.append('\n');
                    indent++;
                    for (HTMLElem child : children)
                        builder.append(child.prettify(indent)).append('\n');
                    indent--;
                    for (int i = 0; i < indent; i++)
                        builder.append(INDENT);
                }
            }
            parseElemTail(builder);
            return builder.toString();
        }

        public String prettify()
        {
            return prettify(0);
        }

        private void parseElemHead(StringBuilder builder)
        {
            builder.append('<').append(tagName);
            if (id != null)
                builder.append(" id=\"").append(id).append('"');
            if (classes != null && classes.length > 0)
            {
                builder.append(" class=\"");
                for (String clazz : classes)
                    builder.append(clazz).append(' ');
                int t = builder.length();
                // 将最后一个空格替换为引号
                builder.replace(t - 1, t, "\"");
            }
            if (!attrs.isEmpty())
            {
                for (Map.Entry<String, String> attr : attrs.entrySet())
                {
                    builder.append(' ').append(attr.getKey()).append("=\"")
                        .append(attr.getValue()).append('"');
                }
            }
            builder.append('>');
        }

        private void parseElemTail(StringBuilder builder)
        {
            builder.append("</").append(tagName).append('>');
        }

    }

    @Getter
    public static class HTMLTextElem extends HTMLElem
    {
        private String content;
    }

    private static class HTMLListener extends HTMLParserBaseListener
    {

        private static class Attribute
        {
            String name, value;
        }

        private Stack<HTMLElem> stack;
        private HTMLElem rootElem;
        private Attribute attr;

        private HTMLElem element() {
            return stack.lastElement();
        }

        public void enterHtmlDocument(HtmlDocumentContext ctx)
        {
            stack = new Stack<>();
        }

        public void exitHtmlDocument(HtmlDocumentContext ctx)
        {
            assert stack.size() == 0;
            assert rootElem.tagName.equals("html");
        }

        public void enterHtmlElement(HtmlElementContext ctx) {
            stack.push(new HTMLElem());
        }
        
        public void exitHtmlElement(HtmlElementContext ctx) {
            HTMLElem elem = stack.pop();
            // 过滤script元素
            if (elem.tagName != null) {
                if (!stack.empty())
                    element().children.add(elem);
                else
                    rootElem = elem;
            }
        }
        
        public void enterHtmlAttribute(HtmlAttributeContext ctx) {
            attr = new Attribute();
        }

        public void exitHtmlAttribute(HtmlAttributeContext ctx) {
            if (attr.name.equals("id")) {
                element().id = attr.value;
            }
            else if (attr.name.equals("class")) {
                element().classes = attr.value.split(" ");
            }
            else {
                element().attrs.put(attr.name, attr.value);
            }
            attr = null;
        }

        
        public void exitHtmlAttributeName(HtmlAttributeNameContext ctx) {
            attr.name = ctx.getText();
        }

        
        public void exitHtmlAttributeValue(HtmlAttributeValueContext ctx) {
            attr.value = ctx.getText()
                .replaceAll("^[\"']", "")
                .replaceAll("[\"']$", "");
        }

        public void exitHtmlTagName(HtmlTagNameContext ctx) {
            element().tagName = ctx.getText();
        }
        
        public void exitHtmlChardata(HtmlChardataContext ctx) {
            String text = ctx.getText().trim();
            if (text.length() > 0) {
                HTMLTextElem elem = new HTMLTextElem();
                elem.content = text;
                if (!stack.empty()) {
                    element().children.add(elem);
                }
                // 不需要再入栈，因为文本节点不会有子元素
            }
        }

    }

    public static HTMLElem parse(String source)
    {
        HTMLLexer lexer = new HTMLLexer(CharStreams.fromString(source));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        HTMLParser parser = new HTMLParser(tokenStream);
        HTMLListener listener = new HTMLListener();
        parser.addParseListener(listener);
        parser.htmlDocument();
        return listener.rootElem;
    }

}