package org.exolab.castor.xml;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * This class tests the functionality of PrimitiveObject.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class PrimitiveObjectTest extends TestCase {

    public void testPrimitiveBigDecimal() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(BigDecimal.class);
        object.setValue("-1.23E-12");
        BigDecimal decimal = (BigDecimal) object.getObject();

        Assert.assertEquals(decimal.unscaledValue(), new BigInteger("-123"));
        Assert.assertEquals(decimal.scale(), 14);

    }

    public void testPrimitiveBigDecimalNull() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(BigDecimal.class);
        object.setValue("");
        BigDecimal decimal = (BigDecimal) object.getObject();

        Assert.assertEquals(decimal.unscaledValue(), new BigInteger("0"));
        Assert.assertEquals(decimal.scale(), 0);

        object.setValue(null);
        decimal = (BigDecimal) object.getObject();

        Assert.assertEquals(decimal.unscaledValue(), new BigInteger("0"));
        Assert.assertEquals(decimal.scale(), 0);

    }

    public void testPrimitiveBigInteger() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(BigInteger.class);
        object.setValue("-123");
        BigInteger bigInt = (BigInteger) object.getObject();

        Assert.assertEquals(bigInt, new BigInteger("-123"));

    }

    public void testPrimitiveBigIntegerNull() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(BigInteger.class);
        object.setValue("");
        BigInteger bigInt = (BigInteger) object.getObject();

        Assert.assertEquals(bigInt, new BigInteger("0"));

        object.setValue(null);
        bigInt = (BigInteger) object.getObject();

        Assert.assertEquals(bigInt, new BigInteger("0"));

    }

    public void testPrimitiveBoolean() {
        PrimitiveObject object = new PrimitiveObject();
        object.setType(Boolean.class);

        object.setValue("1");
        Boolean bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, true);

        object.setValue("true");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, true);

        object.setValue("TRUE");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, true);

        object.setValue("0");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

        object.setValue("false");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

        object.setValue("FALSE");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

        object.setType(Boolean.TYPE);

        object.setValue("1");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, true);

        object.setValue("true");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, true);

        object.setValue("TRUE");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, true);

        object.setValue("0");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

        object.setValue("false");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

        object.setValue("FALSE");
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);
    }

    public void testPrimitiveBooleanNull() {
        PrimitiveObject object = new PrimitiveObject();
        object.setType(Boolean.class);

        object.setValue("");
        Boolean bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

        object.setValue(null);
        bool = (Boolean) object.getObject();
        Assert.assertEquals(bool, false);

    }

    public void testPrimitiveByte() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(Byte.class);
        object.setValue("8");
        Byte byt = (Byte) object.getObject();
        Assert.assertEquals(byt, new Byte("8"));

        object.setType(Byte.TYPE);
        byt = (Byte) object.getObject();
        Assert.assertEquals(byt, new Byte("8"));

    }

    public void testPrimitiveByteNull() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(Byte.class);
        object.setValue("");
        Byte byt = (Byte) object.getObject();
        Assert.assertEquals(byt, new Byte("0"));

        object.setValue(null);
        byt = (Byte) object.getObject();

        Assert.assertEquals(byt, new Byte("0"));

    }

    public void testPrimitiveChar() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(Character.class);
        object.setValue("I don't finish anythi");
        Character chars = (Character) object.getObject();
        Assert.assertEquals(chars, new Character('I'));

    }

    public void testPrimitiveCharNull() {

        PrimitiveObject object = new PrimitiveObject();

        object.setType(Character.class);
        object.setValue("");
        Character chars = (Character) object.getObject();
        Assert.assertEquals(chars, new Character('\0'));

        object.setValue(null);
        chars = (Character) object.getObject();
        Assert.assertEquals(chars, new Character('\0'));

    }

    public void testPrimitiveDefault() {
        PrimitiveObject object = new PrimitiveObject();

        String value = "I don't finish anythi";
        object.setType(UnknownError.class);
        object.setValue(value);
        String string = (String) object.getObject();
        Assert.assertEquals(string, value);

    }

    public void testPrimitiveDefaultNull() {
        PrimitiveObject object = new PrimitiveObject();

        String value = null;
        object.setValue(value);
        Object retObject = object.getObject();
        Assert.assertEquals(retObject, null);

    }

    public void testPrimitiveDouble() {
        PrimitiveObject object = new PrimitiveObject();

        object.setType(Double.class);
        object.setValue("4.2");
        Double value = (Double) object.getObject();
        Assert.assertEquals(value, new Double("4.2"));

        object.setType(Double.TYPE);
        value = (Double) object.getObject();
        Assert.assertEquals(value, new Double("4.2"));

    }

    public void testPrimitiveDoubleNull() {
        PrimitiveObject object = new PrimitiveObject();

        object.setType(Double.class);
        Double value = (Double) object.getObject();
        Assert.assertEquals(value, new Double("0.0"));

        object.setType(Double.TYPE);
        value = (Double) object.getObject();
        Assert.assertEquals(value, new Double("0.0"));

    }

    public void testPrimitiveEnum() {

        // TODO PE enum test
    }

    public void testPrimitiveFloat() {
        PrimitiveObject object = new PrimitiveObject();

        Class<?> type = Float.TYPE;
        object.setType(type);
        object.setValue("42");
        Float floatObject = (Float) object.getObject();
        Assert.assertEquals(floatObject, new Float("42"));

        object.setValue("  43 ");
        floatObject = (Float) object.getObject();
        Assert.assertEquals(floatObject, new Float("43"));

        type = Float.class;
        object.setType(type);
        object.setValue("44");
        floatObject = (Float) object.getObject();
        Assert.assertEquals(floatObject, new Float("44"));

        object.setValue("  45 ");
        floatObject = (Float) object.getObject();
        Assert.assertEquals(floatObject, new Float("45"));

    }

    public void testPrimitiveObject() {
        PrimitiveObject object = new PrimitiveObject();

        Class<?> type = Integer.TYPE;
        object.setType(type);
        object.setValue("   42 ");
        Integer integer = (Integer) object.getObject();
        Assert.assertTrue(integer == 42);

        object.setType(String.class);
        object.setValue("Hallo");
        String string = (String) object.getObject();
        Assert.assertEquals(string, "Hallo");

    }

    public void testPrimitiveInteger() {
        PrimitiveObject object = new PrimitiveObject();

        Class<?> type = Integer.TYPE;
        object.setType(type);
        object.setValue("42");
        Integer integer = (Integer) object.getObject();
        Assert.assertTrue(integer == 42);

        object.setValue("  43 ");
        integer = (Integer) object.getObject();
        Assert.assertTrue(integer == 43);

        type = Integer.class;
        object.setType(type);
        object.setValue("44");
        integer = (Integer) object.getObject();
        Assert.assertTrue(integer == 44);

        object.setValue("  45 ");
        integer = (Integer) object.getObject();
        Assert.assertTrue(integer == 45);

    }

    public void testPrimitiveLong() {
        PrimitiveObject object = new PrimitiveObject();

        Class<?> type = Long.TYPE;
        object.setType(type);
        object.setValue("42");
        Long longObject = (Long) object.getObject();
        Assert.assertEquals(longObject, new Long("42"));

        object.setValue("  43 ");
        longObject = (Long) object.getObject();
        Assert.assertEquals(longObject, new Long("43"));

        type = Long.class;
        object.setType(type);
        object.setValue("44");
        longObject = (Long) object.getObject();
        Assert.assertEquals(longObject, new Long("44"));

        object.setValue("  45 ");
        longObject = (Long) object.getObject();
        Assert.assertEquals(longObject, new Long("45"));

    }

    public void testPrimitiveShort() {
        PrimitiveObject object = new PrimitiveObject();

        Class<?> type = Short.TYPE;
        object.setType(type);
        object.setValue("42");
        Short shortObject = (Short) object.getObject();
        Assert.assertEquals(shortObject, new Short("42"));

        object.setValue("  43 ");
        shortObject = (Short) object.getObject();
        Assert.assertEquals(shortObject, new Short("43"));

        type = Short.class;
        object.setType(type);
        object.setValue("44");
        shortObject = (Short) object.getObject();
        Assert.assertEquals(shortObject, new Short("44"));

        object.setValue("  45 ");
        shortObject = (Short) object.getObject();
        Assert.assertEquals(shortObject, new Short("45"));

    }

    public void testPrimitiveString() {
        PrimitiveObject object = new PrimitiveObject();

        String value = "I don't finish anythi";
        object.setType(UnknownError.class);
        object.setValue(value);
        String string = (String) object.getObject();
        Assert.assertEquals(string, value);
    }

}
