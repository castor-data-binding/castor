/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * $Id: LRU.java
 */
package org.exolab.castor.persist;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * <p>Base interface for all least-recently-used cache. 
 *
 * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
 */
public abstract class LRU {

    /**
     * Map type for no caching as the caching mechanism. All object
     * put into the map will be discarded.
     */
    public final static int CACHE_NONE = 1;
    
    /**
     * Map type of Count-Limited least-recently-used as caching mechanism.
     * Object Lock which is not hold by any transcation will be put in the cache, until
     * the cache is full and other object overwritten it.
     */
    public final static int CACHE_COUNT_LIMITED = 2;
    
    /**
     * Map type of Time-Limited least-recently-used is used as caching mechanism.
     * Object Lock which is not hold by any transcation will be put in the cache, until
     * timeout is reached.
     */
    public final static int CACHE_TIME_LIMITED = 3;
    
    /**
     * Map type of unlimited cache as caching mechanism.
     * Object Lock which is not hold by any transcation will be put in the cache 
     * for later use.
     */
    public final static int CACHE_UNLIMITED = 4;

    /** 
     * Specify the default LRU mechanism
     */
    public final static int DEFAULT_TYPE = CACHE_COUNT_LIMITED;

    /**
     * Specify the default LRU parameter
     */
    public final static int DEFAULT_PARAM = 30;

    /**
     * Maps the specified <code>key</code> to the specified 
     * <code>value</code> in this hashtable. Neither the key nor the 
     * value can be <code>null</code>. 
     * <p>
     * The value can be retrieved by calling the <code>get</code> method 
     * with a key that is equal to the original key, before it is diposed
     * by the least-recently-used map. 
     * <p>
     * @param      key     the hashtable key.
     * @param      value   the value.
     * @return     the previous value of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one.
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>.
     */
	public abstract Object put(Object key, Object value);

	/**
     *Returns the value to which the specified key is mapped in this hashtable.
     *@param key - a key in the hashtable.
     *@return the value to which the key is mapped in this hashtable; null if 
     * the key is not mapped to any value in this hashtable.
	 */
	public abstract Object get(Object key);

    /**
     * Removes the key (and its corresponding value) from this 
     * hashtable. This method does nothing if the key is not in the hashtable.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this hashtable,
     *          or <code>null</code> if the key did not have a mapping.
     */
	public abstract Object remove(Object key);

    /**
     * Returns an enumeration of the values in this LRU map.
     * Use the Enumeration methods on the returned object to fetch the elements
     * sequentially.
     *
     * @return  an enumeration of the values in this hashtable.
     * @see     java.util.Enumeration
     */
	public abstract Enumeration elements();

    /**
     * Factory method to create a LRU map of specified type.
     *
     * @param type   mechanism type
     * @param param   capacity of the lru
     */
	public static LRU create( int type, int param ) {
		LRU cache;

        if ( type == 0 ) {
            type = DEFAULT_TYPE;
            param = DEFAULT_PARAM;
        }

        switch ( type ) {
        case CACHE_COUNT_LIMITED :
            if ( param > 0 ) 
                cache = new LRU.CountLimited( param );
            else 
                cache = new LRU.NoCache();
            break;
        case CACHE_TIME_LIMITED :
            if ( param > 0 ) 
                cache = new LRU.TimeLimited( param );
            else 
                cache = new LRU.NoCache();
            break;
        case CACHE_UNLIMITED :
            cache = new LRU.Unlimited();
            break;
        case CACHE_NONE :
            cache = new LRU.NoCache();
            break;
        default :
            cache = new LRU.CountLimited( 100 );
        }
		return cache;
	}

    /**
     * Map the type in String into an int to represent 
     * the lru cache type.
     */
    public static int mapType( String type ) {
        if ( type == null || type.equals("") )
            return 0;

        if ( type.toLowerCase().equals("none") )
            return CACHE_NONE;

        if ( type.toLowerCase().equals("count-limited") )
            return CACHE_COUNT_LIMITED;

        if ( type.toLowerCase().equals("time-limited") )
            return CACHE_TIME_LIMITED;

        if ( type.toLowerCase().equals("unlimited") )
            return CACHE_UNLIMITED;

        return 0;
    }

	/**
	 * CountLimited is a count limted least-recently-used <tt>Map</tt>.
	 * <p>
	 * Every object being put in the Map will live until the
	 * map is full. If the map is full, a least-recently-used object 
     * will be disposed. 
	 * <p>
     * Method {@link dispose(Object)} will be called whenever an 
     * old object is diposed. Developer can get notify by overriding
     * the dispose method {@link dispose(Object)}.
     *
     * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
	 */
	public static class CountLimited extends LRU {

