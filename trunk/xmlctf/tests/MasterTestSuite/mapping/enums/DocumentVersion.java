import java.util.Arrays;
import java.util.List;

/**
 * Enum Style Class for Document Version.
 */
public final class DocumentVersion {
    private static final byte ADDOCUMENT_1_0_VALUE = (byte) 0;
    private static final String ADDOCUMENT_1_0_XML_VALUE = "addocument_1_0";
    private static final byte ADDOCUMENT_1_1_VALUE = (byte) 1;
    private static final String ADDOCUMENT_1_1_XML_VALUE = "addocument_1_1";

    /**
     * Document Version 1.0
     */
    public static final DocumentVersion ADDOCUMENT_1_0 = new DocumentVersion(ADDOCUMENT_1_0_VALUE,
                                                                             ADDOCUMENT_1_0_XML_VALUE);

    /**
     * Document Version 1.1
     */
    public static final DocumentVersion ADDOCUMENT_1_1 = new DocumentVersion(ADDOCUMENT_1_1_VALUE,
                                                                             ADDOCUMENT_1_1_XML_VALUE);

    /**
     * Interne Liste mit allen Typ-Definitionen
     */
    private static final DocumentVersion[] ALL = { ADDOCUMENT_1_0, ADDOCUMENT_1_1 };

    /**
     * der Name des Typs
     */
    private final String name;

    /**
     * der Wert des Typs
     */
    private final byte value;

    /**
     * Setzt den Namen des Typs
     *
     * @param val Der Wert des Typs
     * @param name Der Name des Typs
     */
    private DocumentVersion(final byte val, final String name) {
        this.value = val;
        this.name = name;
    }

    /**
     * Gibt alle Typen zur&uuml;ck
     *
     * @return Eine Liste mit allen Typen
     */
    public static List getAll() {
        return Arrays.asList(ALL);
    }

    /**
     * Gibt den Namen des Typs zur&uuml;ck
     *
     * @return Der Name des Typs
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den internen Wert des Typs zur&uuml;ck
     *
     * @return Der Wert des Typs
     */
    public byte getValue() {
        return value;
    }

    /**
     * Sucht zum Key den passenden Typ
     *
     * @param key Der gesuchte Key
     * @return Der passende Typ, oder eine IllegalArgumentException
     */
    public static DocumentVersion lookup(byte key) {
        for (int i = 0; i < ALL.length; i++) {
            if (key == ALL[i].getValue()) {
                return ALL[i];
            }
        }

        // we have no default eventtype, so we throw an exception
        throw new IllegalArgumentException("Unknown DocumentVersion with key " +
                                           key);
    }

    /**
     * Gibt die String-Version des Typs zur&uuml;ck
     *
     * @return String-Version des Typs
     */
    public String toString() {
        return this.getName();
    }

    /**
     * Liefert f&uuml;r einen String den entsprechenden Typ zur&uuml;ck.<br>
     * Wird unter anderem vom Castor verwendet.
     *
     * @param value der Typ als String
     * @return das passtende Typ-Objekt oder <code>null</code> wenn f&uuml;r den String kein entsprechender Typ vorhanden ist
     */
    public static DocumentVersion valueOf(String value) {
        if (ADDOCUMENT_1_0_XML_VALUE.equals(value)) {
            return ADDOCUMENT_1_0;
        } else if (ADDOCUMENT_1_1_XML_VALUE.equals(value)) {
            return ADDOCUMENT_1_1;
        }

        // no default type, so throw an exception
        throw new IllegalArgumentException("No such documentVersion: " + value);
    }
}
