package org.exolab.castor.persist;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * <p>A count limted LRU hashtable for caching objects.
 * 
 * <p>
 * Every object being put in the hashtable will live for until the
 * hashtable is full, then an object which <em>one of the</em> 
 * least recent used object will be disposed. 
 *
 * <p>
 * If you are interested in the disposing object, extend it class
 * and override the method <code>dispose(Object o)</code>.
 * Otherwise, the object is silently discarded.
 *
 * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
 */
public class CountLimitedLRU implements LRU {

	private final static int LRU_OLD = 0;
	private final static int LRU_NEW = 1;

	private Hashtable mapKeyPos;
	private Object[] keys;
	private Object[] values;
	private int[] status;
	private int cur;
	private int size;

	public CountLimitedLRU( int size ) {
		keys = new Object[size];
		values = new Object[size];
		status = new int[size];
		mapKeyPos = new Hashtable(size);
		this.size = size;
	}

	/**
	 * ...work like Hashtable's <code>put</code>...except it's time limited
	 */
	public synchronized Object put( Object key, Object object ) {
		Object oldPos = mapKeyPos.get(key);
		if ( oldPos != null ) {
			int pos = ((Integer)oldPos).intValue();
			Object oldObject = values[pos];
			values[pos] = object;
			status[pos] = LRU_NEW;
			dispose( oldObject );
			return oldObject;
		} else {
			// skip to new pos -- for Cache, change walkStatus() to get lock....
			while (walkStatus() != LRU_OLD) {}

			Object intvalue;// = null;
			if ( keys[cur] != null ) {
				intvalue = mapKeyPos.remove(keys[cur]);
//				if ( intvalue == null )
//					intvalue = new Integer(cur);
			} else {
				intvalue = new Integer(cur);
			}
			Object oldObject = values[cur];
			keys[cur] = key;
			values[cur] = object;
			status[cur] = LRU_NEW;
			//System.out.println("mapKeyPos, key: "+key+" intvalue: "+intvalue);
			mapKeyPos.put(key, intvalue);
			cur++;
			if ( cur >= size ) cur = 0;
			if ( oldObject != null )
				dispose( oldObject );
			return oldObject;
		}
	}
	/**
	 * ...work like Hashtable's <code>get</code>...except it's count limited
	 */
	public synchronized Object get( Object key ) {
		Object intvalue = mapKeyPos.get(key);
		if ( intvalue != null ) {
			int pos = ((Integer)intvalue).intValue();
			status[pos] = LRU_NEW;
			//System.out.println("CountLimiteLRU: get("+key+") = "+values[pos]);
			return values[pos];
		}
		return null;
	}
	/**
	 * ...work like Hashtable's <code>remove</code>...except it's count limited
	 */
	public synchronized Object remove( Object key ) {
		Object intvalue = mapKeyPos.remove(key);
		if ( intvalue != null ) {
			int pos = ((Integer)intvalue).intValue();
			Object temp = values[pos];
			keys[pos] = null;
			values[pos] = null;
			status[pos] = LRU_OLD;
			//System.out.println("CountLimiteLRU: remove("+key+") = "+temp);
			return temp;
		}
		return null;
	}
	/*
	 * ...work like Hashtable's <code>elements</code>...except it's count limited
	 */
	public Enumeration elements() {
		return new ValuesEnumeration(values);
	}
	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 */
	protected void dispose( Object o ) {
		//System.out.println("diposed: "+o+" by CountLimitedLRU");
	}
	private int walkStatus() {
		int s = status[cur];
		if ( status[cur] == LRU_NEW ) {
			status[cur] = LRU_OLD;
			cur++;
			if ( cur >= size ) cur = 0;
			return LRU_NEW;
		} else {
			return LRU_OLD;
		}
	}
	private class ValuesEnumeration implements Enumeration {
		private int cur;
		private Object[] values;
		private ValuesEnumeration( Object[] v ) {
			Vector t = new Vector(v.length);
			for ( int i=0; i<v.length; i++ ) {
				if ( v[i] != null ) {
					t.add(v[i]);
				}
			}
			values = t.toArray();
		}
		public boolean hasMoreElements() {
			if ( values != null && values.length > cur ) 
				return true;
			return false;
		}
		public Object nextElement() throws NoSuchElementException {
			if ( values == null || values.length <= cur )
				throw new NoSuchElementException();
			return values[cur++];
		}
	}
	/*
	 * inner class for putting test cases only. Safe to delete.
	 */
	private static class Test {
		public static void main (String args[]) {
			class TestLRU extends CountLimitedLRU {
				public TestLRU(int count) {
					super(count);
				}
				protected void dispose(Object o) {
					//System.out.println("dispose: " + o );
				}
			}
			CountLimitedLRU cl = new TestLRU(3);
			Enumeration e = cl.elements();
			System.out.println( "<empty>" );
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "</empty>" );

			System.out.println( "<put(a,a)>" );
			cl.put("a","#a");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(a,a)>" );

			System.out.println( "<put(b,b)>" );
			cl.put("b","#b");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(b,b)>" );

			System.out.println( "<put(c,c)>" );
			cl.put("c","#c");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(c,c)>" );

			System.out.println( "<put(d,d)>" );
			cl.put("d","#d");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(d,d)>" );

			System.out.println( "<put(c,c1)>" );
			cl.put("c","#c1");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(c,c1)>" );

			System.out.println( "<put(c,c2)>" );
			cl.put("c","#c2");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(c,c2)>" );

			System.out.println( "<put(c,c3)>" );
			cl.put("c","#c3");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(c,c3)>" );

			System.out.println( "<put(b,b)>" );
			cl.put("b","#b");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(b,b)>" );

			System.out.println( "<put(e,e)>" );
			cl.put("e","#e");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(e,e)>" );

			System.out.println( "<put(f,f)>" );
			cl.put("f","#f");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(f,f)>" );

			System.out.println( "<remove(e,e)>" );
			System.out.println(cl.remove("e")+" is removed!");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</remove(e,e)>" );

			System.out.println( "<put(g,g)>" );
			cl.put("g","#g");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(g,g)>" );

			System.out.println( "<remove(f,f)>" );
			System.out.println(cl.remove("f")+" is removed!");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</remove(f,f)>" );

			System.out.println( "<remove(b,b)>" );
			System.out.println(cl.remove("b")+" is removed!");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</remove(b,b)>" );

			System.out.println( "<remove(g,g)>" );
			System.out.println(cl.remove("g")+" is removed!");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</remove(g,g)>" );

			System.out.println( "<remove(x,x)>" );
			System.out.println(cl.remove("x")+" is removed!");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</remove(x,x)>" );

			System.out.println( "<put(a,a)>" );
			cl.put("a","#a");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(a,a)>" );

			System.out.println( "<put(b,b)>" );
			cl.put("b","#b");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(b,b)>" );

			System.out.println( "<put(c,c)>" );
			cl.put("c","#c");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(c,c)>" );

			System.out.println( "<put(d,d)>" );
			cl.put("d","#d");
			e = cl.elements();
			while ( e.hasMoreElements() ) {
				System.out.print( e.nextElement() + "\t" );
			}
			System.out.println( "\n</put(d,d)>" );

		}
	}
}