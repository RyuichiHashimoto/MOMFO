package lib.experiments;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Automaton which parses the following LL(1) grammar.
 *
* <formula> ::= <switch>
* <switch> ::= switch(<formula>)\{{<formula>:<formula>}+\}| <ifThen>
* <ifThen> ::= <logic> {? <formula> : <formula>}
* <logic> ::= <comparison>{(&|\|)<comparison>}*
* <comparison> ::= <sum> {(==|!=|<|>|<=|>=) <sum>}*
* <sum> ::= <product> {(+|-) <product>}*
* <product> ::= <unary> {(*|/|%) <unary>}*
* <function> ::= round<function>|defined<key>|equals(<string>, <string>)|
*               -<function>|(<formula>)|<literal>
* <literal> ::= <boolean>|<number>
*/
class SettingFormula {
	public static final double SF_TRUE = btod(true);
	public static final double SF_FALSE = btod(false);

	protected CommandSetting setting;
	protected Tokenizer tokenizer_;

	public SettingFormula() {
		this(null);
	}

	public SettingFormula(CommandSetting s) {
		setting = s;
	}

	public double parseFormula(String eq) {
		tokenizer_ = new Tokenizer(eq);
		double value = formula();
		if (tokenizer_.hasNext()) error(tokenizer_.next() +" is not necessary. ");
		return value;
	}

	// <formula> ::= <switch>
	protected double formula() {
		return switching();
	}

	// <switch> ::= switch(<formula>)\{<formula>:<formula>}+\}|<ifThen>
	protected double switching() {
		String next = tokenizer_.next();
		if (! "switch".equals(next)) {
			tokenizer_.unread(next);
			return ifThen();
		}

		expectToken("(");
		double pivot = formula();
		expectToken(")");
		expectToken("{");

		double retval = Double.NaN;
		while (tokenizer_.hasNext()) {
			double key = formula();
			expectToken(":");
			double value = formula();
			if (pivot == key) retval = value;
			next = tokenizer_.next();
			if (!",".equals(next)) {
				tokenizer_.unread(next);
				break;
			}
		}
		expectToken("}");
		if (Double.isNaN(retval)) error("No matching key for "+ pivot);
		return retval;
	}

	// <ifThen> ::= <logic> {? <formula> : <formula>}
	protected double ifThen() {
		double value = logic();
		if (!tokenizer_.hasNext()) return value;
		String token = tokenizer_.next();
		if ("?".equals(token)) {
			double trueCase = formula();
			token = tokenizer_.next();
			if (!":".equals(token)) error(":", token);
			double falseCase = formula();
			return dtob(value) ? trueCase : falseCase;
		} else {
			tokenizer_.unread(token);
		}
		return value;
	}

	// <logic> ::= <comparison>{(&|\|)<comparison>}*
	protected double logic() {
		double value = comparison();
		if (!tokenizer_.hasNext()) return value;
		String token;
		NEXT_TOKEN:
		while (tokenizer_.hasNext()) {
			token = tokenizer_.next();
			switch (token) {
			case "&":
				value = btod(dtob(value) & dtob(comparison()));
				break;
			case "|":
				value = btod(dtob(value) | dtob(comparison()));
				break;
			default:
				tokenizer_.unread(token);
				break NEXT_TOKEN;
			}
		}
		return value;
	}

	// <comparison> ::= <sum> {(==|!=|<|>|<=|>=) <sum>}*
	protected double comparison() {
		double previous = sum();
		String token;
		if (tokenizer_.hasNext()) {
			token = tokenizer_.next();
			// whether comparisons take place or not
			switch (token) {
			case"==":case"!=":case"<":case">":
			case"<=":case">=":
				tokenizer_.unread(token);
				break;
			default:
				tokenizer_.unread(token);
				return previous;
			}
		} else {
			return previous;
		}

		boolean retval = true;
		double value;
		NEXT_TERM:
		while (tokenizer_.hasNext()) {
			token = tokenizer_.next();
			switch (token) {
			case "==":
				value = sum();
				retval &= previous == value;
				previous = value;
				continue;
			case "!=":
				value = sum();
				retval &= previous != value;
				previous = value;
				continue;
			case "<":
				value = sum();
				retval &= previous < value;
				previous = value;
				continue;
			case ">":
				value = sum();
				retval &= previous > value;
				previous = value;
				continue;
			case ">=":
				value = sum();
				retval &= previous >= value;
				previous = value;
				continue;
			case "<=":
				value = sum();
				retval &= previous <= value;
				previous = value;
				continue;
			default:
				tokenizer_.unread(token);
				break NEXT_TERM;
			}
		}
		return btod(retval);
	}

	// <sum> ::= <product> {(+|-) <product>}*
	protected double sum() {
		double value = product();
		while (tokenizer_.hasNext()) {
			String token = tokenizer_.next();
			switch (token) {
			case "+":
				value = value + product();
				break;
			case "-":
				value = value - product();
				break;
			default:
				tokenizer_.unread(token);
				return value;
			}
		}
		return value;
	}

