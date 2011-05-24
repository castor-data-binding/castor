package org.castor.jpa.scenario.sequence_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="sequence_generator_class")
@SequenceGenerator(name="class_sequence", sequenceName="class_sequence")
public class AnnotatedClass {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="class_sequence")
	public Long getId() {
		return id;
	}
}
