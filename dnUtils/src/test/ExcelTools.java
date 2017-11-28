package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.common.beans.Invoker;
import com.jery.ngsp.server.log.LoggerService;

/**
 *
 * @author Administrator
 */
public class ExcelTools {

	/**
	 * 获取excel workbook
	 *
	 * @param file
	 * @return
	 * @throws java.io.FileNotFoundException
	 */
	public static Workbook getWorkbook(File file) throws FileNotFoundException,
			IOException {
		Workbook workbook = null;
		if (file.getName().endsWith(".xls")) {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} else if (file.getName().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}
		return workbook;
	}

	private static final int NAME_ROW_INDEX = 1;
	private static final int DATA_TYPE_ROW_INDEX = 2;
	private static final int DATA_ROW_INDEX = 3;

	/**
	 * 解析excel文件数据
	 *
	 * @param <T>
	 * @param filePath
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> explainExcelData(String filePath, Class<T> clazz)
			throws Exception {
		Workbook workbook = ExcelTools.getWorkbook(new File(filePath));
		Sheet sheet = workbook.getSheet(clazz.getSimpleName());
		ClassInfo info = ClassInfo.getInstance(clazz);
		Row nameRow = sheet.getRow(NAME_ROW_INDEX);
		Row dataTypeRow = sheet.getRow(DATA_TYPE_ROW_INDEX);
		int firstCellNum = dataTypeRow.getFirstCellNum();
		int lastCellNum = dataTypeRow.getLastCellNum();
		List<T> list = new ArrayList<>();
		for (int dataIndex = DATA_ROW_INDEX; dataIndex <= sheet.getLastRowNum(); dataIndex++) {
			Row dataRow = sheet.getRow(dataIndex);
			if (dataRow != null) {
				T obj = clazz.newInstance();
				list.add(obj);
				for (int columnIndex = firstCellNum; columnIndex < lastCellNum; columnIndex++) {
					Cell cell = dataRow.getCell(columnIndex);
					String dataType = dataTypeRow.getCell(columnIndex)
							.toString().trim();

					Invoker setInvoker = null;
					try {
						setInvoker = info.getSetInvoker(nameRow
								.getCell(columnIndex).toString().trim());
					} catch (Exception e) {
						LoggerService.getLogicLog().warn(e.getMessage());
						setInvoker = null;
					}
					if (cell != null && setInvoker != null) {
						switch (dataType) {
						case "int":
							setInvoker.invoke(obj, new Object[] { Integer
									.parseInt(cell.toString()) });
							break;
						case "String":
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
									+ " sheet: " + sheet.getSheetName()
									+ " col:" + columnIndex + ",错误数据类型！"
									+ dataType);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 解析excel文件配置赋值对象
	 *
	 * @param <T>
	 * @param filePath
	 * @param obj
	 * @throws Exception
	 */
	public static <T> T loadExcelConfig(String filePath, Class<T> clazz)
			throws Exception {
		T obj = clazz.newInstance();
		loadExcelConfig(filePath, obj);
		return obj;
	}

	/**
	 * 解析excel文件配置赋值对象
	 *
	 * @param <T>
	 * @param filePath
	 * @param obj
	 * @throws Exception
	 */
	public static <T> void loadExcelConfig(String filePath, T obj)
			throws Exception {
		Workbook workbook = ExcelTools.getWorkbook(new File(filePath));
		Sheet sheet = workbook.getSheet(obj.getClass().getSimpleName());
		ClassInfo info = ClassInfo.getInstance(obj.getClass());
		Row nameRow = sheet.getRow(NAME_ROW_INDEX);
		Row dataTypeRow = sheet.getRow(DATA_TYPE_ROW_INDEX);
		int firstCellNum = dataTypeRow.getFirstCellNum();
		int lastCellNum = dataTypeRow.getLastCellNum();
		int dataIndex = DATA_ROW_INDEX;
		Row dataRow = sheet.getRow(dataIndex);
		if (dataRow != null) {
			for (int columnIndex = firstCellNum; columnIndex < lastCellNum; columnIndex++) {
				Cell cell = dataRow.getCell(columnIndex);
				String dataType = dataTypeRow.getCell(columnIndex).toString()
						.trim();
				Invoker setInvoker = null;
				try {
					setInvoker = info.getSetInvoker(nameRow
							.getCell(columnIndex).toString().trim());
				} catch (Exception e) {
					LoggerService.getLogicLog().debug(e.getMessage(), e);
					setInvoker = null;
				}
				if (cell != null && setInvoker != null) {
					switch (dataType) {
					case "int":
						setInvoker.invoke(obj, new Object[] { Integer
								.parseInt(cell.toString()) });
						break;
					case "String":
						setInvoker
								.invoke(obj, new Object[] { cell.toString() });
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
					case "long":
						setInvoker
								.invoke(obj, new Object[] { Long.parseLong(cell
										.toString()) });
						break;
					default:
						throw new Exception("filePath:" + filePath + " sheet: "
								+ sheet.getSheetName() + " col:" + columnIndex
								+ ",错误数据类型！" + dataType);
					}
				}
			}
		}
	}