		private final static int LRU_OLD = 0;
		private final static int LRU_NEW = 1;

		private Hashtable mapKeyPos;
		private Object[] keys;
		private Object[] values;
		private int[] status;
		private int cur;
		private int size;

		public CountLimited( int size ) {
			keys = new Object[size];
			values = new Object[size];
			status = new int[size];
			mapKeyPos = new Hashtable(size);
			this.size = size;
		}

        /**
         * Maps the specified <code>key</code> to the specified 
         * <code>value</code> in this Map. Neither the key nor the 
         * value can be <code>null</code>. 
         * <p>
         * The value can be retrieved by calling the <code>get</code> method 
         * with a key that is equal to the original key, before it is diposed
         * when the Map is full. 
         * <p>
         * @param      key     the Map key.
         * @param      value   the value.
         * @return     the previous value of the specified key in this Map,
         *             or <code>null</code> if it did not have one.
         * @exception  NullPointerException  if the key or value is
         *               <code>null</code>.
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
				mapKeyPos.put(key, intvalue);
				cur++;
				if ( cur >= size ) cur = 0;
				if ( oldObject != null )
					dispose( oldObject );
				return oldObject;
			}
		}

    	/**
         *Returns the value to which the specified key is mapped in this Map.
         *@param key - a key in the Map.
         *@return the value to which the key is mapped in this Map; null if 
         * the key is not mapped to any value in this Map.
    	 */
		public synchronized Object get( Object key ) {
			Object intvalue = mapKeyPos.get(key);
			if ( intvalue != null ) {
				int pos = ((Integer)intvalue).intValue();
				status[pos] = LRU_NEW;
				return values[pos];
			}
			return null;
		}

        /**
         * Removes the key (and its corresponding value) from this 
         * Map. This method does nothing if the key is not in the Map.
         *
         * @param   key   the key that needs to be removed.
         * @return  the value to which the key had been mapped in this Map,
         *          or <code>null</code> if the key did not have a mapping.
         */
		public synchronized Object remove( Object key ) {
			Object intvalue = mapKeyPos.remove(key);
			if ( intvalue != null ) {
				int pos = ((Integer)intvalue).intValue();
				Object temp = values[pos];
				keys[pos] = null;
				values[pos] = null;
				status[pos] = LRU_OLD;
				return temp;
			}
			return null;
		}

        /**
         * Returns an enumeration of the values in this LRU map.
         * Use the Enumeration methods on the returned object to fetch the elements
         * sequentially.
         *
         * @return  an enumeration of the values in this Map.
         * @see     java.util.Enumeration
         */
		public Enumeration elements() {
			return new ValuesEnumeration(values);
		}

