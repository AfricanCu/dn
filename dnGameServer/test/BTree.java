import java.util.Stack;

import com.jery.ngsp.server.log.LoggerService;

/**
 * 二叉树
 * 
 * @author Administrator
 *
 */
public class BTree {
	/**
	 * 解析为后缀表达式
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String parseAfterIterator(String str) throws Exception {
		BTree createBtree = new BTree();
		createBTree(createBtree, null, str);
		StringBuilder builder = new StringBuilder();
		afterIterator(getLeftestNode(createBtree), builder);
		return builder.toString();
	}

	/**
	 * 获取最左的节点
	 * 
	 * @param node
	 * @return
	 */
	public static BTree getLeftestNode(BTree node) {
		if (node.left.para == null) {
			return getLeftestNode(node.left);
		} else {
			return node;
		}
	}

	/**
	 * 获取最根的节点
	 * 
	 * @param node
	 * @return
	 */
	public static BTree getParentestNode(BTree node) {
		if (node.parent != null) {
			return getParentestNode(node.parent);
		} else {
			return node;
		}
	}

	/**
	 * 获取表达式树的后缀表达式
	 * 
	 * @param root
	 * @param builder
	 */
	public static void afterIterator(BTree root, StringBuilder builder) {
		if (root == null) {
			return;
		}
		if (root.left.para != null) {
			builder.append(root.left.para);
		}
		if (root.right.para != null) {
			builder.append(root.right.para);
		} else {
			BTree rl = getLeftestNode(root.right);
			afterIterator(rl, builder);
		}
		if (root.oparator != null)
			builder.append(root.oparator);
		afterIterator(root.parent, builder);
	}

	private Character oparator = null;
	private String para = null;
	private BTree parent = null;
	private BTree left = null;
	private BTree right = null;
	private static final char lkuo = '(';
	private static final String lkuoS = "(";
	private static final String rkuoS = ")";
	private static final char rkuo = ')';
	private static final char add = '+';
	private static final char minus = '-';
	private static final char mutiple = '*';
	private static final char divide = '/';

	/**
	 * 去除多余的括号
	 * 
	 * @param exp
	 * @return
	 */
	public static String eraserKuo(String exp) {
		if (exp.startsWith(lkuoS) && exp.endsWith(rkuoS)) {
			String substring = exp.substring(1, exp.length() - 1);
			Stack<Character> stack = new Stack<Character>();
			for (int i = 0; i < substring.length(); i++) {
				char charAt = substring.charAt(i);
				if (charAt == lkuo) {
					stack.push(charAt);
				} else if (charAt == rkuo) {
					if (!stack.isEmpty() && stack.peek() == lkuo) {
						stack.pop();
					} else
						stack.push(charAt);
				}
			}
			if (stack.isEmpty()) {
				String eraserKuo = eraserKuo(substring);
				while (!eraserKuo.equals(substring)) {
					substring = eraserKuo;
					eraserKuo = eraserKuo(substring);
				}
				return substring;
			}
		}
		return exp;
	}

	/**
	 * 
	 * @param exp
	 * @return
	 */
	static int rightIndex(String exp) {
		if (exp.startsWith(lkuoS)) {
			Stack<Character> stack = new Stack<Character>();
			stack.push(lkuo);
			int i = 1;
			for (; i < exp.length(); i++) {
				char charAt = exp.charAt(i);
				if (charAt == lkuo) {
					stack.push(charAt);
				} else if (charAt == rkuo) {
					if (!stack.isEmpty() && stack.peek() == lkuo) {
						stack.pop();
					} else
						stack.push(charAt);
				}
				if (stack.isEmpty()) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 创建BTree
	 * 
	 * @param exp
	 * @return
	 * @throws Exception
	 */
	public static void createBTree(BTree root, BTree left, String exp) throws Exception {
		if (left == null)
			exp = eraserKuo(exp);
		if (left == null && exp.length() == 1) {
			root.para = exp;
			return;
		}
		if (left != null) {
			root.left = left;
			left.parent = root;
		} else {
			int lbindex = -1;
			int leindex = -1;
			if (exp.charAt(0) == lkuo) {
				int indexOf = rightIndex(exp);
				if (indexOf == -1) {
					throw new Exception("括号不匹配！");
				}
				lbindex = 0;
				leindex = indexOf + 1;
			} else {
				lbindex = 0;
				leindex = 1;
			}
			BTree lBtree = new BTree();
			createBTree(lBtree, null, exp.substring(lbindex, leindex));
			BTree lbt = getParentestNode(lBtree);
			root.left = lbt;
			lbt.parent = root;
			char oparator = exp.charAt(leindex);
			if (isOpe(oparator)) {
				root.oparator = oparator;
			} else {
				LoggerService.getLogicLog().warn("no use" + exp.substring(leindex));
				return;
			}
			exp = exp.substring(leindex + 1);
		}
		int rbindex;
		int reindex;
		if (exp.charAt(0) == lkuo) {
			int indexOf = rightIndex(exp);
			if (indexOf == -1) {
				throw new Exception("括号不匹配！");
			}
			rbindex = 0;
			reindex = indexOf + 1;
		} else {
			rbindex = 0;
			reindex = 1;
		}
		if (reindex != exp.length()) {// 右边的还没搞定
			char o = exp.charAt(reindex);
			if (!isOpe(o)) {
				throw new Exception("不是操作符" + o);
			}
			if (priorThan(o, root.oparator)) {// 后面运算优先度高的放在右节点
				while (priorThan(o, root.oparator)) {
					int startIndex = reindex + 1;
					if (exp.charAt(startIndex) == lkuo) {
						int indexOf = rightIndex(exp.substring(startIndex));
						if (indexOf == -1) {
							throw new Exception("括号不匹配！");
						}
						reindex = startIndex + indexOf + 1;
					} else {
						reindex = startIndex + 1;
					}
					if (reindex == exp.length()) {
						break;
					}
					o = exp.charAt(reindex);
					if (!isOpe(o)) {
						throw new Exception("不是操作符" + o);
					}
				}
			}
			BTree rnBt = new BTree();
			createBTree(rnBt, null, exp.substring(rbindex, reindex));
			root.right = getParentestNode(rnBt);
			if (reindex != exp.length()) {// 没处理掉的放在母节点优先度低
				BTree pnBt = new BTree();
				pnBt.oparator = o;
				createBTree(pnBt, root, exp.substring(reindex + 1));

			}
		} else {
			BTree rBtree = new BTree();
			createBTree(rBtree, null, exp.substring(rbindex));
			root.right = getParentestNode(rBtree);
		}
	}

	/**
	 * 操作符优先级
	 * 
	 * 乘除大于加减
	 * 
	 * @param afterOpe
	 *            后面的操作符
	 * @param beforeOpe
	 *            前面的操作符
	 * @return
	 * @throws Exception
	 */
	private static boolean priorThan(char afterOpe, char beforeOpe) throws Exception {
		if (isOpe(afterOpe)) {
			if ((afterOpe == mutiple || afterOpe == divide) && (beforeOpe == add || beforeOpe == minus)) {
				return true;
			}
			return false;
		} else
			throw new Exception("不是操作符！");
	}

	private static boolean isOpe(char afterOpe) {
		return afterOpe == mutiple || afterOpe == divide || afterOpe == add || afterOpe == minus;
	}
}
