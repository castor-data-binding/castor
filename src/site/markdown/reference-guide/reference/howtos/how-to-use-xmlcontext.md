# How to use Castor's XMLContext for un-/marshaling

## Intended Audience

Anyone who wants to use Castor XML for XML data binding, namely marshaling and 
unmarshaling operations.
        
## Prerequisites

You should have downloaded the Castor binaries or included Castor as a dependency in a 
Maven project.
    
## Domain classes
    
For the purpose of showcasing the use of the `XMLContext` class, let's assume we 
have a simple `Person` class as follows:
     
```
import java.util.Date;

public class Person implements java.io.Serializable {

   private String name = null;

   private Date dob = null;

   public Person() {
      super();
   }

   public Person(String name) {
      this.name  = name;
   }

   public Date getDateOfBirth() {
      return dob;
   }

   public String getName() {
      return name;
   }

   public void setDateOfBirth(Date dob) {
      this.dob = dob;
   }

   public void setName(String name) {
      this.name = name;
   }
}
```

## Basic code fragments

   Starting with Castor 1.1.2, the `org.exolab.castor.xml.XMLContext` 
   class provides a bootstrap mechanism for Castor XML, and allows easy (and efficient) 
   instantiation of `org.exolab.castor.xml.Marshaller` and 
   `org.exolab.castor.xml.Unmarshaller` 
   instances, which can be used to perform basic XML data binding operations.
		
   Below is a code sample that shows how to use the `XMLContext` class for 
   umarshaling a `Person` instance using an 
   `org.exolab.castor.xml.Unmarshaller`. In this example, 
   a mapping file is used.
		
```
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
		
Mapping mapping = <b>XMLContext.createMapping()</b>;
mapping.loadMapping("mapping.xml");

XMLContext context = new XMLContext();
context.addMapping(mapping);

Unmarshaller unmarshaller = <b>context.createUnmarshaller()</b>;
unmarshaller.setClass(Person.class);

Reader reader = new FileReader("test.xml");

Person person = (Person) unmarshaller.unmarshal(reader);
```

		   
   As shown above, the `org.exolab.castor.xml.XMLContext` 
   class offers various factory methods to obtain a new 
   `org.exolab.castor.xml.Marshaller`, 
   `org.exolab.castor.xml.Unmarshaller"` or 
   `org.exolab.castor.mapping.Mapping` instance.
		
   When you need more than one `org.exolab.castor.xml.Unmarshaller` 
   instance in your application, please call 
   `org.exolab.castor.xml.XMLContext#createUnmarshaller` as required. As all 
   `Unmarshaller` instances are created from the very same `XMLContext` 
   instance, overhead will be minimal. Please note, though, that an 
   `Unmarshaller` instance is not thread-safe.
