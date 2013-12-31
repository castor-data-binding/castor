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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.mapping.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.exolab.castor.mapping.loader.collection.handler.ArrayListCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.CollectionCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.IteratorCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.ListCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.MapCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.QueueCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.SetCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.SortedMapCollectionHandler;
import org.exolab.castor.mapping.loader.collection.handler.SortedSetCollectionHandler;

/**
 * Implementation of various collection handlers for the Java 1.2 libraries.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-26 13:08:15 -0600 (Wed, 26 Apr
 *          2006) $
 */
public final class J2CollectionHandlers {

   /**
    * List of all the default collection handlers.
    */
   private static CollectionHandlers.Info[] collectionHandlers;

   static {
      collectionHandlers = new CollectionHandlers.Info[] {
            new CollectionHandlers.Info("list", List.class, false, new ListCollectionHandler()),
            new CollectionHandlers.Info("arraylist", ArrayList.class, false, new ArrayListCollectionHandler()),
            // For Collection/ArrayList (1.2)
            new CollectionHandlers.Info("collection", Collection.class, false, new CollectionCollectionHandler()),
            // For Queue (1.2)
            new CollectionHandlers.Info("priorityqueue", PriorityQueue.class, false, new QueueCollectionHandler()),
            // For Set/HashSet (1.2)
            new CollectionHandlers.Info("set", Set.class, false, new SetCollectionHandler()),
            // For Map/HashMap (1.2)
            new CollectionHandlers.Info("map", Map.class, false, new MapCollectionHandler()),
            // For SortedSet (1.2 aka 1.4)
            new CollectionHandlers.Info("sortedset", SortedSet.class, false, new SortedSetCollectionHandler()),
            // For SortedMap (1.2 aka 1.4)
            new CollectionHandlers.Info("sortedmap", SortedMap.class, false, new SortedMapCollectionHandler()),
            // For java.util.Iterator
            new CollectionHandlers.Info("iterator", Iterator.class, false, new IteratorCollectionHandler())
      };
   }

   public static CollectionHandlers.Info[] getCollectionHandlersInfo() {
      return collectionHandlers;
   }

   /**
    * Enumerator for an iterator.
    */
   public static final class IteratorEnumerator<T> implements Enumeration<T> {

      private final Iterator<T> _iterator;

      public IteratorEnumerator(Iterator<T> iterator) {
         _iterator = iterator;
      }

      public boolean hasMoreElements() {
         return _iterator.hasNext();
      }

      public T nextElement() {
         return _iterator.next();
      }

   }

}
