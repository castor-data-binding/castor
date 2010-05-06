package org.castor.jpa.scenario.cache;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.castor.jdo.jpa.annotations.Cache;
import org.castor.jdo.jpa.annotations.CacheProperty;

@Entity
@Cache({
	@CacheProperty(key="type", value="none")
})
@Table(name="Cache_none")
public class NonCachingEntity implements CacheTestEntity {

    private long id;
    private String name;

	@Id
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
