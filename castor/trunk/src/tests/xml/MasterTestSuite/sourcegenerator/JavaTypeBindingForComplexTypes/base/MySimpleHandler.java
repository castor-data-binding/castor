package base;

import java.awt.Color;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

public class MySimpleHandler extends GeneralizedFieldHandler {


	@Override
	public Object convertUponGet(Object value) {
		if (value == null)
			return null;
		Color c = (Color) value;
		return c.toString();
	}

	@Override
	public Object convertUponSet(Object value) {
		System.out.println("Creating color");
		String s = (String) value;
		Color c = Color.getColor(s);
		return c;
	}

	@Override
	public Class getFieldType() {
		return java.awt.Color.class;
		//return null;
	}
	
	@Override
	public Object newInstance(Object parent) throws IllegalStateException {
		return null;
	}
	
	@Override
	public Object newInstance(Object parent, Object[] args) throws IllegalStateException {
		return null;
	}
	
}
