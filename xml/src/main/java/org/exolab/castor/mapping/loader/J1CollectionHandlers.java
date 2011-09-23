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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.mapping.loader;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.ArrayCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.EnumerateCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.HashtableCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.VectorCollectionHandler;

import java.lang.reflect.*;

/**
 * Implementation of various collection handlers for the Java 1.1 libraries.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr
 *          2006) $
 * @see CollectionHandler
 */
public final class J1CollectionHandlers {

   /**
    * List of all the default collection handlers.
    */
   private static CollectionHandlers.Info[] _colHandlers;
   
   static {
      _colHandlers = new CollectionHandlers.Info[] {
         // For array (any)
         new CollectionHandlers.Info("array", Object[].class, true, new ArrayCollectionHandler()),
         // For Vector (1.1)
         new CollectionHandlers.Info("vector", Vector.class, false, new VectorCollectionHandler()),
         // For Hashtable (1.1)
         new CollectionHandlers.Info("hashtable", Hashtable.class, false, new HashtableCollectionHandler()),
         // For Enumeration (1.1)
         new CollectionHandlers.Info("enumerate", Enumeration.class, false, new EnumerateCollectionHandler())
      };
   }

   public static CollectionHandlers.Info[] getCollectionHandlersInfo() {
      return _colHandlers;
   }
   
   /**
    * Enumerator for an array.
    */
   public static final class ArrayEnumerator<T> implements Enumeration<T> {

      private final Object _array;

      private int _index;

      public ArrayEnumerator(Object array) {
         _array = array;
      }

      public boolean hasMoreElements() {
         return (_index < Array.getLength(_array));
      }

      @SuppressWarnings("unchecked")
      public T nextElement() {
         if (_index >= Array.getLength(_array))
            throw new NoSuchElementException();
         return (T) Array.get(_array, _index++);
      }

   }

}
