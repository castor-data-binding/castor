package org.exolab.castor.mapping.loader.collection.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.J2CollectionHandlers.IteratorEnumerator;

public final class ListCollectionHandler<T> implements CollectionHandler<T> {

   @SuppressWarnings("unchecked")
   public Object add(Object collection, final T object) {
         if (collection == null) {
            collection = new ArrayList<T>();
            ((Collection<T>) collection).add(object);
            return collection;
         }
         ((Collection<T>) collection).add(object);
         return null;
      }

      @SuppressWarnings("unchecked")
      public Enumeration<T> elements(final Object collection) {
         if (collection == null) {
            return new CollectionHandlers.EmptyEnumerator<T>();
         }
         return new IteratorEnumerator<T>(((Collection<T>) collection).iterator());
      }

      @SuppressWarnings("unchecked")
      public int size(final Object collection) {
         if (collection == null) {
            return 0;
         }
         return ((Collection<T>) collection).size();
      }

      @SuppressWarnings("unchecked")
      public Object clear(final Object collection) {
         if (collection != null) {
            ((Collection<T>) collection).clear();
         }
         return null;
      }

      public String toString() {
         return "List";
      }
}