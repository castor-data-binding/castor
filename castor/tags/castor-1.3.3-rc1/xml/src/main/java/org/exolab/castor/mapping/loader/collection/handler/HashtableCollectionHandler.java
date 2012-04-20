package org.exolab.castor.mapping.loader.collection.handler;

import java.util.Enumeration;
import java.util.Hashtable;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.loader.CollectionHandlers;

public final class HashtableCollectionHandler<T> implements CollectionHandler<T> {
   
   @SuppressWarnings("unchecked")
   public Object add(Object collection, T object) {

      T key = object;
      T value = object;

      if (object instanceof org.exolab.castor.mapping.MapItem) {
         MapItem<T, T> mapItem = (MapItem<T, T>) object;
         key = mapItem.getKey();
         value = mapItem.getValue();
         if (value == null) {
            value = object;
         }
         if (key == null) {
            key = value;
         }
      }

      if (collection == null) {
         collection = new Hashtable<T, T>();
         ((Hashtable<T, T>) collection).put(key, value);
         return collection;
      }
      ((Hashtable<T, T>) collection).put(key, value);
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration<T> elements(Object collection) {
      if (collection == null)
         return new CollectionHandlers.EmptyEnumerator<T>();
      return ((Hashtable<T, T>) collection).elements();
   }

   @SuppressWarnings("unchecked")
   public int size(Object collection) {
      if (collection == null)
         return 0;
      return ((Hashtable<T, T>) collection).size();
   }

   @SuppressWarnings("unchecked")
   public Object clear(Object collection) {
      if (collection != null)
         ((Hashtable<T, T>) collection).clear();
      return null;
   }

   public String toString() {
      return "Hashtable";
   }
}