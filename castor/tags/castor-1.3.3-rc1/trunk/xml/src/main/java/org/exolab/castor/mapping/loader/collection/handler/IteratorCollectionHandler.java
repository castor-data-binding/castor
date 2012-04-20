package org.exolab.castor.mapping.loader.collection.handler;

import java.util.Enumeration;
import java.util.Iterator;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.J2CollectionHandlers.IteratorEnumerator;

public final class IteratorCollectionHandler<T> implements CollectionHandler<T> {
   public Object add(Object collection, T object) {
      // -- do nothing, cannot add elements to an
      // enumeration
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration<T> elements(Object collection) {
      if (collection == null) {
         return new CollectionHandlers.EmptyEnumerator<T>();
      }
      return new IteratorEnumerator<T>((Iterator<T>) collection);
   }

   public int size(Object collection) {
      // -- Nothing we can do without iterating over the
      // iterator
      return 0;
   }

   public Object clear(Object collection) {
      return null;
   }

   public String toString() {
      return "Iterator";
   }
}