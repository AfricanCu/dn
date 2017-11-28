package test.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Stack;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.wk.engine.net.InnerMsgId;

import test.ExcelTools;

public class GenInnerMsg {

	public static void innerMsgId() throws FileNotFoundException, IOException,
			URISyntaxException {
		Workbook workbook = ExcelTools.getWorkbook(new File(GenInnerMsg.class
				.getResource("/test/inner消息.xlsx").toURI()));
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
				if (resMsgName.endsWith("bk")
						&& (resMsgName.substring(0, resMsgName.length() - 2))
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
		JavaGenerate.gen(InnerMsgId.class, stringBuilder.toString());
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
			this.note = dataRow.getCell(2).toString();
			Cell cell2 = dataRow.getCell(3);
			this.period = (long) (cell2 == null ? 10000 : cell2
					.getNumericCellValue());
			this.msgName = msgClass.substring(msgClass.lastIndexOf(".") + 1,
					msgClass.length());
		}
	}
}
