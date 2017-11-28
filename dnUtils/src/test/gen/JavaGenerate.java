package test.gen;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wk.util.FileProcessor;

/**
 * 生成java文件
 *
 * @author lixing
 */
public class JavaGenerate {

	public static void gen(Class<?> clazz, String data) {
		int indexOf = clazz.getName().indexOf('$');
		while (indexOf != -1) {
			clazz = clazz.getSuperclass();
			indexOf = clazz.getName().indexOf('$');
		}
		Path onejavaPath = FileSystems.getDefault().getPath(
				JavaGenerate.getPath(clazz));
		Pattern onePatern = Pattern.compile("// 自动生成开始[\\s\\S]*// 自动生成结束");
		JavaObj javaObj = new JavaObj(onejavaPath, onePatern);
		JavaGenerate.replaceGenerate(javaObj, data);
		System.err.println(clazz.getName() + ".a(" + clazz.getSimpleName()
				+ ".java:1) - 生成成功！");
	}

	private static String getPath(Class<?> clazz) {
		return String.format("./src/%s.java", clazz.getName().substring(0)
				.replace('.', '/'));
	}

	/**
	 * 在java 文件重写某部分
	 *
	 * @param javaObj
	 * @param collections
	 */
	public static void replaceGenerate(JavaObj javaObj, String builder) {
		Set<OpenOption> openOpts = FileProcessor
				.getSyncOpenOption(FileProcessor.OpenType.ReadType);
		FileChannel fc = null;
		try {
			fc = (FileChannel.open(javaObj.javaPath, openOpts));
			int capacity = (int) fc.size();
			ByteBuffer out = ByteBuffer.allocate(capacity);
			fc.read(out);
			fc.close();
			byte[] array = out.array();
			String str = new String(array, FileProcessor.utf8_charset);
			if (str.equals("")) {
				return;
			}
			Matcher matcher = javaObj.patern.matcher(str);
			boolean find = matcher.find();
			fc = FileChannel.open(javaObj.javaPath, FileProcessor
					.getSyncOpenOption(FileProcessor.OpenType.ReWriteType));
			if (find) {
				String group = matcher.group();
				String stringBuilder = "// 自动生成开始\n" + builder + "// 自动生成结束";
				String replace = str.replace(group, stringBuilder);
				byte data[] = replace.getBytes(FileProcessor.utf8_charset);
				ByteBuffer buff = ByteBuffer.wrap(data);
				fc.write(buff);
				fc.close();
			} else {
				byte data[] = (str.substring(0, str.lastIndexOf('}'))
						+ "// 自动生成开始\n" + builder + "// 自动生成结束\n}")
						.getBytes(FileProcessor.utf8_charset);
				ByteBuffer buff = ByteBuffer.wrap(data);
				fc.write(buff);
				fc.close();
				System.err.println("在java文件中找不到对应位置,append？");
			}
		} catch (IOException x) {
			x.printStackTrace();
		} finally {
			try {
				if (fc != null) {
					fc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class JavaObj {

		private Path javaPath;
		private Pattern patern;

		public JavaObj(String javaPath, String patern) {
			this.javaPath = FileSystems.getDefault().getPath(javaPath);
			this.patern = Pattern.compile(patern);
		}

		public JavaObj(Path javaPath, Pattern patern) {
			this.javaPath = javaPath;
			this.patern = patern;
		}

		/**
		 *
		 * @param javaPath
		 * @param patern
		 * @param flags
		 *            - 正则表达式设置标识
		 */
		public JavaObj(String javaPath, String patern, int flags) {
			this.javaPath = FileSystems.getDefault().getPath(javaPath);
			this.patern = Pattern.compile(patern, flags);
		}

		/**
		 *
		 * @param javaPath
		 * @param patern
		 * @param flags
		 *            - 正则表达式设置标识
		 */
		public JavaObj(Path javaPath, String patern, int flags) {
			this.javaPath = javaPath;
			this.patern = Pattern.compile(patern, flags);
		}
	}

}
