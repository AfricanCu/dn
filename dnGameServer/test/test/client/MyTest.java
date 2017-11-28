package test.client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * 本人的测试类
 * @author EMS-WX-i5-01
 *
 */
public class MyTest {
	public static final Properties properties = new Properties();
	
	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<>();
		URL resource = TestClient.class.getClassLoader().getResource(
				"test.properties");
		properties.load(resource.openStream());
		for(int i = 0; i < 100; i++){
			String property = properties.getProperty("login"+i);
			if(property != null)
				list.add(property);
		}
		
		for(String s : list){
			System.out.println(s);
		}
		
	}
}
