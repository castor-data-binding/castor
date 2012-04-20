package org.castor.cpa.jpa.info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public final class SequenceGeneratedValueTestClass {
    private Long _id;
    
    public void setId(final Long id) {
        _id = id;
    }
    
    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "test_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    public Long getId() {
        return _id;
    }
}
