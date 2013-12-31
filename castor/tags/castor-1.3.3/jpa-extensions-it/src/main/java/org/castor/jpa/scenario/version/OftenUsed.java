package org.castor.jpa.scenario.version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity(name="version")
@SequenceGenerator(name="versionGenerator", sequenceName="version_sequence")
public class OftenUsed implements VersionTest {

    private long id;
    private String name;
    private long version;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="versionGenerator")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
