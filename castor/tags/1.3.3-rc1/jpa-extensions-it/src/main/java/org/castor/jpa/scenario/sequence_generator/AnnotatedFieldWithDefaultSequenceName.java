package org.castor.jpa.scenario.sequence_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="sequence_generator_field_with_default_sequence_name")
public class AnnotatedFieldWithDefaultSequenceName {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@SequenceGenerator(name="field_sequence_with_default_name")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="field_sequence_with_default_name")
	public Long getId() {
		return id;
	}
}
