package test.client;

public class Item {
	private String name;
	private String value;

	public Item() {
		super();
	}

	public Item(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static final Item NullItem = new Item("", "");

	public boolean isNull() {
		return this == NullItem;
	}

}