	// <product> ::= <function> {(*|/|%) <function>}*
	protected double product() {
		double value = function();
		while (tokenizer_.hasNext()) {
			String token = tokenizer_.next();
			switch (token) {
			case "*":
				value = value * function();
				break;
			case "/":
				value = value / function();
				break;
			case "%":
				value = value % function();
				break;
			default:
				tokenizer_.unread(token);
				return value;
			}
		}
		return value;
	}

	// <function> ::= round<unary>|defined<key>|equals(<string>, <string>)|
	//               -<function>|(<formula>)|<literal>
	// <literal> ::= <boolean>|<number>
	protected double function() {
		String token = tokenizer_.next();
		switch (token) {
		case "round":
			return Math.rint(function());
		case "defined":
			return btod(setting.containsKey(tokenizer_.next()));
		case "equals":
			String next = tokenizer_.next();
			if (!"(".equals(next)) error("(", next);
			String str1 = tokenizer_.next();
			if (!(",".equals(next = tokenizer_.next()))) error(",", next);
			String str2 = tokenizer_.next();
			if (!(")".equals(next = tokenizer_.next()))) error(")", next);
			return btod(str1.equals(str2));
		case "-":
			return - function();
		case "(":
			double value = formula();
			token = tokenizer_.next();
			if (!")".equals(token)) error(")", token);
			return value;
		default:
			// <unary> ::= <literal>
			// <literal> ::= true|false
			if (token.equalsIgnoreCase("true")) {
				return btod(true);
			} else if (token.equalsIgnoreCase("false")) {
				return btod(false);
			}
			// <literal> ::= <number>
			if (Character.isDigit(token.charAt(0))){
				return Double.parseDouble(token);
			}
			error("Unexpected token: "+ token);
		}
		throw new InternalError("Never reached. ");
	}


	protected static double btod(boolean bool) {
		return bool ? 1 : 0;
	}
	protected static boolean dtob(double bool) {
		return bool != 0;
	}

	protected void expectToken(String expect) {
		String next = tokenizer_.next();
		if (!expect.equals(next)) error(expect, next);
	}

	protected void error(String expected, String actual) {
		error("Expected \""+ expected +"\", but found \""+ actual +"\"");
	}

	protected void error(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(tokenizer_.getSequence());
		sb.append("\n");
		for (int i = 1; i < tokenizer_.getCurrentPos(); i++) {
			sb.append(" ");
		}
		sb.append("^\n");

		throw new IllegalArgumentException(message + sb);
	}

	static class Tokenizer implements Iterator<String> {
		private static final String EOS = "\0";
		private int index = 0;
		private char[] seq_;
		private String nextToken_ = null;
		private String pushback_;
		private static final HashSet<Character> singleSymbol_ = new HashSet<>();

		{
			singleSymbol_.add('+');
			singleSymbol_.add('-');
			singleSymbol_.add('*');
			singleSymbol_.add('/');
			singleSymbol_.add('%');
			singleSymbol_.add('(');
			singleSymbol_.add(')');
			singleSymbol_.add('?');
			singleSymbol_.add(':');
			singleSymbol_.add(',');
			singleSymbol_.add('&');
			singleSymbol_.add('|');
			singleSymbol_.add('{');
			singleSymbol_.add('}');
			singleSymbol_.add(',');
		}

		Tokenizer(String formula) {
			seq_ = formula.toCharArray();
			next();
		}

		public void unread(String token) {
			pushback_ = token;
		}

		public String getSequence() {
			return String.valueOf(seq_, 0, seq_.length);
		}

		public int getCurrentPos() {
			if (hasNext()) {
				return index - nextToken_.length() - 1;
			} else {
				return index;
			}

		}

		@Override
		public boolean hasNext() {
			return pushback_ != null || nextToken_ != EOS;
		}

		@Override
		public String next() {
			if (!hasNext()) throw new NoSuchElementException();
			// return unread
			if (pushback_ != null) {
				String retval = pushback_;
				pushback_ = null;
				return retval;
			}
			// parse next
			String retval = nextToken_;
			nextToken_ = getNext();
			return retval;
		}

		private String getNext() {
			// skip white spaces
			while(index + 1 < seq_.length && seq_[index] == ' ') index++;
			if (index == seq_.length) return EOS;

			// single symbol except for =, <, >
			if (singleSymbol_.contains(seq_[index])) {
				return String.valueOf(seq_, index++, 1);
			}
			int startIdx = index;
			// !=, ==, <=, >=, < and >
			if (index + 1 < seq_.length) {
				if ((seq_[index] == '!' && seq_[index + 1] == '=') |
					(seq_[index] == '=' && seq_[index + 1] == '=') |
					(seq_[index] == '<' && seq_[index + 1] == '=') |
					(seq_[index] == '>' && seq_[index + 1] == '=')) {
					index += 2;
					return String.valueOf(seq_, startIdx, 2);
				} else {
					switch (seq_[index]) {
					case '=':
					case '>':
					case '<':
						return String.valueOf(seq_, index++, 1);
					}
				}
			}

			// read until a literal ends
			while(index < seq_.length) {
				if ((seq_[index] == ' ') ||
					(seq_[index] == '=') ||
					(singleSymbol_.contains(seq_[index]))) {
						break;
				}
				index++;
			}
			return String.valueOf(seq_, startIdx, index - startIdx);
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
