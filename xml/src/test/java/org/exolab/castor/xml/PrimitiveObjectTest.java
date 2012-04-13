package org.exolab.castor.xml;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.exolab.castor.xml.parsing.primitive.objects.PrimitiveObjectFactory;
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
        BigDecimal decimal = (BigDecimal) PrimitiveObjectFactory.getInstance().getObject(BigDecimal.class, "-1.23E-12");

        Assert.assertEquals(decimal.unscaledValue(), new BigInteger("-123"));
        Assert.assertEquals(decimal.scale(), 14);

    }

    public void testPrimitiveBigDecimalNull() {
        BigDecimal decimal = (BigDecimal) PrimitiveObjectFactory.getInstance().getObject(BigDecimal.class, null);
        Assert.assertEquals(decimal.unscaledValue(), new BigInteger("0"));
        Assert.assertEquals(decimal.scale(), 0);
    }

    public void testPrimitiveBigDecimalEmptyString() {
        BigDecimal decimal = (BigDecimal) PrimitiveObjectFactory.getInstance().getObject(BigDecimal.class, "");
        Assert.assertEquals(decimal.unscaledValue(), new BigInteger("0"));
        Assert.assertEquals(decimal.scale(), 0);
    }

    public void testPrimitiveBigInteger() {
        BigInteger bigInt = (BigInteger) PrimitiveObjectFactory.getInstance().getObject(BigInteger.class, "-123");
        Assert.assertEquals(bigInt, new BigInteger("-123"));
    }

    public void testPrimitiveBigIntegerNull() {
        BigInteger bigInt = (BigInteger) PrimitiveObjectFactory.getInstance().getObject(BigInteger.class, "");
        Assert.assertEquals(bigInt, new BigInteger("0"));

        bigInt = (BigInteger) PrimitiveObjectFactory.getInstance().getObject(BigInteger.class, null);
        Assert.assertEquals(bigInt, new BigInteger("0"));
    }

    public void testPrimitiveBoolean() {
        Boolean bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "1");
        Assert.assertEquals(bool, true);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "true");
        Assert.assertEquals(bool, true);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "TRUE");
        Assert.assertEquals(bool, true);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "0");
        Assert.assertEquals(bool, false);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "false");
        Assert.assertEquals(bool, false);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "FALSE");
        Assert.assertEquals(bool, false);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.TYPE, "1");
        Assert.assertEquals(bool, true);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.TYPE, "true");
        Assert.assertEquals(bool, true);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.TYPE, "TRUE");
        Assert.assertEquals(bool, true);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.TYPE, "0");
        Assert.assertEquals(bool, false);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.TYPE, "false");
        Assert.assertEquals(bool, false);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.TYPE, "FALSE");
        Assert.assertEquals(bool, false);
    }

    public void testPrimitiveBooleanNull() {
        Boolean bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, "");
        Assert.assertEquals(bool, false);

        bool = (Boolean) PrimitiveObjectFactory.getInstance().getObject(Boolean.class, null);
        Assert.assertEquals(bool, false);
    }

    public void testPrimitiveByte() {
        Byte byt = (Byte) PrimitiveObjectFactory.getInstance().getObject(Byte.class, "8");
        Assert.assertEquals(byt, new Byte("8"));

        byt = (Byte) PrimitiveObjectFactory.getInstance().getObject(Byte.TYPE, "8");
        Assert.assertEquals(byt, new Byte("8"));
    }

    public void testPrimitiveByteNull() {
        Byte byt = (Byte) PrimitiveObjectFactory.getInstance().getObject(Byte.class, "");
        Assert.assertEquals(byt, new Byte("0"));

        byt = (Byte) PrimitiveObjectFactory.getInstance().getObject(Byte.class, null);
        Assert.assertEquals(byt, new Byte("0"));
    }

    public void testPrimitiveChar() {
        Character chars = (Character) PrimitiveObjectFactory.getInstance().getObject(Character.class, "I don't finish anythi");
        Assert.assertEquals(chars, new Character('I'));
    }

    public void testPrimitiveCharNull() {
        Character chars = (Character) PrimitiveObjectFactory.getInstance().getObject(Character.class, "");
        Assert.assertEquals(chars, new Character('\0'));

        chars = (Character) PrimitiveObjectFactory.getInstance().getObject(Character.class, null);
        Assert.assertEquals(chars, new Character('\0'));
    }

    public void testPrimitiveDefault() {
        String value = "I don't finish anythi";
        String string = (String) PrimitiveObjectFactory.getInstance().getObject(UnknownError.class, value);
        Assert.assertEquals(string, value);
    }

    public void testPrimitiveDefaultNull() {
        Object retObject = PrimitiveObjectFactory.getInstance().getObject(UnknownError.class, null);
        Assert.assertEquals(retObject, null);
    }

    public void testPrimitiveDouble() {
        Double value = (Double) PrimitiveObjectFactory.getInstance().getObject(Double.class, "4.2");
        Assert.assertEquals(value, new Double("4.2"));

        value = (Double) PrimitiveObjectFactory.getInstance().getObject(Double.TYPE, "4.2");
        Assert.assertEquals(value, new Double("4.2"));
    }

    public void testPrimitiveDoubleNull() {
        Double value = (Double) PrimitiveObjectFactory.getInstance().getObject(Double.class, null);
        Assert.assertEquals(value, new Double("0.0"));

        value = (Double) PrimitiveObjectFactory.getInstance().getObject(Double.TYPE, null);
        Assert.assertEquals(value, new Double("0.0"));
    }

    public void testPrimitiveEnum() {
        // TODO PE enum test
    }

    public void testPrimitiveFloat() {
        Float floatObject = (Float) PrimitiveObjectFactory.getInstance().getObject(Float.TYPE, "42");
        Assert.assertEquals(floatObject, new Float("42"));

        floatObject = (Float) PrimitiveObjectFactory.getInstance().getObject(Float.TYPE, "  43  ");
        Assert.assertEquals(floatObject, new Float("43"));

        floatObject = (Float) PrimitiveObjectFactory.getInstance().getObject(Float.class, "44");
        Assert.assertEquals(floatObject, new Float("44"));

        floatObject = (Float) PrimitiveObjectFactory.getInstance().getObject(Float.class, "  45  ");
        Assert.assertEquals(floatObject, new Float("45"));
    }

    public void testPrimitiveObject() {
        Integer integer = (Integer) PrimitiveObjectFactory.getInstance().getObject(Integer.TYPE, "   42  ");
        Assert.assertTrue(integer == 42);

        String string = (String) PrimitiveObjectFactory.getInstance().getObject(String.class, "Hallo");
        Assert.assertEquals(string, "Hallo");
    }

    public void testPrimitiveInteger() {
        Integer integer = (Integer) PrimitiveObjectFactory.getInstance().getObject(Integer.TYPE, "42");
        Assert.assertTrue(integer == 42);

        integer = (Integer) PrimitiveObjectFactory.getInstance().getObject(Integer.TYPE, "  43  ");
        Assert.assertTrue(integer == 43);

        integer = (Integer) PrimitiveObjectFactory.getInstance().getObject(Integer.class, "44");
        Assert.assertTrue(integer == 44);

        integer = (Integer) PrimitiveObjectFactory.getInstance().getObject(Integer.class, "  45  ");
        Assert.assertTrue(integer == 45);
    }

    public void testPrimitiveLong() {
        Long longObject = (Long) PrimitiveObjectFactory.getInstance().getObject(Long.TYPE, "42");
        Assert.assertEquals(longObject, new Long("42"));

        longObject = (Long) PrimitiveObjectFactory.getInstance().getObject(Long.TYPE, "  43  ");
        Assert.assertEquals(longObject, new Long("43"));

        longObject = (Long) PrimitiveObjectFactory.getInstance().getObject(Long.class, "44");
        Assert.assertEquals(longObject, new Long("44"));

        longObject = (Long) PrimitiveObjectFactory.getInstance().getObject(Long.class, "  45  ");
        Assert.assertEquals(longObject, new Long("45"));
    }

    public void testPrimitiveShort() {
        Short shortObject = (Short) PrimitiveObjectFactory.getInstance().getObject(Short.TYPE, "42");
        Assert.assertEquals(shortObject, new Short("42"));

        shortObject = (Short) PrimitiveObjectFactory.getInstance().getObject(Short.TYPE, "  43  ");
        Assert.assertEquals(shortObject, new Short("43"));

        shortObject = (Short) PrimitiveObjectFactory.getInstance().getObject(Short.class, "44");
        Assert.assertEquals(shortObject, new Short("44"));

        shortObject = (Short) PrimitiveObjectFactory.getInstance().getObject(Short.class, "  45  ");
        Assert.assertEquals(shortObject, new Short("45"));
    }

    public void testPrimitiveString() {
        String value = "I don't finish anythi";
        String string = (String) PrimitiveObjectFactory.getInstance().getObject(UnknownError.class, value);
        Assert.assertEquals(string, value);
    }

}
