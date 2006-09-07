package base;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

import generated.Item;

public class MyHandler extends GeneralizedFieldHandler {


	public Object convertUponGet(Object arg0) {
		// Uncomment this to get correct behaviour
		Item i = new Item();
		MyItem mi = (MyItem) arg0;
		i.setName(mi.getName());
		return i;
//		return arg0;
	}

	public Object convertUponSet(Object arg0) {
		System.out.println("Converting to MyItem");
		Item i = (Item) arg0;
		MyItem mi = new MyItem();
		mi.setName(i.getName());
		return mi;
	}

	public Class getFieldType() {
		return MyItem.class;
	}
}
