/**
 * Main Document Class.
 */
public class Document {
    private DocumentVersion version;
    private DocumentVersion versionWithHandler;

    /**
     * Default Constructor
     */
    public Document() {
    }

    /**
     * @return the version
     */
    public final DocumentVersion getVersion() {
        return version;
    }

    /**
     * @return the versionWithHandler
     */
    public final DocumentVersion getVersionWithHandler() {
        return versionWithHandler;
    }

    /**
     * @param version the version to set
     */
    public final void setVersion(DocumentVersion documentVersion) {
        this.version = documentVersion;
    }

    /**
     * @param versionWithHandler the versionWithHandler to set
     */
    public final void setVersionWithHandler(DocumentVersion documentVersionWithHandler) {
        this.versionWithHandler = documentVersionWithHandler;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof Document)) {
            return false;
        }
        
        Document document = (Document) obj;
        return (getVersion() == document.getVersion() &&
                getVersionWithHandler() == document.getVersionWithHandler()); 
    }
    
    
}
