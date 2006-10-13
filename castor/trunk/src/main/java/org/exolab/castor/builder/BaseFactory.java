package org.exolab.castor.builder;

/**
 * This class defines a base type for the source generator code factory classes.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6287 $ $Date: $
 */
public class BaseFactory {

    /**
     * The BuilderConfiguration instance, for callbacks to obtain certain
     * configured properties
     */
    protected BuilderConfiguration _config = null;
    /**
     * The type factory.
     */
    protected FieldInfoFactory infoFactory = null;

    /**
     * Creates an instance of this class.
     * @param config XML code generator configuration
     * @param infoFactory
     */
    public BaseFactory(BuilderConfiguration config, FieldInfoFactory infoFactory) {
        if (config == null) {
            String err = "The 'BuilderConfiguration' argument must not be null.";
            throw new IllegalArgumentException(err);
        }
        _config = config;

        if (infoFactory == null) {
            this.infoFactory = new FieldInfoFactory();
        } else {
            this.infoFactory = infoFactory;
        }
    }

    /**
     * Normalizes the given string for use in comments.
     *
     * @param value
     *            the String to normalize
     * @return the given string, normalized, for use in comments.
     */
    protected String normalize(String value) {
        if (value == null) {
            return null;
        }

        char[]  chars    = value.toCharArray();
        char[]  newChars = new char[chars.length + 10];
        int     count    = 0;
        int     i        = 0;
        boolean skip     = false;

        while (i < chars.length) {
            char ch = chars[i++];

            if ((ch == ' ') || (ch == '\t')) {
                if ((!skip) && (count != 0)) {
                    newChars[count++] = ' ';
                }
                skip = true;
            } else if (ch =='*') {
                if (chars[i] == '/') {
                    newChars[count++] = ch;
                    newChars[count++] = '\\';
                }
            } else {
                if (count == 0) {
                    //-- ignore new lines only if count == 0
                    if ((ch == '\r') || (ch == '\n')) {
                        continue;
                    }
                }
                newChars[count++] = ch;
                skip = false;
            }
        }
        return new String(newChars,0,count);
    }

}