	/**
	 * 解析excel文件数据为java代码
	 *
	 * @param <T>
	 * @param filePath
	 * @param clazz
	 * @param name
	 *            生成的变量名
	 * @return
	 * @throws Exception
	 */
	public static <T> String translateJava(String filePath, Class<T> clazz,
			String name, boolean isAddConstruct) throws Exception {
		Workbook workbook = ExcelTools.getWorkbook(new File(filePath));
		Sheet sheet = workbook.getSheet(clazz.getSimpleName());
		ClassInfo info = ClassInfo.getInstance(clazz);
		Row nameRow = sheet.getRow(NAME_ROW_INDEX);
		Row dataTypeRow = sheet.getRow(DATA_TYPE_ROW_INDEX);
		int firstCellNum = dataTypeRow.getFirstCellNum();
		int lastCellNum = dataTypeRow.getLastCellNum();
		String canonicalName = clazz.getCanonicalName();
		StringBuilder constructBuilder = new StringBuilder("public "
				+ clazz.getSimpleName() + "(){}\n" + "public "
				+ clazz.getSimpleName() + "(");
		StringBuilder constructBuilder2 = new StringBuilder();

		StringBuilder build = new StringBuilder("public static final "
				+ canonicalName + " []" + name + "= {");
		for (int dataIndex = DATA_ROW_INDEX; dataIndex <= sheet.getLastRowNum(); dataIndex++) {
			Row dataRow = sheet.getRow(dataIndex);
			if (dataRow != null) {
				build.append("new " + canonicalName + "(");
				for (int columnIndex = firstCellNum; columnIndex < lastCellNum; columnIndex++) {
					Cell cell = dataRow.getCell(columnIndex);
					String dataType = dataTypeRow.getCell(columnIndex)
							.toString().trim();
					Invoker setInvoker = null;
					String trim = nameRow.getCell(columnIndex).toString()
							.trim();
					try {
						setInvoker = info.getSetInvoker(trim);
					} catch (Exception e) {
						LoggerService.getLogicLog().warn(e.getMessage());
						setInvoker = null;
					}
					if (cell != null && setInvoker != null) {
						if (dataIndex == DATA_ROW_INDEX) {
							constructBuilder.append(dataType + " " + trim);
							constructBuilder2.append("this." + trim + "="
									+ trim + ";");
						}
						switch (dataType) {
						case "int":
							build.append(Integer.parseInt(cell.toString()));
							break;
						case "String":
							build.append("\"" + cell.toString() + "\"");
							break;
						case "double":
							build.append(Double.parseDouble(cell.toString()));
							break;
						case "float":
							build.append(Float.parseFloat(cell.toString())
									+ "f");
							break;
						case "boolean":
							build.append(Boolean.parseBoolean(cell.toString()));
							break;
						default:
							throw new Exception("filePath:" + filePath
									+ " sheet: " + sheet.getSheetName()
									+ " col:" + columnIndex + ",错误数据类型！"
									+ dataType);
						}
						if (columnIndex < lastCellNum - 1) {
							build.append(",");
							if (dataIndex == DATA_ROW_INDEX) {
								constructBuilder.append(",");
								constructBuilder2.append("\n");
							}
						}
					} else {
						LoggerService.getLogicLog().warn("找不到参数{}", trim);
					}
				}
				build.append(")");
				if (dataIndex == DATA_ROW_INDEX) {
					constructBuilder.append(")");

				}
				if (dataIndex < sheet.getLastRowNum()) {
					build.append(",\n");
				}
			}
		}
		build.append("};\n");
		if (isAddConstruct) {
			build.append("\n" + constructBuilder.toString() + "{\n"
					+ constructBuilder2.toString() + "\n}");
		}
		return build.toString();
	}
}
