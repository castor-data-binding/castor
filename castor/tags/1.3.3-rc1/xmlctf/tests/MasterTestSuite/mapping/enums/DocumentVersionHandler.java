import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * Castor FieldHandler Implementierung zum Marshalling/Unmarshalling einer DocumentVersion.
 *
 */
public final class DocumentVersionHandler extends GeneralizedFieldHandler {
    /**
     * Default Constructor
     */
    public DocumentVersionHandler() {
        super();
    }

    /**
     * Gibt die DocumentVersion als String zurueck.
     *
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
     */
    public Object convertUponGet(Object arg0) {
        String value = null;

        if (arg0 != null) {
            value = ((DocumentVersion) arg0).toString();
        }

        return value;
    }

    /**
     * Gibt die zum String passende DocumentVersion zur?ck.
     *
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
     */
    public Object convertUponSet(Object arg0) {
        return DocumentVersion.valueOf((String) arg0);
    }

    /**
     * Gibt den Typ der unterst?tzten Klasse zurueck.
     *
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
     */
    public Class getFieldType() {
        return DocumentVersion.class;
    }
}
