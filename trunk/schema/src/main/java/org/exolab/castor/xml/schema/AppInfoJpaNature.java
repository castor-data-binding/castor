package org.exolab.castor.xml.schema;

import java.util.List;

import org.castor.core.nature.BaseNature;

public class AppInfoJpaNature extends BaseNature {

    /**
     * Property key of content.
     */
    private static final String CONTENT_KEY = "content";

    /**
     * @param appInfo
     *            the appInfo in focus.
     */
    public AppInfoJpaNature(final AppInfo appInfo) {
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
    @SuppressWarnings("unchecked")
    public void addContent(final Object contentValue) {
        List<Object> content = getPropertyAsList(CONTENT_KEY);
        content.add(contentValue);
    }
    
    /**
     * Returns a list of JDO content values.
     * 
     * @return the JDO content values.
     */
    @SuppressWarnings("unchecked")
    public List<Object> getContent() {
        return this.getPropertyAsList(CONTENT_KEY);
    }

}
