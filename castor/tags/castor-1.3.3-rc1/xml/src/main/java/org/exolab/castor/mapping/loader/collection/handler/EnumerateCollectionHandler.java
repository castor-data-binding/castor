package org.exolab.castor.mapping.loader.collection.handler;

import java.util.Enumeration;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;

public final class EnumerateCollectionHandler<T> implements CollectionHandler<T> {
   public Object add(Object collection, T object) {
      // -- do nothing, cannot add elements to an enumeration
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration<T> elements(Object collection) {
      if (collection == null)
         return new CollectionHandlers.EmptyEnumerator<T>();
      return ((Enumeration<T>) collection);
   }

   public int size(Object collection) {
      // -- Nothing we can do without destroying the enumeration
      return 0;
   }

   public Object clear(Object collection) {
      // -- Should we iterate over nextElement?
      return null;
   }

   public String toString() {
      return "Enumeration";
   }
}