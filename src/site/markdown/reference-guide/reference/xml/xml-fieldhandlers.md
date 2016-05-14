Castor XML: Writing Custom FieldHandlers
========================================

Introduction
------------

Sometimes we need to deal with a data format that Castor doesn't support
out-of-the-box, such as an unsupported Date/Time representation, or we
want to wrap and unwrap fields in Wrapper objects to get the desired XML
output without changing our object model. To handle these cases Castor
allows specifying a custom `org.exolab.castor.mapping.FieldHandler`
which can do these varying conversions during calls to the fields setter
and getter methods.

> **Note**
>
> The *FieldHandler* is the basic interface used by the Castor Framework
> when accessing field values or setting them. By specifying a custom
> *FieldHandler* in the mapping file we can basically intercept the
> calls to retrieve or set a field's value and do whatever conversions
> are necessary.

Writing a simple FieldHandler
-----------------------------

When a writing a FieldHandler handler we need to provide implementations
of the various methods specified in the FieldHandler interface. The main
two methods are the *getValue* and *setValue* methods which will
basically handle all our conversion code. The other methods provide ways
to create a new instance of the field's value or reset the field value.

> **Tip**
>
> It's actually even easier to write custom field handlers if we use a
> GeneralizedFieldHandler.

Let's take a look at how to convert a date in the format YYYY-MM-DD
using a custom FieldHandler. We want to marshal the following XML input
file `text.xml`:

``` {.xml}
<?xml version="1.0"?>
<root>2004-05-10</root>
```

The class we'll be marshalling from and unmarshalling to looks as
follows:

``` {.java}
import java.util.Date;

public class Root {

    private Date _date;

    public Root() {
        super();
    }

    public Date getDate() {
        return _date;
    }


    public void setDate(final Date date) {
        _date = date;
    }
         
```

So we need to write a custom FieldHandler that takes the input String
and converts it into the proper java.util.Date instance:

``` {.java}
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.ValidityException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The FieldHandler for the Date class
 *
 */
public class MyDateHandler implements FieldHandler
{

    private static final String FORMAT = "yyyy-MM-dd";

    /**
     * Creates a new MyDateHandler instance
     */
    public MyDateHandler() {
        super();
    }

    /**
     * Returns the value of the field from the object.
     *
     * @param object The object
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     */
    public Object getValue(final Object object) throws IllegalStateException {
        Root root = (Root)object;
        Date value = root.getDate();
        if (value == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        Date date = (Date)value;
        return formatter.format(date);
    }


    /**
     * Sets the value of the field on the object.
     *
     * @param object The object
     * @param value The new value
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     * @throws IllegalArgumentException The value passed is not of
     *  a supported type
     */
    public void setValue(Object object, Object value) 
       throws IllegalStateException, IllegalArgumentException {
       
        Root root = (Root)object;
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        Date date = null;
        try {
            date = formatter.parse((String)value);
        }
        catch(ParseException px) {
            throw new IllegalArgumentException(px.getMessage());
        }
        root.setDate(date);
    }


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance(Object parent) throws IllegalStateException {
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }


    /**
     * Sets the value of the field to a default value.
     *
     * Reference fields are set to null, primitive fields are set to
     * their default value, collection fields are emptied of all
     * elements.
     *
     * @param object The object
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     */
    public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
        ((Root)object).setDate(null);
    }
}
       
```

> **Tip**
>
> The *newInstance* method should return null for immutable types.

> **Note**
>
> There is also an `org.exolab.castor.mapping.AbstractFieldHandler` that
> we can extend instead of implementing FieldHandler directly. Not only
> do we not have to implement deprecated methods, but we can also gain
> access to the *FieldDescriptor* used by Castor.

In order to tell Castor that we want to use our Custom FieldHandler we
must specify it in the mapping file `mapping.xml`:

``` {.xml}

<?xml version="1.0"?>
<mapping>
  <class name="Root">
     <field name="date" type="string" handler="MyDateHandler">
        <bind-xml node="text"/>
     </field>
  </class>
</mapping>        
```

We can now use a simple Test class to unmarshal our XML document:

``` {.java}
import java.io.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.mapping.*;

public class Test {

    public static void main(String[] args) {
        try {

           //--load mapping
           Mapping mapping = new Mapping();
           mapping.loadMapping("mapping.xml");

           System.out.println("unmarshalling root instance:");
           System.out.println();

           Reader reader = new FileReader("test.xml");
           Unmarshaller unmarshaller = new Unmarshaller(Root.class);
           unmarshaller.setMapping(mapping);
           Root root = (Root) unmarshaller.unmarshal(reader);
           reader.close();

           System.out.println("Root#getDate : " + root.getDate());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

         
```

