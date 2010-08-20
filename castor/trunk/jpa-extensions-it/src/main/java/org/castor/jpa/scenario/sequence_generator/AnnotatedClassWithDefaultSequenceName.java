package org.castor.jpa.scenario.sequence_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="sequence_generator_class_with_default_sequence_name")
@SequenceGenerator(name="class_sequence_with_default_name")
public class AnnotatedClassWithDefaultSequenceName {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="class_sequence_with_default_name")
	public Long getId() {
		return id;
	}
}
