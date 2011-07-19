package org.castor.cpa.persistence.convertor;

/**
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class EnumToOrdinal extends AbstractSimpleTypeConvertor {
    //-----------------------------------------------------------------------------------
    
    /**
     * Creates an instance of this class.
     */
    public EnumToOrdinal() {
        super(Enum.class, int.class);
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Object convert(final Object object) {
        try {
            return object.getClass().getMethod("ordinal").invoke(object);
        } catch (Exception ex) {
            return null;
        }
    }

    //-----------------------------------------------------------------------------------
}
