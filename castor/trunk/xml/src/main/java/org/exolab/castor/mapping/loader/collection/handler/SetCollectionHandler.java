package org.exolab.castor.mapping.loader.collection.handler;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.J2CollectionHandlers.IteratorEnumerator;

public final class SetCollectionHandler<T> implements CollectionHandler<T> {
   @SuppressWarnings("unchecked")
   public Object add(Object collection, T object) {
      if (collection == null) {
         collection = new HashSet<T>();
         ((Set<T>) collection).add(object);
         return collection;
      }
      // if ( ! ( (Set) collection ).contains( object ) )
      ((Set<T>) collection).add(object);
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration<T> elements(Object collection) {
      if (collection == null)
         return new CollectionHandlers.EmptyEnumerator<T>();
      return new IteratorEnumerator<T>(((Set<T>) collection).iterator());
   }

   @SuppressWarnings("unchecked")
   public int size(Object collection) {
      if (collection == null)
         return 0;
      return ((Set<T>) collection).size();
   }

   @SuppressWarnings("unchecked")
   public Object clear(Object collection) {
      if (collection != null)
         ((Set<T>) collection).clear();
      return null;
   }

   public String toString() {
      return "Set";
   }

}