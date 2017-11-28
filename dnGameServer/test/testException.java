import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;

public class testException {

	public static void main(String[] args) {
		System.err.println("b1111".matches(".*11.*"));
	}
	
	public static void main1(String[] args) {
		ServerConfig.initLog4J();
		Exception e = xx();
		System.err.println(e.getStackTrace()[0]);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(baos));
		String exception = baos.toString();
		System.err.println("baos:" + exception);
		System.err.println(new Exception().getStackTrace()[0]);
		LoggerService.getPlatformLog().error("{}",
				new Exception().getStackTrace()[0]);
		System.err.println(a()||b()||c());
	}

	private static boolean a() {
		System.err.println("a");
		return false;
	}

	private static boolean b() {
		System.err.println("b");
		return true;
	}

	private static boolean c() {
		System.err.println("c");
		return false;
	}

	void xy() {
	}

	static Exception xx() {
		try {
			throw new Exception("xx");
		} catch (Exception e) {
			return e;
		}
	}
}
