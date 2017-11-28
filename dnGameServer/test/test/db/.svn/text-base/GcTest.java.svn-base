package test.db;

public class GcTest {

	private int id;

	public GcTest(int id) {
		super();
		this.id = id;
	}

	private StringBuilder builder = new StringBuilder("ddddddd");

	private XxObjEx ex = new XxObjEx();

	private class XxObj {

		public void aa() {
			System.err.println(builder.toString());
		}

	}

	private class XxObjEx {

		public void aa() {
			System.err.println(builder.toString());
		}

	}

	public static void main(String[] args) {
		GcTest gcTest = new GcTest(1);
		XxObj obj = gcTest.new XxObj();
		obj.aa();
		GcTest gcTest2 = new GcTest(2);
		gcTest2.ex = gcTest.ex;
		gcTest = null;
		GcTest gcTest3 = new GcTest(3);
		System.gc();
		test2(obj);
	}

	public static void test(GcTest gcTest2) {
		final GcTest gcTest = gcTest2;
		System.gc();
		ThreadEx threadEx = new ThreadEx(gcTest);
		threadEx.start();
	}

	public static void test2(XxObj obj) {
		final XxObj gcTest = obj;
		System.gc();
		ThreadEx2 threadEx = new ThreadEx2(gcTest);
		threadEx.start();
	}

	static class ThreadEx2 extends Thread {
		private XxObj gcTest;

		public ThreadEx2(XxObj gcTest2) {
			super();
			this.gcTest = gcTest2;
		}

		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.gc();
			gcTest.aa();
		};
	}

	static class ThreadEx extends Thread {
		private GcTest gcTest;

		public ThreadEx(GcTest gcTest) {
			super();
			this.gcTest = gcTest;
		}

		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.gc();
			gcTest.ex.aa();
		};
	}

}
