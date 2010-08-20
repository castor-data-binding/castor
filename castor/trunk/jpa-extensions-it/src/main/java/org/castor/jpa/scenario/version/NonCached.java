package org.castor.jpa.scenario.version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.castor.jdo.jpa.annotations.Cache;
import org.castor.jdo.jpa.annotations.CacheProperty;

@Entity(name="non_cached_version")
@SequenceGenerator(name="nonCachedVersionGenerator", sequenceName="non_cached_version_sequence")
@Cache({
    @CacheProperty(key="type", value="none")
})
public class NonCached implements VersionTest {

    private long id;
    private String name;
    private long version;

    /* (non-Javadoc)
     * @see org.castor.jpa.scenario.version.VersionTest#getId()
     */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="nonCachedVersionGenerator")
    public long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.castor.jpa.scenario.version.VersionTest#setId(long)
     */
    public void setId(long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.castor.jpa.scenario.version.VersionTest#getVersion()
     */
    @Version
    public long getVersion() {
        return version;
    }

    /* (non-Javadoc)
     * @see org.castor.jpa.scenario.version.VersionTest#setVersion(long)
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /* (non-Javadoc)
     * @see org.castor.jpa.scenario.version.VersionTest#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.castor.jpa.scenario.version.VersionTest#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
