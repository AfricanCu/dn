package com.jery.ngsp.server;

import com.jery.ngsp.server.impl.InterfaceFactoryImpl;

public class InterfaceFactoryManager {
	private static InterfaceFactory interfaceFactory;

	public static InterfaceFactory getInterfaceFactory() {
		if (interfaceFactory == null) {
			interfaceFactory = new InterfaceFactoryImpl();
		}
		return interfaceFactory;
	}
}