Now simply compile the code and run!

``` {.java}
% java Test
unmarshalling root instance:

Root#getDate : Mon May 10 00:00:00 CDT 2004
         
```

After running our test program we can see that Castor invoked our custom
FieldHandler and we got our properly formatted date in our Root.class.

Writing a GeneralizedFieldHandler
---------------------------------

A `org.exolab.castor.mapping.GeneralizedFieldHandler` is an extension of
FieldHandler interface where we simply write the conversion methods and
Castor will automatically handle the underlying get/set operations. This
allows us to re-use the same FieldHandler for fields from different
classes that require the same conversion.

> **Note:** Currently the GeneralizedFieldHandler cannot be used from a
> *binding-file* for use with the SourceGenerator, an enhancement patch
> will be checked into SVN for this feature, shortly after 0.9.6 final
> is released.

The same FieldHandler we used above can be written as a
GeneralizedFieldHandler as such:

``` {.java}

import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The FieldHandler for the Date class
 *
 */
public class MyDateHandler extends GeneralizedFieldHandler {

    private static final String FORMAT = "yyyy-MM-dd";

    /**
     * Creates a new MyDateHandler instance
     */
    public MyDateHandler() {
        super();
    }

    /**
     * This method is used to convert the value when the
     * getValue method is called. The getValue method will
     * obtain the actual field value from given 'parent' object.
     * This convert method is then invoked with the field's
     * value. The value returned from this method will be
     * the actual value returned by getValue method.
     *
     * @param value the object value to convert after
     *  performing a get operation
     * @return the converted value.
     */
    public Object convertUponGet(Object value) {
        if (value == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        Date date = (Date)value;
        return formatter.format(date);
    }


    /**
     * This method is used to convert the value when the
     * setValue method is called. The setValue method will
     * call this method to obtain the converted value.
     * The converted value will then be used as the value to
     * set for the field.
     *
     * @param value the object value to convert before
     *  performing a set operation
     * @return the converted value.
     */
    public Object convertUponSet(Object value) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
        Date date = null;
        try {
            date = formatter.parse((String)value);
        }
        catch(ParseException px) {
            throw new IllegalArgumentException(px.getMessage());
        }
        return date;
    }

    /**
     * Returns the class type for the field that this
     * GeneralizedFieldHandler converts to and from. This
     * should be the type that is used in the
     * object model.
     *
     * @return the class type of of the field
     */
    public Class getFieldType() {
        return Date.class;
    }

    /**
     * Creates a new instance of the object described by
     * this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple
     *  type and cannot be instantiated
     */
    public Object newInstance(Object parent) throws IllegalStateException
    {
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

}     
```

Everything else is the same. So we can re-run our test case using this
GeneralizedFieldHandler and we'll get the same result. The main
difference is that we implement the *convertUponGet* and
*convertUponSet* methods.

Notice that we never reference the *Root* class in our
`GeneralizedFieldHandler`. This allows us to use the same exact
`FieldHandler` for any field that requires this type of conversion.

Use ConfigurableFieldHandler for more flexibility
-------------------------------------------------

In some situations, the `GeneralizedFieldHandler` might not provide
sufficient flexibility. Suppose your XML document uses more than one
date format. You could solve this by creating a
`GeneralizedFieldHandler` subclass per date format, but that would lead
to code duplication, which in itself is not desirable.

A `ConfigurableFieldHandler` is a `FieldHandler` that can be configured
in the mapping file with any kind and any number of parameters. You can
simply configure two (or more) instances of the same
`ConfigurableFieldHandler` class with different date format patterns.
Here's a mapping file that uses a `ConfigurableFieldHandler` to marshal
and unmarshal the date field, similar to the preceding examples:

``` {.java}
<?xml version="1.0"?>
<mapping>
   
   <field-handler name="myHandler" class="FieldHandlerImpl">
      <param name="date-format" value="yyyyMMddHHmmss"/>
   </field-handler>
   
   <class name="Root">
      <field name="date" type="string" handler="myHandler"/>
   </class>

</mapping>            
```

The *field-handler* element defines the `ConfigurableFieldHandler`. The
class must be an implementation of the
*org.exolab.castor.mapping.ConfigurableFieldHandler* interface. This
instance is configured with a date format. However, each implementation
can decide which, and how many parameters to use.

The field handler instance is referenced by the *field* element, using
the *handler* attribute.

Here's the ConfigurableFieldHandler implementation:

