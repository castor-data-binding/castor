package org.castor.cpa.jpa.info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity
public final class TableGeneratedValueTestClass {
    private Long _id;
    
    public void setId(final Long id) {
        _id = id;
    }
    
    @Id
    @TableGenerator(name = "tableGenerator", table = "generatorTable")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    public Long getId() {
        return _id;
    }
}
