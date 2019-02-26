package org.luncert.csdn2.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.luncert.csdn2.util.NormalUtil;
import org.luncert.csdn2.util.Result;
import org.luncert.csdn2.util.Result.Status;
import org.luncert.csdn2.util.filterGrammar.CFLexer;
import org.luncert.csdn2.util.filterGrammar.CFParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class FilterController
{
    
    // @GetMapping("/filter")
    public Result filter(@RequestParam String query)
    {
        try {
            CFLexer lexer = new CFLexer(CharStreams.fromString(query));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            CFParser parser = new CFParser(tokenStream);
            parser.filter();
            parseQueryFromTokens(tokenStream.getTokens());
            return new Result(Status.OK);
        } catch (Exception e) {
            return new Result(Status.ERROR, NormalUtil.throwableToString(e));
        }
    }

    private void parseQueryFromTokens(List<Token> tokens)
    {
        Token firstToken = tokens.remove(0);
        assert(firstToken.getType() == CFLexer.SYMBOL_NAME);
        String listName = firstToken.getText();

        Stack<Token> stack = new Stack<>();
        for (Token token : tokens)
        {
            
        }
    }
    
}