``` {.java}
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.ValidityException;

public class FieldHandlerImpl implements FieldHandler, ConfigurableFieldHandler {

    private DateFormat formatter;

    public void setConfiguration(final Properties config) throws ValidityException {
        String pattern = config.getProperty("date-format");
        if (pattern == null) {
            throw new ValidityException("Required parameter \"date-format\" is missing for FieldHandlerImpl.");
        }
        try {
            formatter = new SimpleDateFormat(pattern);
        } catch (IllegalArgumentException e) {
            throw new ValidityException("Pattern \""+pattern+"\" is not a valid date format.");
        }
    }

    /**
     * Returns the value of the field from the object.
     *
     * @param object The object
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     */
    public Object getValue(Object object) throws IllegalStateException {
        Root root = (Root)object;
        Date value = root.getDate();
        if (value == null) return null;
        return formatter.format(value);
    }

    /**
     * Sets the value of the field on the object.
     *
     * @param object The object
     * @param value The new value
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     * @throws IllegalArgumentException The value passed is not of
     *  a supported type
     */
    public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        Root root = (Root)object;
        Date date = null;
        try {
            date = formatter.parse((String)value);
        }
        catch(ParseException px) {
            throw new IllegalArgumentException(px.getMessage());
        }
        root.setDate(date);
    }
    
    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance(Object parent)
        throws IllegalStateException
    {
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

    /**
     * Sets the value of the field to a default value.
     *
     * Reference fields are set to null, primitive fields are set to
     * their default value, collection fields are emptied of all
     * elements.
     *
     * @param object The object
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     */
    public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        ((Root)object).setDate(null);
    }

}        
```

This implementation is similar to the first *MyDateHandler* example on
this page, except that is adds a *setConfiguration* method as specified
by the *ConfigurableFieldHandler* interface. All parameters that are
configured in the mapping file will be passed in as a *Properties*
object. The implementing method is responsible for processing the
configuration data.

As a convenience, *org.exolab.castor.mapping.AbstractFieldHandler*
already implements *ConfigurableFieldHandler*. However, the
*setConfiguration* method is not doing anything. Any subclass of
*AbstractFieldHandler* only has to override this method to leverage the
configuration capabilities. Since *AbstractFieldHandler* and its
subclass *GeneralizedFieldHandler* are useful abstract classes, you'd
probably want to use them anyway. It eliminates the need to implement
the *ConfigurableFieldHandler* interface yourself.

Reuse a `ConfigurableFieldHandler` for more than one field definition
---------------------------------------------------------------------

Imagine a scenario where you want to use above
`ConfigurableFieldHandler` instance for more than one field - a valid
use case as it promotes reuse.

``` {.xml}
<?xml version="1.0"?>
<mapping>
   
   <field-handler name="myFirstHandler" class="FieldHandlerImpl">
      <param name="date-format" value="yyyyMMddHHmmss"/>
   </field-handler>

   <field-handler name="mySecondHandler" class="FieldHandlerImpl">
      <param name="date-format" value="yyyy-MM-ddHH:mm:ss"/>
   </field-handler>
   
   <class name="Root">
      <field name="firstDate" type="string" handler="myFirstHandler"/>
      <field name="secondDate" type="string" handler="myFirstHandler"/>
      <field name="thirdDate" type="string" handler="mySecondHandler"/>
   </class>

</mapping>     
```

For this to work, there's one more thing you will have to do: your
`ConfigurableFieldHandler` implementation has to implement the
`ClonableFieldHandlerMarker` interface and implement the
copyFieldHandler() method. As indicated by the name, please return a
clone/copy of your `FieldHandler` instance ... and you are all set.

A simplified sample implementation could look as follows, extending the
`FieldHandlerImpl` class from the previous section:

``` {.java}
public class FieldHandlerImpl implements FieldHandler, ConfigurableFieldHandler, ClonableFieldHandlerMarker {

    private DateFormat format;

    ...

    public void setFormat(DateFormat format) {
        this.format = format;
    }

    @Override
    public FieldHandler copyFieldHandler() {
        FieldHandlerImpl handler = new FieldHandlerImpl();
        handler.setFormat(this.getFormat());
        return handler;
    }
    
}       
```

No Constructor, No Problem!
---------------------------

A number of classes such as type-safe enum style classes have no
constructor, but instead have some sort of static factory method used
for converting a string value into an instance of the class. With a
custom FieldHandler we can allow Castor to work nicely with these types
of classes.

> **Tip**
>
> Castor XML automatically supports these types of classes if they have
> a specific method:
>
> ``` {.java}
> public static {Type} valueOf(String)
>           
> ```

> **Note**
>
> We're working on the same support for Castor JDO

Even though Castor XML supports the "valueOf" method type-safe enum
style classes, we'll show you how to write a custom handler for these
classes anyway since it's useful for any type of class regardless of the
name of the factory method.

