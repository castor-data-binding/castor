package org.exolab.castor.mapping.loader.collection.handler;

import java.util.Enumeration;
import java.util.Vector;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;

public final class VectorCollectionHandler<T> implements CollectionHandler<T> {
   
   @SuppressWarnings("unchecked")
   public Object add(Object collection, T object) {
      if (collection == null) {
         collection = new Vector<T>();
         ((Vector<T>) collection).addElement(object);
         return collection;
      }
      ((Vector<T>) collection).addElement(object);
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration<T> elements(Object collection) {
      if (collection == null)
         return new CollectionHandlers.EmptyEnumerator<T>();
      return ((Vector<T>) collection).elements();
   }

   @SuppressWarnings("unchecked")
   public int size(Object collection) {
      if (collection == null)
         return 0;
      return ((Vector<T>) collection).size();
   }

   @SuppressWarnings("unchecked")
   public Object clear(Object collection) {
      if (collection != null)
         ((Vector<T>) collection).removeAllElements();
      return null;
   }

   public String toString() {
      return "Vector";
   }
}