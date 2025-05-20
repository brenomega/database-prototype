package compiler;

public class Tokenizer {

	public enum TokenType {
		TK_SEMI,TK_LP, TK_RP, TK_COMMA, TK_ID, TK_NE, TK_EQ, TK_GT, TK_LE, TK_LT, 
		TK_GE, TK_BITAND, TK_BITOR, TK_LSHIFT, TK_RSHIFT, TK_PLUS, TK_MINUS, 
		TK_STAR, TK_SLASH, TK_REM, TK_CONCAT, TK_BITNOT, TK_STRING, TK_NULL, 
		TK_DOT, TK_INTEGER, TK_FLOAT, TK_BLOB, TK_VARIABLE, TK_ILLEGAL, TK_SPACE
	}
	
	class Token {
		private final TokenType type;
		private final String value;
		
		public Token(TokenType type, String value) {
			this.type = type;
			this.value = value;
		}
		
		public TokenType getType() {
			return type;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	// Character class constants ASCII
	private static final int CC_X = 0;
	private static final int CC_KYWD0 = 1;
	private static final int CC_KYWD = 2;
	private static final int CC_DIGIT = 3;
	private static final int CC_DOLLAR = 4;
	private static final int CC_VARALPHA = 5;
	private static final int CC_VARNUM = 6;
	private static final int CC_SPACE = 7;
	private static final int CC_QUOTE = 8;
	private static final int CC_QUOTE2 = 9;
	private static final int CC_PIPE = 10;
	private static final int CC_MINUS = 11;
	private static final int CC_LT = 12;
	private static final int CC_GT = 13;
	private static final int CC_EQ = 14;
	private static final int CC_BANG = 15;
	private static final int CC_SLASH = 16;
	private static final int CC_LP = 17;
	private static final int CC_RP = 18;
	private static final int CC_SEMI = 19;
	private static final int CC_PLUS = 20;
	private static final int CC_STAR = 21;
	private static final int CC_PERCENT = 22;
	private static final int CC_COMMA = 23;
	private static final int CC_AND = 24;
	private static final int CC_TILDA = 25;
	private static final int CC_DOT = 26;
	private static final int CC_ID = 27;
	private static final int CC_ILLEGAL = 28;
	private static final int CC_NUL = 29;
	private static final int CC_BOM = 30;
		
	private static final int[] charClass = {
			29, 28, 28, 28, 28, 28, 28, 28, 28,  7,  7, 28,  7,  7, 28, 28,
			28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28,
			7, 15,  8,  5,  4, 22, 24,  8, 17, 18, 21, 20, 23, 11, 26, 16,
			3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  5, 19, 12, 14, 13,  6,
			5,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
			1,  1,  1,  1,  1,  1,  1,  1,  0,  2,  2,  9, 28, 28, 28,  2,
			8,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
			1,  1,  1,  1,  1,  1,  1,  1,  0,  2,  2, 28, 10, 28, 25, 28,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 30,
			27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27
	};
		
	private String input;
	private int offset = 0;
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public Token nextToken() { 
		if (offset >= input.length()) return null;
		
		int start = offset;
		char c = input.charAt(start);
		int cls = charClass[c];
		
		switch (cls) {
		case CC_SPACE:
			while (offset < input.length() && charClass[input.charAt(offset)] == CC_SPACE) offset++;
			return new Token(TokenType.TK_SPACE, input.substring(start, offset));
		case CC_MINUS:
			if (offset + 1 < input.length() && charClass[input.charAt(offset+1)] == CC_MINUS) {
				offset += 2;
				while (offset < input.length() && input.charAt(offset) != '\n') offset++;
				offset++;
				return new Token(TokenType.TK_SPACE, input.substring(start, offset));
			}
			offset++;
			return new Token(TokenType.TK_MINUS, "-");
		case CC_LP:
			offset++;
			return new Token(TokenType.TK_LP, "(");
		case CC_RP:
			offset++;
			return new Token(TokenType.TK_RP, ")");
		case CC_SEMI:
			offset++;
			return new Token(TokenType.TK_SEMI, ";");
		case CC_PLUS:
			offset++;
			return new Token(TokenType.TK_PLUS, "+");
		case CC_STAR:
			offset++;
			return new Token(TokenType.TK_STAR, "*");
		case CC_SLASH:
			if (offset + 1 < input.length() && charClass[input.charAt(offset+1)] == CC_STAR) {
				offset += 2;
				while (offset < input.length()) {
					if (offset + 1 < input.length() 
							&& charClass[input.charAt(offset)] == CC_STAR
							&& charClass[input.charAt(offset+1)] == CC_SLASH) {
						offset += 2;
						return new Token(TokenType.TK_SPACE, input.substring(start, offset));
					}
					offset++;
				}
				return new Token(TokenType.TK_SPACE, input.substring(start, offset));
			}
			offset++;
			return new Token(TokenType.TK_SLASH, "/");
		case CC_PERCENT:
			offset++;
			return new Token(TokenType.TK_REM, "%");
		case CC_EQ:
			if (offset + 1 < input.length() && charClass[input.charAt(offset+1)] == CC_EQ) offset++;
			offset++;
			return new Token(TokenType.TK_EQ, input.substring(start, offset));
		case CC_LT:
			if (offset + 1 < input.length()) {
				int nextCharClass = charClass[input.charAt(offset+1)];
				
				if (nextCharClass == CC_EQ) {
					offset += 2;
					return new Token(TokenType.TK_LE, input.substring(start, offset));
				} else if (nextCharClass == CC_GT) {
					offset += 2;
					return new Token(TokenType.TK_NE, input.substring(start, offset));
				} else if (nextCharClass == CC_LT) {
					offset += 2;
					return new Token(TokenType.TK_LSHIFT, input.substring(start, offset));
				}
			}
			offset++;
			return new Token(TokenType.TK_LT, "<");
		case CC_GT:
			if (offset + 1 < input.length()) {
				int nextCharClass = charClass[input.charAt(offset+1)];
				
				if (nextCharClass == CC_EQ) {
					offset += 2;
					return new Token(TokenType.TK_GE, input.substring(start, offset));
				} else if (nextCharClass == CC_GT) {
					offset += 2;
					return new Token(TokenType.TK_RSHIFT, input.substring(start, offset));
				}
			}
			offset++;
			return new Token(TokenType.TK_GT, ">");
		case CC_BANG:
			if (offset + 1 < input.length() && charClass[input.charAt(offset+1)] == CC_EQ) {
				offset += 2;
				return new Token(TokenType.TK_NE, input.substring(start, offset));
			} else {
				offset++;
				return new Token(TokenType.TK_ILLEGAL, input.substring(start, offset));
			}
		case CC_COMMA:
			offset++;
			return new Token(TokenType.TK_COMMA, ",");
		case CC_AND:
			offset++;
			return new Token(TokenType.TK_BITAND, "&");
		case CC_TILDA:
			offset++;
			return new Token(TokenType.TK_BITNOT, "~");
		case CC_PIPE:
			if (offset + 1 < input.length() && charClass[input.charAt(offset+1)] == CC_PIPE) {
				offset += 2;
				return new Token(TokenType.TK_CONCAT, input.substring(start, offset));
			} else {
				offset++;
				return new Token(TokenType.TK_BITOR, "|");
			}
		case CC_DOT:
			if (offset + 1 >= input.length() || charClass[input.charAt(offset+1)] != CC_DIGIT) {
				offset++;
				return new Token(TokenType.TK_DOT, ".");
			}
			offset++;
			while (offset < input.length() && charClass[input.charAt(offset)] == CC_DIGIT) offset++;
			return new Token(TokenType.TK_FLOAT, input.substring(start, offset));
		default:
			offset++;
			return new Token(TokenType.TK_ILLEGAL, input.substring(start, offset));
		}
	}
}
