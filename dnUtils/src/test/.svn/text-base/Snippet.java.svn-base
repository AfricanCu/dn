package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.jery.ngsp.server.InterfaceFactoryManager;
import com.jery.ngsp.server.dirtyword.DirtyWordsManager;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.db.dao.RoomDao;

public class Snippet {

	public static void main5(String[] args) throws URISyntaxException {
		LoggerService.initDef();
		DirtyWordsManager dirtyWordsManager = InterfaceFactoryManager
				.getInterfaceFactory().getDirtyWordsManager();
		// 脏词管理启动
		dirtyWordsManager.loadData(null);
		String checkDirtyWord = dirtyWordsManager.checkDirtyWord("mya", true);
		System.err.println(checkDirtyWord);
		// alterXml();
	}

	/**
	 * 修改xml文件元素内容
	 * 
	 * @param type
	 * @throws URISyntaxException
	 */
	public static void alterXml() throws URISyntaxException {
		try {
			URL resource = Snippet.class.getClassLoader().getResource(
					"DirtyWords.xml");
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(resource);
			Element root = document.getRootElement();
			Element element = root.element("dirtyWords");
			List<Element> elements = element.elements("word");

			HashMap<Integer, ArrayList<Element>> map = new HashMap<>();
			for (int i = 1; i <= 100; i++) {
				map.put(i, new ArrayList<Element>());
			}
			for (Element ele : elements) {
				int length = ele.getText().length();
				map.get(length).add(ele);
			}
			File file = new File("./a.xml");
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (int k : map.keySet()) {
				ArrayList<Element> arrayList = map.get(k);
				for (Element ele : arrayList) {
					writer.write("	<word>" + ele.getTextTrim() + "</word>\n");
				}
			}
			writer.flush();
			writer.close();
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			// 输出全部原始数据，并用它生成新的我们需要的XML文件
			OutputStream outputStream = new FileOutputStream(new File(
					"./DirtyWords.xml"));
			XMLWriter writer2 = new XMLWriter(outputStream, format);
			writer2.write(document); // 输出到文件
			writer2.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main2(String[] args) throws SQLException {
		int[] aa = new int[] { 1, 2, 3, 4, 5, 6 };
		IbatisDbServer.getGmSqlMapper().startTransaction();
		try {
			for (int id : aa) {
				try {
					int resetRoom = RoomDao.resetRoom(id);
					if (resetRoom != 1) {
						LoggerService.getLogicLog().error("没有重置房间成功！roomId:{}",
								id);
					}
				} catch (SQLException e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
					LoggerService.getLogicLog().error("没有重置房间成功！roomId:{}", id);
				}
			}
			IbatisDbServer.getGmSqlMapper().commitTransaction();
		} finally {
			IbatisDbServer.getGmSqlMapper().endTransaction();
		}
	}

	public static void main(String[] args) throws SQLException {
		int resetRoom = RoomDao.resetRoom(10);
		System.err.println(resetRoom);

	}
}