        /**
		 * This method is called when an object is disposed.
		 * Override this method if you interested in the disposed object.
         *
         * @param o - the disposed object
		 */
		protected void dispose( Object o ) {
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
		/**
		 * Test is an inner class for whitebox testing.
		 */
		private static class Test {
			public static void main (String args[]) {
				class TestLRU extends CountLimited {
					public TestLRU(int count) {
						super(count);
					}
					protected void dispose(Object o) {
					}
				}
				CountLimited cl = new TestLRU(3);
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


	/**
	 * NoCache is a Map which dispose all object right the way.
	 * <p>
	 * Every object being put in the Map will be disposed. 
	 * <p>
     * Method {@link dispose(Object)} will be called whenever an 
     * old object is diposed. Developer can get notify by overriding
     * the dispose method {@link dispose(Object)}.
     *
     * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
	 */
	public static class NoCache extends LRU {

        /**
         * Maps the specified <code>key</code> to the specified 
         * <code>value</code> in this Map. Neither the key nor the 
         * value can be <code>null</code>. End of theory.
         * <p>
         * Every object being put in the Map will be disposed.
         * <p>
         * @param      key     the Map key.
         * @param      value   the value.
         * @return     the previous value of the specified key in this Map,
         *             or <code>null</code> if it did not have one.
         * @exception  NullPointerException  if the key or value is
         *               <code>null</code>.
         */
		public synchronized Object put( Object key, Object object ) {
			dispose( object );
			return null;
		}

    	/**
         *Returns the value to which the specified key is mapped in this Map.
         *@param key - a key in the Map.
         *@return the value to which the key is mapped in this Map; null if 
         * the key is not mapped to any value in this Map.
    	 */
		public synchronized Object get( Object key ) {
			return null;
		}

        /**
         * Removes the key (and its corresponding value) from this 
         * Map. This method does nothing if the key is not in the Map.
         *
         * @param   key   the key that needs to be removed.
         * @return  the value to which the key had been mapped in this Map,
         *          or <code>null</code> if the key did not have a mapping.
         */
		public synchronized Object remove( Object key ) {
			return null;
		}

        /**
         * Returns an enumeration of the values in this LRU map.
         * Use the Enumeration methods on the returned object to fetch the elements
         * sequentially.
         *
         * @return  an enumeration of the values in this Map.
         * @see     java.util.Enumeration
         */
		public Enumeration elements() {
			return new EmptyEnumeration();
		}
		/**
		 * This method is called when an object is disposed.
		 * Override this method if you interested in the disposed object.
		 */
		protected void dispose( Object o ) {
			//System.out.println("dispose: "+o+" by NoCache.");
		}
		private class EmptyEnumeration implements Enumeration {

			private EmptyEnumeration() {
			}
			public boolean hasMoreElements() {
				return false;
			}
			public Object nextElement() throws NoSuchElementException {
				throw new NoSuchElementException();
			}
		}
	}

	/**
	 * TimeLimited is a time limted least-recently-used <tt>Map</tt>.
	 * <p>
	 * Every object being put in the Map will live until the timeout
	 * expired. 
	 * <p>
     * Method {@link dispose(Object)} will be called whenever an 
     * old object is diposed. Developer can get notify by overriding
     * the dispose method {@link dispose(Object)}.
     *
     * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
	 */
	public static class TimeLimited extends LRU {
		/**
		 *	The Default precision in millisecond is 1000. Percision is the interval 
		 *  between each time which the timer thread will wake up and trigger 
		 *  clean up of least-recently-used Object.
		 */
		public final static int DEFAULT_PRECISION = 1000;

		private static TimeThread ticker = new TimeThread( DEFAULT_PRECISION );

		private int interval;
		private int tailtime;
		private QueueItem head;
		private QueueItem tail;
		private Hashtable map;

		/**
		 * Constructor
		 *
		 * @param interval the number of ticks an object live before it is disposed.
	     * @param tick precision in msec.
		 */
		public TimeLimited( int interval ) {
			map = new Hashtable();
			this.interval = interval+1;
			ticker.addListener( this );
		}

        /**
         * Maps the specified <code>key</code> to the specified 
         * <code>value</code> in this Map. Neither the key nor the 
         * value can be <code>null</code>. 
         * <p>
         * The value can be retrieved by calling the <code>get</code> method 
         * with a key that is equal to the original key, before it is diposed
         * when the timeout of the entry is expired. 
         * <p>
         * @param      key     the Map key.
         * @param      value   the value.
         * @return     the previous value of the specified key in this Map,
         *             or <code>null</code> if it did not have one.
         * @exception  NullPointerException  if the key or value is
         *               <code>null</code>.
         */
		public synchronized Object put(Object key, Object value) {
			QueueItem oldItem = (QueueItem) map.get(key);
			if ( oldItem != null ) {
				Object oldObject = oldItem.item;
				oldItem.item = value;
				remove(oldItem);
				add(oldItem);
				return oldObject;
			} else {
				QueueItem newitem = new QueueItem(key,value);
				map.put(key,newitem);
				add(newitem);
				return null;
			}
		}

    	/**
         *Returns the value to which the specified key is mapped in this Map.
         *@param key - a key in the Map.
         *@return the value to which the key is mapped in this Map; null if 
         * the key is not mapped to any value in this Map.
    	 */
		public synchronized Object get(Object key) {
			Object o = map.get(key);
			if ( o == null )
				return null;
			else 
				return ((QueueItem)o).item;
		}

        /**
         * Removes the key (and its corresponding value) from this 
         * Map. This method does nothing if the key is not in the Map.
         *
         * @param   key   the key that needs to be removed.
         * @return  the value to which the key had been mapped in this Map,
         *          or <code>null</code> if the key did not have a mapping.
         */
		public synchronized Object remove(Object key) {

			Object o = map.remove(key);
			if ( o == null ) {
				//System.out.println("TimeLimiteLRU: not in cache ... remove("+key+")");
				return null;
			} else {
				//System.out.println("TimeLimiteLRU: remove("+key+") = "+((QueueItem)o).item);
				return ((QueueItem)o).item;
			}
		}

        /**
         * Returns an enumeration of the values in this LRU map.
         * Use the Enumeration methods on the returned object to fetch the elements
         * sequentially.
         *
         * @return  an enumeration of the values in this Map.
         * @see     java.util.Enumeration
         */
		public synchronized Enumeration elements() {
			return new ValuesEnumeration(map.elements());
		}

        /**
		 * This method is called when an object is disposed.
		 * Override this method if you interested in the disposed object.
         *
         * @param o - the disposed object
		 */
		protected void dispose( Object o ) {
		}

		private void remove(QueueItem item) {
			//System.out.println( "removing: <" + item + "> while...head=<"+head+"> tail=<"+tail+">" );
			QueueItem temp;
			if ( item == null ) 
				throw new NullPointerException();

			if ( item == head ) {
				temp = item;

				head = head.next;
				if ( head == null ) {
					tail = null;
				} else {
					head.prev = null;
					head.time += temp.time;
				}
				temp.prev = null;
				temp.next = null;
				temp.time = 0;
			} else if ( item == tail ) {
				tail = tail.prev;
				tailtime = 0;
			} else {
				temp = item;

				temp.prev.next = temp.next;
				temp.next.prev = temp.prev;
				temp.next.time += temp.time;

				temp.prev = null;
				temp.next = null;
				temp.time = 0;
			}
		}

		private void add(QueueItem item) {
			ticker.startTick();
			if ( head == null ) {
				head = tail = item;
				item.prev = null;
				item.next = null;
				item.time = interval;
				tailtime = interval;
			} else {
				tail.next = item;
				item.prev = tail;
				item.next = null;
				item.time = interval-tailtime;
				tailtime = interval;
				tail = item;
			}
		}

		/* 
		 * call by ticker daemon
		 */
		private synchronized void tick() {
			QueueItem temp;
			Object o;

			if ( head != null ) {
				head.time--;
				tailtime--;
			}
			while ( head != null && head.time <= 0 ) {
				
				temp = head;

				o = head.item;
				remove(temp);
				map.remove(temp.key);
				dispose(o);
			}
	//		if ( head == null ) {
	//			ticker.stopTick();
	//		}
		}

		private class ValuesEnumeration implements Enumeration {
			private Vector v = new Vector();
			private int cur;

			private ValuesEnumeration( Enumeration e ) {
				while ( e.hasMoreElements() ) {
					v.add(e.nextElement());
				}
				v.trimToSize();
			}
			public boolean hasMoreElements() {
				if ( v.size() > cur ) 
					return true;
				return false;
			}
			public Object nextElement() throws NoSuchElementException {
				if ( v.size() <= cur )
					throw new NoSuchElementException();
				Object o = v.get(cur++);
				if ( o != null )
					return ((QueueItem)o).item;
				else 
					return null;
			}
		}

		private class QueueItem {
			private QueueItem next;
			private QueueItem prev;
			private Object key;
			private Object item;
			private int time;

			private QueueItem( Object key, Object item ) {
				this.key = key;
				this.item = item;
			}
		}

		/*
		 * Ticker daemon. Generate tick in fixed interval of time.
		 */
		private static class TimeThread extends Thread {
			private int[] listenerLock = new int[0];
			private LinkList listener;
			private int[] lock = new int[0];
			private int tick;
			private long lastTime;
			private boolean isStopped;
			private boolean isStarted;

			public TimeThread(int tick) {
                super("Time-limited cache daemon");
				this.tick = tick;
				setDaemon(true);
				setPriority( MIN_PRIORITY );
				isStopped = true;
				start();
			}
			public void startTick() {
				//System.out.println( "start tick" );
				if ( isStarted && isStopped ) {
					synchronized(lock) {
						lastTime = System.currentTimeMillis();
						lock.notify();
					}
				}
			}
			public void stopTick() {
				//System.out.println( "stop tick" );
				isStopped = true;
			}
			public void run() {
				isStarted = true;
				try {
					while ( true ) {
						if ( isStopped ) {
							synchronized(lock) { lock.wait(); }
							isStopped = false;
						} else {							
							long time = System.currentTimeMillis();
							if ( time - lastTime < tick ) 
								sleep(tick-(time-lastTime));
							lastTime = time;
						}

						LinkList temp = listener;
						while ( temp != null ) {
							temp.t.tick();
							temp = temp.next;
						}
					}
				} catch ( InterruptedException e ) {
				}
			}
			public void addListener( TimeLimited t ) {
				synchronized ( listenerLock ) {
					listener = new LinkList( listener, t );
				}
			}
			private class LinkList {
				private LinkList next;
				private TimeLimited t;
				LinkList( LinkList next, TimeLimited t ) {
					this.next = next;
					this.t = t;
				}
			}
		}

		/**
		 * Test is an inner class for whitebox testing.
		 */
		private static class Test {
			public static void main (String args[]) throws Exception {
				class TestLRU extends TimeLimited {
					public TestLRU(int count) {
						super(count);
					}
					protected void dispose(Object o) {
						Enumeration e = this.elements();
						System.out.println("disposing: " + o );
						System.out.println( "list after disposed!" );
						while ( e.hasMoreElements() ) {
							System.out.print( e.nextElement() + "\t" );
						}
						System.out.println();
						super.dispose(o);
					}
				}
				TimeLimited cl = new TestLRU(5);
				Thread t = Thread.currentThread();

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
				t.sleep(1000);
				System.out.println( "\n</put(a,a)>" );


				System.out.println( "<put[a,b,c,d,e,f,g,h]>" );
				cl.put("a","#a"); System.out.println( "put a" );
				t.sleep(10);

				e = cl.elements();
				while ( e.hasMoreElements() ) {
					System.out.print( e.nextElement() + "\t" );
				}
				System.out.println();

				cl.put("b","#b"); System.out.println( "put b" );
				t.sleep(100);

				e = cl.elements();
				while ( e.hasMoreElements() ) {
					System.out.print( e.nextElement() + "\t" );
				}
				System.out.println();

				cl.put("c","#c"); System.out.println( "put c" );
				t.sleep(1000);

				e = cl.elements();
				while ( e.hasMoreElements() ) {
					System.out.print( e.nextElement() + "\t" );
				}
				System.out.println();

				cl.put("d","#d"); System.out.println( "put d" );
				t.sleep(500);

				e = cl.elements();
				while ( e.hasMoreElements() ) {
					System.out.print( e.nextElement() + "\t" );
				}
				System.out.println();

				cl.put("e","#e"); System.out.println( "put e" );
				t.sleep(1000);
				cl.put("f","#f"); System.out.println( "put f" );
				t.sleep(1000);
				cl.put("g","#g"); System.out.println( "put g" );
				t.sleep(1000);
				cl.put("h","#h"); System.out.println( "put h" );
				t.sleep(500);
				e = cl.elements();
				while ( e.hasMoreElements() ) {
					System.out.print( e.nextElement() + "\t" );
				}
				System.out.println();

				for (int i=0; i<10; i++ ) {
					t.sleep(1000);
					e = cl.elements();
					while ( e.hasMoreElements() ) {
						System.out.print( e.nextElement() + "\t" );
					}
					System.out.println();
				}

				System.out.println( "\n</put[a,b,c,d,e,f,g,h]>" );

			}
		}
	}

	/**
	 * UnLimited is Map which implements the {@link LRU} interface.
	 * <p>
	 * Every object being put in the Map will live until it is
     * removed manually.
	 * <p>
     * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
	 */
	public static class Unlimited extends LRU {
		private Hashtable map = new Hashtable();

		/**
         * Constructor
         *
		 */
		public Unlimited() {
		}

        /**
         * Maps the specified <code>key</code> to the specified 
         * <code>value</code> in this Map. Neither the key nor the 
         * value can be <code>null</code>. 
         * <p>
         * The value can be retrieved by calling the <code>get</code> method 
         * with a key that is equal to the original key.
         * <p>
         * @param      key     the Map key.
         * @param      value   the value.
         * @return     the previous value of the specified key in this Map,
         *             or <code>null</code> if it did not have one.
         * @exception  NullPointerException  if the key or value is
         *               <code>null</code>.
         */
		public Object put(Object key, Object value) {
			return map.put(key,value);
		}

    	/**
         *Returns the value to which the specified key is mapped in this Map.
         *@param key - a key in the Map.
         *@return the value to which the key is mapped in this Map; null if 
         * the key is not mapped to any value in this Map.
    	 */
		public Object get(Object key) {
			return map.get(key);
		}

        /**
         * Removes the key (and its corresponding value) from this 
         * Map. This method does nothing if the key is not in the Map.
         *
         * @param   key   the key that needs to be removed.
         * @return  the value to which the key had been mapped in this Map,
         *          or <code>null</code> if the key did not have a mapping.
         */
		public Object remove(Object key) {
			return map.remove(key);
		}

        /**
         * Returns an enumeration of the values in this LRU map.
         * Use the Enumeration methods on the returned object to fetch the elements
         * sequentially.
         *
         * @return  an enumeration of the values in this Map.
         * @see     java.util.Enumeration
         */
		public Enumeration elements() {
			return map.elements();
		}

        /**
		 * This method is called when an object is disposed.
		 * Override this method if you interested in the disposed object.
         *
         * @param o - the disposed object
		 */
		protected void dispose( Object o ) {
		}
	}
}
