package test.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import test.ExcelTools;
import test.gen.JavaGenerate;

import com.wk.logic.enm.MsgId;
import com.wk.util.ReadUtil;

public class GenMsg {
	public static final String msgIdPath = "../douniuProto/消息.xlsx";
	public static final String noticeTextPath = "D:/mj/mjProto/NoticeText.csv";

	public static void main(String[] args) throws Exception {
		// try {
		// String cmdStr = "D:\\soft\\workspace4\\proto\\make_proto.bat";
		// Process process = Runtime.getRuntime().exec(cmdStr);
		// byte[] buffer = new byte[1000];
		// // 将调用结果打印到控制台上
		// InputStream in = process.getInputStream();
		// int count;
		// while ((count = in.read(buffer)) != -1) {
		// System.err.println(new String(buffer, 0, count));
		// }
		// process.getOutputStream().write(new byte[] { 13, 10 });
		// while ((count = in.read(buffer)) != -1) {
		// System.err.println(new String(buffer, 0, count));
		// }
		// process.getOutputStream().write(new byte[] { 13, 10 });
		// while ((count = in.read(buffer)) != -1) {
		// System.err.println(new String(buffer, 0, count));
		// }
		// in.close();
		// process.destroy();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println(System.currentTimeMillis());
	}

	public static class noticeText {
		private String Key;
		private int Value;
		private String Text;
	}

	/**
	 * 解析静态数据
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void noticeText() throws Exception {
		List<GenMsg.noticeText> explainCsvData = ReadUtil.explainCsvData(
				noticeTextPath, noticeText.class, true);
		StringBuilder builder = new StringBuilder("public class NoticeText {\n");
		for (noticeText noticeText : explainCsvData) {
			builder.append("		/** ").append(noticeText.Text).append("**/\n");
			builder.append("		public static final int ").append(noticeText.Key)
					.append("=").append(noticeText.Value).append(";\n");
		}
		builder.append("}\n");
		JavaGenerate.gen(noticeText.class, builder.toString());
	}

	public static void msgId() throws FileNotFoundException, IOException {
		Workbook workbook = ExcelTools.getWorkbook(new File(msgIdPath));
		Sheet sheet = workbook.getSheet("消息id");
		Stack<DataRow> list = new Stack<>();
		for (int dataIndex = 0; dataIndex <= sheet.getLastRowNum(); dataIndex++) {
			Row dataRow = sheet.getRow(dataIndex);
			Row dataRowNext = sheet.getRow(dataIndex + 1);
			if (dataRow != null) {
				DataRow row = new DataRow(dataRow);
				DataRow rowNext = null;
				if (dataRowNext != null) {
					rowNext = new DataRow(dataRowNext);
				}
				String resMsgName = rowNext != null ? rowNext.msgName : "null";
				if (row.msgName.endsWith("Cm")
						&& resMsgName.endsWith("Sm")
						&& (resMsgName.substring(0, resMsgName.length() - 2) + "Cm")
								.equals(row.msgName)) {
				} else {
					resMsgName = "null";
				}
				row.build.append("/**" + row.note + " " + row.msgId + "**/\n"
						+ row.msgName + "(" + row.msgId + "," + "\"" + row.note
						+ "\"," + row.msgClass + ".getDefaultInstance(),"
						+ resMsgName + "," + row.period + "),\n");
				list.push(row);
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		while (!list.isEmpty()) {
			stringBuilder.append(list.pop().build.toString());
		}
		JavaGenerate.gen(MsgId.class, stringBuilder.toString());
	}

	public static class DataRow {
		private int msgId;
		private String msgClass;
		private String note;
		private String msgName;
		/**
		 * 在这段时间内不能重复发（毫秒）
		 */
		private long period;
		private StringBuilder build = new StringBuilder();

		DataRow(Row dataRow) {
			this.msgId = (int) dataRow.getCell(0).getNumericCellValue();
			this.msgClass = dataRow.getCell(1).toString();
			Cell cell3 = dataRow.getCell(2);
			this.note = cell3 == null ? "" : cell3.toString();
			Cell cell2 = dataRow.getCell(3);
			this.period = (long) (cell2 == null ? 250 : cell2
					.getNumericCellValue());
			this.msgName = msgClass.substring(msgClass.lastIndexOf(".") + 1,
					msgClass.length());
			Cell cell = dataRow.getCell(4);
		}
	}
}
