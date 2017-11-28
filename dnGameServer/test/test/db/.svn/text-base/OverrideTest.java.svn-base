package test.db;

public class OverrideTest {

	private int id;

	public OverrideTest(int id) {
		super();
		this.id = id;
	}

	private StringBuilder builder = new StringBuilder("ddddddd");

	private XxObjEx ex = new XxObjEx();

	private class XxObj {

		public void aa() {
			System.err.println("super aa");
			XxObj oo= this;
			oo.cc();
		}

		public void bb() {
			this.aa();
			System.err.println("bb");
		}

		public void cc() {
			System.err.println("cc");
		}
	}

	private class XxObjEx extends XxObj {

		public void aa() {
			super.aa();
			System.err.println("extends aa");
		}

		public void bb() {
			super.aa();
			System.err.println("bb");
		}

		@Override
		public void cc() {
			System.err.println("extends cc");
		}

	}

	public static void main(String[] args) {
		OverrideTest overrideTest = new OverrideTest(1);
		XxObjEx xxObjEx = overrideTest.new XxObjEx();
		xxObjEx.bb();
	}
}
