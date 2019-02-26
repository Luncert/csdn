package org.luncert.csdn2.util.filterGrammar;

// Generated from CF.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CFLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, SYMBOL_NAME=9, 
		NUMBER=10, INTEGER=11, FLOAT=12, STRING=13, WS=14, NOT=15, AND=16, OR=17, 
		LBRACKET=18, RBRACKET=19;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "SYMBOL_NAME", 
			"NUMBER", "INTEGER", "FLOAT", "STRING", "WS", "NOT", "AND", "OR", "LBRACKET", 
			"RBRACKET"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", "'?='", "'='", "'!='", "'>'", "'<'", "'>='", "'<='", null, 
			null, null, null, null, null, "'!'", "'&&'", "'||'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "SYMBOL_NAME", 
			"NUMBER", "INTEGER", "FLOAT", "STRING", "WS", "NOT", "AND", "OR", "LBRACKET", 
			"RBRACKET"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CFLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\25\u0080\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\7\n@\n\n\f\n\16\nC\13\n\3"+
		"\13\3\13\5\13G\n\13\3\f\3\f\7\fK\n\f\f\f\16\fN\13\f\3\r\3\r\7\rR\n\r\f"+
		"\r\16\rU\13\r\3\r\3\r\6\rY\n\r\r\r\16\rZ\5\r]\n\r\3\16\3\16\7\16a\n\16"+
		"\f\16\16\16d\13\16\3\16\3\16\3\16\7\16i\n\16\f\16\16\16l\13\16\3\16\5"+
		"\16o\n\16\3\17\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\2\2\25\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25\3\2\t\5\2C\\aac|\6\2"+
		"\62;C\\aac|\3\2\63;\3\2\62;\4\2\f\f))\4\2\f\f$$\5\2\f\f\17\17\"\"\2\u0088"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\3)\3\2\2\2\5+\3\2\2\2\7.\3\2\2\2\t\60\3\2"+
		"\2\2\13\63\3\2\2\2\r\65\3\2\2\2\17\67\3\2\2\2\21:\3\2\2\2\23=\3\2\2\2"+
		"\25F\3\2\2\2\27H\3\2\2\2\31O\3\2\2\2\33n\3\2\2\2\35p\3\2\2\2\37t\3\2\2"+
		"\2!v\3\2\2\2#y\3\2\2\2%|\3\2\2\2\'~\3\2\2\2)*\7<\2\2*\4\3\2\2\2+,\7A\2"+
		"\2,-\7?\2\2-\6\3\2\2\2./\7?\2\2/\b\3\2\2\2\60\61\7#\2\2\61\62\7?\2\2\62"+
		"\n\3\2\2\2\63\64\7@\2\2\64\f\3\2\2\2\65\66\7>\2\2\66\16\3\2\2\2\678\7"+
		"@\2\289\7?\2\29\20\3\2\2\2:;\7>\2\2;<\7?\2\2<\22\3\2\2\2=A\t\2\2\2>@\t"+
		"\3\2\2?>\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\24\3\2\2\2CA\3\2\2\2D"+
		"G\5\27\f\2EG\5\31\r\2FD\3\2\2\2FE\3\2\2\2G\26\3\2\2\2HL\t\4\2\2IK\t\5"+
		"\2\2JI\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2M\30\3\2\2\2NL\3\2\2\2OS\t"+
		"\4\2\2PR\t\5\2\2QP\3\2\2\2RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\\\3\2\2\2US"+
		"\3\2\2\2VX\7\60\2\2WY\t\5\2\2XW\3\2\2\2YZ\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2"+
		"[]\3\2\2\2\\V\3\2\2\2\\]\3\2\2\2]\32\3\2\2\2^b\7)\2\2_a\n\6\2\2`_\3\2"+
		"\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3\2\2\2eo\7)\2\2fj\7$\2"+
		"\2gi\n\7\2\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2km\3\2\2\2lj\3\2\2"+
		"\2mo\7$\2\2n^\3\2\2\2nf\3\2\2\2o\34\3\2\2\2pq\t\b\2\2qr\3\2\2\2rs\b\17"+
		"\2\2s\36\3\2\2\2tu\7#\2\2u \3\2\2\2vw\7(\2\2wx\7(\2\2x\"\3\2\2\2yz\7~"+
		"\2\2z{\7~\2\2{$\3\2\2\2|}\7*\2\2}&\3\2\2\2~\177\7+\2\2\177(\3\2\2\2\f"+
		"\2AFLSZ\\bjn\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}