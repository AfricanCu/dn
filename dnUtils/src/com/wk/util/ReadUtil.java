package com.wk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.common.beans.Invoker;
import com.jery.ngsp.server.log.LoggerService;

public class ReadUtil {
	private static final int NAME_ROW_INDEX = 0;
	private static final int DATA_TYPE_ROW_INDEX = 1;
	private static final int DATA_ROW_INDEX = 2;
	private static final HashSet<TemplateCheckAbs> checkList = new HashSet<>();

	public static Properties loadFromURL(URL configURL) {
		Properties props = new Properties();
		InputStream istream = null;
		URLConnection uConn = null;
		try {
			uConn = configURL.openConnection();
			uConn.setUseCaches(false);
			istream = uConn.getInputStream();
			props.load(istream);
			return props;
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return null;
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 加载Properties
	 * 
	 * @param filePath
	 * @return
	 */
	public static Properties loadFromFile(String filePath) {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	private static boolean isRead(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			LoggerService.getLogicLog().error(
					"--------{} is not exists----------", filename);
			return false;
		}
		return true;
	}

	/**
	 * 读取csv文件
	 * 
	 * @param filePath
	 *            csv目录下的子文件夹目录名/csv的文件名
	 * @param includeHeader
	 *            list是否包含第一行
	 * @return List<String[]> String[]的每个值依次为csv文件每一行从左到右的单元格的值
	 */
	private static List<String[]> readCSV(String filePath,
			boolean includeHeader, boolean isBug) {
		List<String[]> list = new ArrayList<String[]>();
		if (!isRead(filePath)) {
			return list;
		}
		CsvReader reader = null;
		try {
			reader = new CsvReader(filePath, ',', Charset.forName("utf-8"));
			/** csv的第一行 * */
			reader.readHeaders();
			String[] headers = reader.getHeaders();
			if (includeHeader) {
				// 读取UTF-8格式有bug 需去掉第一个字符的空格
				if (isBug)
					headers[0] = headers[0].substring(1);
				list.add(headers);
			}
			/** 从第二行开始读 * */
			while (reader.readRecord()) {
				String[] values = reader.getValues();
				if (values.length != 0 && !StringUtils.isBlank(values[0])) {
					list.add(values);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/** 关闭reader * */
			if (reader != null) {
				reader.close();
			}
		}
		return list;
	}

	/**
	 * 解析csv文件数据
	 *
	 * @param <T>
	 * @param filePath
	 * @param clazz
	 * @param isBug
	 *            是否有读csv头的bug
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> explainCsvData(String filePath, Class<T> clazz,
			boolean isBug) throws Exception {
		List<String[]> workbook = readCSV(filePath, true, isBug);
		ClassInfo info = ClassInfo.getInstance(clazz);
		String[] nameRow = workbook.get(NAME_ROW_INDEX);
		String[] dataTypeRow = workbook.get(DATA_TYPE_ROW_INDEX);
		int firstCellNum = 0;
		int lastCellNum = dataTypeRow.length;
		List<T> list = new ArrayList<>();
		for (int dataIndex = DATA_ROW_INDEX; dataIndex < workbook.size(); dataIndex++) {
			String[] dataRow = workbook.get(dataIndex);
			if (dataRow != null) {
				T obj = clazz.newInstance();
				if (obj instanceof TemplateCheckAbs) {
					checkList.add((TemplateCheckAbs) obj);
				}
				list.add(obj);
				for (int columnIndex = firstCellNum; columnIndex < lastCellNum; columnIndex++) {
					String cell = dataRow[columnIndex];
					String dataType = dataTypeRow[columnIndex].trim()
							.toLowerCase();
					Invoker setInvoker = null;
					try {
						String trim = nameRow[columnIndex].trim();
						setInvoker = info.getSetInvoker(trim);
					} catch (Exception e) {
						LoggerService.getLogicLog().warn(e.getMessage(), e);
						setInvoker = null;
					}
					try {
						if (cell != null && setInvoker != null) {
							switch (dataType) {
							case "int":
								setInvoker.invoke(obj, new Object[] { Integer
										.parseInt(cell.toString()) });
								break;
							case "short":
								setInvoker.invoke(obj, new Object[] { Short
										.parseShort(cell.toString()) });
								break;
							case "string":
								setInvoker.invoke(obj,
										new Object[] { cell.toString() });
								break;
							case "double":
								setInvoker.invoke(obj, new Object[] { Double
										.parseDouble(cell.toString()) });
								break;
							case "float":
								setInvoker.invoke(obj, new Object[] { Float
										.parseFloat(cell.toString()) });
								break;
							case "boolean":
								setInvoker.invoke(obj, new Object[] { Boolean
										.parseBoolean(cell.toString()) });
								break;

							default:
								throw new Exception("filePath:" + filePath
										+ " class: " + clazz.getName()
										+ " col:" + columnIndex + ",错误数据类型！"
										+ dataType);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						LoggerService.getLogicLog().error(
								("filePath:" + filePath + " class: "
										+ clazz.getName() + " col:"
										+ columnIndex + ",错误数据类型！" + dataType));
						throw e;
					}
				}
			}
		}
		return list;
	}

	public static void check() throws Exception {
		for (TemplateCheckAbs check : checkList) {
			check.check();
		}
		LoggerService.getPlatformLog().error("检测完成！条目：{}", checkList.size());
		checkList.clear();
	}
}
