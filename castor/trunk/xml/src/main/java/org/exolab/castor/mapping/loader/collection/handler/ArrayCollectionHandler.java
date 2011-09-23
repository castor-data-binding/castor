package org.exolab.castor.mapping.loader.collection.handler;

import java.lang.reflect.Array;
import java.util.Enumeration;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.J1CollectionHandlers.ArrayEnumerator;

public final class ArrayCollectionHandler<T> implements CollectionHandler<T> {
   public Object add(Object collection, T object) {
      if (collection == null) {

         // If the collection is of primitive type, the instantiation
         // (if it was null) is handled in the FieldHandlerImpl. We
         // can rely here that we deal only with array of object.
         Object newArray = Array.newInstance(object.getClass(), 1);
         Array.set(newArray, 0, object);
         return newArray;
      }

      Class<?> type = collection.getClass();
      if (!type.isArray()) {
         String err = "J1CollectionHandlers.array#add: type "
               + "mismatch, expecting an array, instead received: ";
         err += type.getName();
         throw new IllegalArgumentException(err);
      }

      type = type.getComponentType();

      Object newArray = Array.newInstance(type, Array.getLength(collection) + 1);

      for (int i = 0; i < Array.getLength(collection); ++i)
         Array.set(newArray, i, Array.get(collection, i));

      Array.set(newArray, Array.getLength(collection), object);

      return newArray;

   }

   public Enumeration<T> elements(Object collection) {
      if (collection == null)
         return new CollectionHandlers.EmptyEnumerator<T>();
      return new ArrayEnumerator<T>(collection);
   }

   public int size(Object collection) {
      if (collection == null)
         return 0;
      return Array.getLength(collection);
   }

   public Object clear(Object collection) {
      if (collection == null) {
         return null;
      }
      Class<?> type = collection.getClass();
      if (!type.isArray()) {
         String err = "J1CollectionHandlers.array#add: type "
               + "mismatch, expecting an array, instead received: ";
         err += type.getName();
         throw new IllegalArgumentException(err);
      }
      type = type.getComponentType();
      return Array.newInstance(type, 0);
   }

   public String toString() {
      return "Object[]";
   }
}