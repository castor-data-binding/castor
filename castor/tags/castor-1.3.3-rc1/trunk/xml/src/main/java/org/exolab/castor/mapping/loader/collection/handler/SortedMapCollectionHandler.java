package org.exolab.castor.mapping.loader.collection.handler;

import java.util.Enumeration;
import java.util.SortedMap;
import java.util.TreeMap;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.J2CollectionHandlers.IteratorEnumerator;

public final class SortedMapCollectionHandler<T> implements CollectionHandler<T> {

   @SuppressWarnings("unchecked")
   public Object add(Object collection, T object) {

      T key = object;
      T value = object;

      if (object instanceof MapItem) {
         MapItem<T, T> item = (MapItem<T, T>) object;
         key = item.getKey();
         value = item.getValue();
         if (value == null) {
            value = object;
         }
         if (key == null) {
            key = value;
         }
      }

      if (collection == null) {
         collection = new TreeMap<T, T>();
         ((SortedMap<T, T>) collection).put(key, value);
         return collection;
      }
      ((SortedMap<T, T>) collection).put(key, value);
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration<T> elements(final Object collection) {
      if (collection == null)
         return new CollectionHandlers.EmptyEnumerator<T>();
      return new IteratorEnumerator<T>(((SortedMap<T, T>) collection).values().iterator());
   }

   @SuppressWarnings("unchecked")
   public int size(final Object collection) {
      if (collection == null)
         return 0;
      return ((SortedMap<T, T>) collection).size();
   }

   @SuppressWarnings("unchecked")
   public Object clear(final Object collection) {
      if (collection != null)
         ((SortedMap<T, T>) collection).clear();
      return null;
   }

   public String toString() {
      return "SortedMap";
   }
}