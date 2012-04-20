package org.castor.jpa.scenario.sequence_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="sequence_generator_field")
public class AnnotatedField {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@SequenceGenerator(name="field_sequence", sequenceName="field_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="field_sequence")
	public Long getId() {
		return id;
	}
}
