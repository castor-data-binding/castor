public class Testable {
	
	public String nativeString;
	public String naturalGSString;
	public String unnaturalGSString;
	public boolean didGetNaturalGSString = false;
	public boolean didGetUnnaturalGSString = false;
	public boolean didSetNaturalGSString = false;
	public boolean didSetUnnaturalGSString = false;
	public String readOnlyString = "readonly";
	
	public int nativeInt;
	public int naturalGSInt;
	public boolean didGetNaturalGSInt = false;
	public boolean didSetNaturalGSInt = false;

	public Integer nativeInteger;
	public Integer naturalGSInteger;
	public boolean didGetNaturalGSInteger = false;
	public boolean didSetNaturalGSInteger = false;

	public boolean naturalBoolGS = false;
	public boolean didGetNaturalBoolGS = false;
	public boolean didSetNaturalBoolGS = false;

	public boolean naturalBoolIsS = false;
	public boolean didGetNaturalBoolIsS = false;
	public boolean didSetNaturalBoolIsS = false;

	// Make a bunch of minefield methods that do bugger all.
	public NativeCollections getParent(NativeCollections collections) { throw new RuntimeException(); }
	public void setParent(NativeCollections collections) { throw new RuntimeException(); }
	public String getInvalidCall(String str) { throw new RuntimeException(); }
	public void setInvalidCall(String str) { throw new RuntimeException(); }
	
	// Test a no-op, mapped thing that doesn't ever appear in xml.
	public String getThing() { throw new RuntimeException(); }
	public void setThing(String str) { throw new RuntimeException(); }
	
	// There is no valid native string method.
	public String getNativeString() { throw new RuntimeException(); }
	public void setNativeString(String str) { throw new RuntimeException(); }

	// Create some missplets and go.
	public String getNaturalGsString() { throw new RuntimeException(); }
	public void setNaturalGsString(String str) { throw new RuntimeException(); }
	public String getnaturalGSString() { throw new RuntimeException(); }
	public void setnaturalGSString() { throw new RuntimeException(); }
	public void setNaturalGSString(Object obj) { throw new RuntimeException(); }
	public void setnaturalgsstring(String str) { throw new RuntimeException(); }

	// And create the real ones.
	public String getNaturalGSString() { didGetNaturalGSString = true; return naturalGSString; }
	public void setNaturalGSString(String str) { if (str==null) throw new RuntimeException("null set"); naturalGSString = str; didSetNaturalGSString = true; }

	// Now create some unnatural ones.
	public String getUnnaturalGSString() { throw new RuntimeException(); }
	public void setUnnaturalGSString(String str) { throw new RuntimeException(); }
	public String getUnnaturalGSStringTest() { didGetUnnaturalGSString = true; return unnaturalGSString; }
	public void setUnnaturalGSStringTest(String str) { if (str==null) throw new RuntimeException("null set"); this.unnaturalGSString = str; didSetUnnaturalGSString = true; }
	
	// Test read-only works.
	public String getReadOnlyString() { return this.readOnlyString; }
	public void setReadOnlyString(String str) { throw new RuntimeException(); }
	
	public int getNativeInt() { throw new RuntimeException(); }
	public void setNativeInt(int integer) { throw new RuntimeException(); }
	public void setNativeInt(Integer integer) { throw new RuntimeException(); }

	public Integer getNativeInteger() { throw new RuntimeException(); }
	public void setNativeInteger(Integer int2) { throw new RuntimeException(); }
	public void setNativeInteger(int int2) { throw new RuntimeException(); }

	public int getNaturalGSInt() { didGetNaturalGSInt = true; return naturalGSInt; }
	public void setNaturalGSInt(int num) { this.naturalGSInt = num; didSetNaturalGSInt = true; } 
	public void setNaturalGSInt(Integer integer) { throw new RuntimeException(); }
	public void setNaturalGSInt(Long longable) { throw new RuntimeException(); }
	public void setNaturalGSInt(long longable) { throw new RuntimeException(); }
	public void setNaturalGSInt(short shortable) { throw new RuntimeException(); }
	public void setNaturalGSInt(Object obj) { throw new RuntimeException(); }
	
	public Integer getNaturalGSInteger() { didGetNaturalGSInteger = true; return naturalGSInteger; }
	public void setNaturalGSInteger(Integer num) { didSetNaturalGSInteger = true; naturalGSInteger = num; }
	public void setNaturalGSInteger(int num) { throw new RuntimeException(); } 
	public void setNaturalGSInteger(Long longable) { throw new RuntimeException(); }
	public void setNaturalGSInteger(long longable) { throw new RuntimeException(); }
	public void setNaturalGSInteger(short shortable) { throw new RuntimeException(); }
	public void setNaturalGSInteger(Object obj) { throw new RuntimeException(); }
	
	public boolean getNaturalBoolGS() { didGetNaturalBoolGS = true; return naturalBoolGS; }
	public void setNaturalBoolGS(boolean bool) { didSetNaturalBoolGS = true; naturalBoolGS = bool; }

	public boolean isNaturalBoolIsS() { didGetNaturalBoolIsS = true; return naturalBoolIsS; }
	public void setIsNaturalBoolIsS(boolean bool) { didSetNaturalBoolIsS = true; naturalBoolIsS = bool; }
	
	public void validate() {
		if (didGetNaturalGSString) throw new RuntimeException("getNaturalGSString() was called.");
		if (didGetUnnaturalGSString) throw new RuntimeException("getUnnaturalGSStringTest was called.");
		if (didGetNaturalGSInt) throw new RuntimeException("getNaturalGSInt was called.");
		if (didGetNaturalGSInteger) throw new RuntimeException("getNaturalGSInteger was called.");
		if (didGetNaturalBoolGS) throw new RuntimeException("getNaturalBoolGS was called.");
		if (didGetNaturalBoolIsS) throw new RuntimeException("isNaturalBoolIsS was called.");
		
		if (!didSetNaturalGSString) throw new RuntimeException("setNaturalGSString() was never called.");
		if (!didSetUnnaturalGSString) throw new RuntimeException("setUnnaturalGSStringTest was never called.");
		if (!didSetNaturalGSInt) throw new RuntimeException("setNaturalGSInt was never called.");
		if (!didSetNaturalGSInteger) throw new RuntimeException("setNaturalGSInteger was never called.");
		if (!didSetNaturalBoolGS) throw new RuntimeException("SetNaturalBoolGS was never called.");

		// BUG: CASTOR-1121:  Setter not being called when set to false.  Setters must always be called when they are defined.
		// if (!didSetNaturalBoolIsS) throw new RuntimeException("setIsNaturalBoolIsS was never called.");

		if (!nativeString.equals("nativeString")) throw new RuntimeException("nativeString not correctly set.");
		if (!naturalGSString.equals("naturalGSString")) throw new RuntimeException("naturalGSString not correctly set.");
		if (!unnaturalGSString.equals("unnaturalGSString")) throw new RuntimeException("unnaturalGSString not correctly set.");
		if (!readOnlyString.equals("readonly")) throw new RuntimeException("readOnlyString not correctly set.");
		if (nativeInt!=1) throw new RuntimeException("nativeInt not correctly set.");
		if (nativeInteger.intValue()!=2) throw new RuntimeException("nativeInteger not correctly set.");
		if (naturalGSInt!=3) throw new RuntimeException("naturalGSInt not correctly set.");
		if (naturalGSInteger.intValue()!=4) throw new RuntimeException("naturalGSInteger not correctly set.");
		if (naturalBoolGS!=true) throw new RuntimeException("naturalBoolGS not correctly set.");
		if (naturalBoolIsS!=false) throw new RuntimeException("naturalBoolIsS not correctly set.");
	}
	
}