Let's look at how to write a handler for the following type-safe enum
style class, which was actually generated by Castor XML (javadoc removed
for brevity):

``` {.java}
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class Color implements java.io.Serializable {

    public static final int RED_TYPE = 0;

    public static final Color RED = new Color(RED_TYPE, "red");

    public static final int GREEN_TYPE = 1;

    public static final Color GREEN = new Color(GREEN_TYPE, "green");

    public static final int BLUE_TYPE = 2;

    public static final Color BLUE = new Color(BLUE_TYPE, "blue");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


    private Color(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- test.types.Color(int, java.lang.String)


    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate()

    public int getType()
    {
        return this.type;
    } //-- int getType()

    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("red", RED);
        members.put("green", GREEN);
        members.put("blue", BLUE);
        return members;
    } //-- java.util.Hashtable init()

    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString()

    public static Color valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Color";
            throw new IllegalArgumentException(err);
        }
        return (Color) obj;
    } //-- test.types.Color valueOf(java.lang.String)

}      
```

The *GeneralizedFieldHandler* for the above *Color* class is as follows
(javadoc removed for brevity):

``` {.java}
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;

/**
 * The FieldHandler for the Color class
**/
public class ColorHandler
    extends GeneralizedFieldHandler
{

    public ColorHandler() {
        super();
    }

    public Object convertUponGet(Object value) {
        if (value == null) return null;
        Color color = (Color)value;
        return color.toString();
    }


    public Object convertUponSet(Object value) {
        return Color.valueOf((String)value);
    }

    public Class getFieldType() {
        return Color.class;
    }

    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

}     
```

That's all there really is to it. Now we just need to hook this up to
our mapping file and run a sample test.

If we have a root class *Foo* as such:

``` {.java}
public class Foo {

    private Color _color = null;
    private int _size = 0;
    private String _name = null;

    public Foo() {
        super();
    }

    public Color getColor() {
        return _color;
    }

    public String getName() {
        return _name;
    }

    public int getSize() {
        return _size;
    }

    public void setColor(Color color) {
        _color = color;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setSize(int size) {
        _size = size;
    }

}      
```

Our mapping file would be the following:

``` {.xml}
<?xml version="1.0"?>
<mapping>
  <class name="Foo">
     <field name="size" type="integer">
        <bind-xml node="element"/>
     </field>
     <field name="name" type="string"/>
     <field name="color" type="string" handler="ColorHandler"/>
  </class>
</mapping>      
```

We can now use our custom FieldHandler to unmarshal the following xml
input:

``` {.xml}
<?xml version="1.0"?>
<foo>
   <name>MyFoo</name>
   <size>345</size>
   <color>blue</color>
</foo>     
```

A sample test class is as follows:

``` {.java}

import java.io.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.mapping.*;

public class Test {

    public static void main(String[] args) {
        try {

            //--load mapping
            Mapping mapping = new Mapping();
            mapping.loadMapping("mapping.xml");

            System.out.println("unmarshalling Foo:");
            System.out.println();

            Reader reader = new FileReader("test.xml");
            Unmarshaller unmarshaller = new Unmarshaller(Foo.class);
            unmarshaller.setMapping(mapping);
            Foo foo = (Foo) unmarshaller.unmarshal(reader);
            reader.close();

            System.out.println("Foo#size : " + foo.getSize());
            System.out.print("Foo#color: ");
            if (foo.getColor() == null) {
                System.out.println("null");
            }
            else {
                System.out.println(foo.getColor().toString());
            }

            PrintWriter pw = new PrintWriter(System.out);
            Marshaller marshaller = new Marshaller(pw);
            marshaller.setMapping(mapping);
            marshaller.marshal(foo);
            pw.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}        
```

Collections and FieldHandlers
-----------------------------

> **Note**: With Castor 0.9.6 and later, the *GeneralizedFieldHandler*
> automatically supports iterating over the items of a collection and
> passing them one-by-one to the *convertUponGet*.
>
> For backward compatibility or to handle the collection iteration
> yourself, simply add the following to the constructor of your
> `GeneralizedFieldHandler` implementation:
>
> ``` {.java}
> setCollectionIteration(false);           
> ```

If you're going to be using custom field handlers for collection fields
with a `GeneralizedFieldHandler` using versions of Castor prior to
0.9.6, then you'll need to handle the collection iteration yourself in
the *convertUponGet* method.

If you're not using a `GeneralizedFieldHandler`, then you'll need to
handle the collection iteration yourself in the
*FieldHandler\#getValue()* method.

> **Tip**: Since Castor incrementally adds items to collection fields, there
> usually is no need to handle collections directly in the
> *convertUponSet* method (or the *setValue()* for those not using
> `GeneralizedFieldHandler`).
