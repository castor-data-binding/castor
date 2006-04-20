/**
 * The root class, holding our links to NativeCollections and GSCollections.
 * NativeCollections tests native accessors, and throws exceptions when non-native
 * accessors are used.
 * GSCollections does the same, in reverse - if natives are used, it will throw
 * exceptions.
 * 
 * All classes have a minefield of other methods that shouldn't ever get called; if any
 * of them do, an immediate exception will be thrown.
 * 
 * @author gblock
 *
 */
public class Root {
	private NativeCollections nativeCollections;
	private GSCollections gsCollections;
	private GACollections gaCollections;
	
	public NativeCollections getNativeCollections() {
		return nativeCollections;
	}
	
	public void setNativeCollections(NativeCollections nc) {
		if (nc==null)throw new RuntimeException("Null sent to setNativeCollections()");
		this.nativeCollections = nc;
		nativeCollections.validate();
	}
	
	public GSCollections getGsCollections() {
		return gsCollections;
	}
	
	public void setGsCollections(GSCollections gc) {
		if (gc==null)throw new RuntimeException("Null sent to setGSCollections()");
		this.gsCollections = gc;
		gsCollections.validate();
	}
	
	public GACollections getGaCollections() {
		return gaCollections;
	}
	
	public void setGaCollections(GACollections ga) {
		if (ga==null)throw new RuntimeException("Null sent to setGACollections()");
		gaCollections = ga;
		gaCollections.validate();
	}
}
