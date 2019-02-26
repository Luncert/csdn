// Generated from CF.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CFParser}.
 */
public interface CFListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CFParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(CFParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(CFParser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CFParser#listName}.
	 * @param ctx the parse tree
	 */
	void enterListName(CFParser.ListNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#listName}.
	 * @param ctx the parse tree
	 */
	void exitListName(CFParser.ListNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CFParser#conditions}.
	 * @param ctx the parse tree
	 */
	void enterConditions(CFParser.ConditionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#conditions}.
	 * @param ctx the parse tree
	 */
	void exitConditions(CFParser.ConditionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CFParser#complexCondition}.
	 * @param ctx the parse tree
	 */
	void enterComplexCondition(CFParser.ComplexConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#complexCondition}.
	 * @param ctx the parse tree
	 */
	void exitComplexCondition(CFParser.ComplexConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CFParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(CFParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(CFParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CFParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(CFParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(CFParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link CFParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(CFParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CFParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(CFParser.FieldNameContext ctx);
}