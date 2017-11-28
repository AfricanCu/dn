import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import sun.misc.Unsafe;

public class UnsafeDemo<K, V> {
	abstract class HashIterator {
		public Map.Entry<K, V> nextEntry() {
			return new Map.Entry<K, V>() {

				@Override
				public K getKey() {
					return null;
				}

				@Override
				public V getValue() {
					return null;
				}

				@Override
				public V setValue(V value) {
					return null;
				}
			};
		}

		public boolean hasMoreElements() {
			return false;
		}

		public boolean hasNext() {
			return false;
		}

		public void remove() {
		}
	}

	final class KeyIterator extends HashIterator implements Iterator<K>,
			Enumeration<K> {
		public final K next() {
			return super.nextEntry().getKey();
		}

		public final K nextElement() {
			return super.nextEntry().getKey();
		}
	}

	public static void main1(String[] args) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		Field f = Unsafe.class.getDeclaredField("theUnsafe"); // Internal
																// reference
		f.setAccessible(true);
		Unsafe unsafe = (Unsafe) f.get(null);

		// This creates an instance of player class without any initialization
		Player p = (Player) unsafe.allocateInstance(Player.class);
		System.out.println(p.getAge()); // Print 0

		p.setAge(45); // Let's now set age 45 to un-initialized object
		System.out.println(p.getAge()); // Print 45
	}
}

class Player {
	private int age = 12;

	private Player() {
		this.age = 50;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}