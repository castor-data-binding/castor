package org.exolab.castor.xml.schema;

import org.castor.core.nature.BaseNature;

public class AppInfoSolrjNature extends BaseNature {

    /**
     * Property key of content.
     */
    private static final String CONTENT_KEY = "content";

    /**
     * @param appInfo
     *            the appInfo in focus.
     */
    public AppInfoSolrjNature(final AppInfo appInfo) {
        super(appInfo);
    }

    /**
     * Returns the id of the Nature. Implementation returns the fully qualified
     * class name.
     * 
     * @return the id.
     * @see org.exolab.castor.builder.info.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Adds a content value to the content.
     * 
     * @param contentValue
     *            content value
     */
    public void setContent(final Object content) {
        setProperty(CONTENT_KEY, content);
    }
    
    /**
     * Returns a list of JDO content values.
     * 
     * @return the JDO content values.
     */
    public Object getContent() {
        return this.getProperty(CONTENT_KEY);
    }

}
