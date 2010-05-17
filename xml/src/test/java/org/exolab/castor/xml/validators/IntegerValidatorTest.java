package org.exolab.castor.xml.validators;

import org.castor.xml.BackwardCompatibilityContext;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;
import org.junit.Before;
import org.junit.Test;

public class IntegerValidatorTest {
    
    private ValidationContext context = null;

    @Before
    public void createValidationContext() {
        context = new ValidationContext();
        context.setInternalContext(new BackwardCompatibilityContext());
    }
    
    @Test(expected=ValidationException.class)
    public void testFixedValueWrong() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setFixed(10);
        validator.validate(20, context);
    }

    @Test
    public void testFixedValue() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setFixed(10);
        validator.validate(10, context);
    }

    @Test
    public void testMinValue() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setMinExclusive(10);
        validator.validate(11, context);
    }

    @Test(expected=ValidationException.class)
    public void testMinValueWrong() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setMinExclusive(10);
        validator.validate(10, context);
    }

    @Test
    public void testMaxValue() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setMaxExclusive(10);
        validator.validate(9, context);
    }

    @Test(expected=ValidationException.class)
    public void testMaxValueWrong() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setMaxExclusive(10);
        validator.validate(10, context);
    }

    @Test
    public void testDigits() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setTotalDigits(3);
        validator.validate(9, context);
    }

    @Test(expected=ValidationException.class)
    public void testDigitsWrong() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.setTotalDigits(2);
        validator.validate(100, context);
    }

    @Test(expected=ValidationException.class)
    public void testNullValue() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.validate((Object) null, context);
    }

    @Test(expected=ValidationException.class)
    public void testWrongClass() throws ValidationException {
        IntegerValidator validator = new IntegerValidator();
        validator.validate((Object) 12.34, context);
    }
}
