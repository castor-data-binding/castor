package org.castor.jdo.jpa.info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class SequenceGeneratedValueTestClass {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@SequenceGenerator(name="sequenceGenerator", sequenceName="test_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequenceGenerator")
	public Long getId() {
		return id;
	}
}
