/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jery.ngsp.server.log.LoggerService;

/**
 * 逆波兰表达式（后缀表达式）
 * 
 * @author Lione
 * @param <K>
 * @param <V>
 */
public class ReversePolishExpression<K, V> implements Map.Entry<K, V> {

	public static final String TAG = "##";

	final K key;
	V value;

	public ReversePolishExpression(K k, V v) {
		value = v;
		key = k;

	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public V setValue(V value) {
		return this.value = value;
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		LoggerService.getLogicLog().warn(
				"{}",
				5 * (6 + 8) - (1 * 22 - (1 + 2 * 8) * 99) - (3 - 1 + 3 * 3)
						* 5.8 / 5.0);
		double caculate = caculate("",
				"5 * (6 + 8) - (1 * 22 - (1 + 2 * 8)*99) - (3 - 1 + 3 * 3) * 5.8 / 5.0");
		LoggerService.getLogicLog().warn("{}", caculate);
	}

	/**
	 * @param replaceStr
	 *            格式为xx##数字##zz##数字
	 * @param exp
	 *            标准数学表达式
	 */
	public static double caculate(String replaceStr, String exp)
			throws Exception {
		if (replaceStr == null) {
			replaceStr = "";
		}
		if (exp == null || exp.trim().equals("")) {
			return 0;
		}
		replaceStr = replaceStr.replaceAll("\\s", "");
		exp = exp.replaceAll("\\s", "");// 替换所有空白字符
		// \u4E00-\u9FA5 \uF900-\uFA2D----------- 中文字符范围
		char indexc = '\uF900';
		Pattern compile = Pattern.compile("[a-zA-Z_]+");// 替换所有单词为中文字符
		Matcher matcher = compile.matcher(exp);
		while (matcher.find()) {
			indexc++;
			String str = "" + indexc;
			String group = matcher.group();
			replaceStr = replaceStr.replace(group, str);
			LoggerService.getLogicLog().warn(group + "=" + str);
			exp = exp.replace(group, str);
		}
		Pattern datacompile = Pattern.compile("[\\p{Digit}\\.]+");// 替换所有的数字为中文
		// &&[^\\+\\-\\*/\\(\\)]]+
		Matcher datamatcher = datacompile.matcher(exp);
		while (datamatcher.find()) {
			indexc++;
			String str = "" + indexc;
			String group = datamatcher.group();
			if (replaceStr.trim() != "")
				replaceStr = replaceStr + TAG + indexc + TAG + group;
			else {
				replaceStr = indexc + TAG + group;
			}
			LoggerService.getLogicLog().warn(group + "=" + str);
			exp = exp.replaceFirst(group, str);
		}
		LoggerService.getLogicLog().warn(exp);
		LoggerService.getLogicLog().warn(replaceStr);
		HashMap<Character, Double> map = new HashMap<Character, Double>();
		String split[] = replaceStr.split(TAG);
		for (int i = 0; i < split.length; i += 2) {
			String string = split[i];
			if (string.length() != 1) {
				throw new Exception("未替换" + string);
			}
			map.put(string.charAt(0), Double.parseDouble(split[i + 1]));
		}
		exp = BTree.parseAfterIterator(exp);
		LoggerService.getLogicLog().warn(exp);
		ParsePost aParser = new ParsePost(exp, map);
		double output = aParser.doParse();
		return output;
	}
}

class StackX {
	private final int maxSize;
	private final double[] stackArray;
	private int top;
	private static boolean debug = false;

	// --------------------------------------------------------------
	public StackX(int size) // constructor
	{
		maxSize = size;
		stackArray = new double[maxSize];
		top = -1;
	}

	// --------------------------------------------------------------
	public void push(double j) // put item on top of stack
	{
		stackArray[++top] = j;
	}

	// --------------------------------------------------------------
	public double pop() // take item from top of stack
	{
		return stackArray[top--];
	}

	// --------------------------------------------------------------
	public double peek() // peek at top of stack
	{
		return stackArray[top];
	}

	// --------------------------------------------------------------
	public boolean isEmpty() // true if stack is empty
	{
		return (top == -1);
	}

	// --------------------------------------------------------------
	public boolean isFull() // true if stack is full
	{
		return (top == maxSize - 1);
	}

	// --------------------------------------------------------------
	public int size() // return size
	{
		return top + 1;
	}

	// --------------------------------------------------------------
	public double peekN(int n) // peek at index n
	{
		return stackArray[n];
	}

	// --------------------------------------------------------------
	public void displayStack(String s) {
		if (debug) {
			System.out.print(s);
			System.out.print("Stack (bottom-->top): ");
			for (int j = 0; j < size(); j++) {
				System.out.print(peekN(j));
				System.out.print(' ');
			}
			LoggerService.getLogicLog().warn("");
		}
	}
	// --------------------------------------------------------------
} // end class StackX
// //////////////////////////////////////////////////////////////

class ParsePost {

	private StackX theStack;
	private final String input;
	HashMap<Character, Double> map;

	// --------------------------------------------------------------
	public ParsePost(String s, HashMap<Character, Double> map) {

		input = s;
		this.map = map;
	}

	// --------------------------------------------------------------
	public double doParse() {
		theStack = new StackX(input.length()); // make new stack
		char ch;
		int j;
		double num1, num2, interAns;

		for (j = 0; j < input.length(); j++) // for each char,
		{
			ch = input.charAt(j); // read from input
			theStack.displayStack("" + ch + " "); // *diagnostic*
			if ((ch >= 'a' && ch <= 'z') || (ch >= '\u4E00' && ch <= '\u9FA5')
					|| (ch >= '\uF900' && ch <= '\uFA2D')) // if
															// it's
															// a
															// 中文字符\u4E00-\u9FA5
															// \uF900-\uFA2D
			{
				theStack.push(map.get(ch)); // push it
			} else // it's an operator
			{
				num2 = theStack.pop(); // pop operands
				num1 = theStack.pop();
				switch (ch) // do arithmetic
				{
				case '+':
					interAns = num1 + num2;
					break;
				case '-':
					interAns = num1 - num2;
					break;
				case '*':
					interAns = num1 * num2;
					break;
				case '/':
					interAns = num1 / num2;
					break;
				default:
					interAns = 0;
				} // end switch
				theStack.push(interAns); // push result
			} // end else
		} // end for
		interAns = theStack.pop(); // get answer
		return interAns;
	} // end doParse()
} // end class ParsePost
// //////////////////////////////////////////////////////////////
