package org.castor.jpa.scenario.table_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity(name="annotated_class_with_default_table_generator_definition")
@TableGenerator(name="tableGeneratorOnClass")
public class AnnotatedClassWithDefaultTableGeneratorDefinition {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="tableGeneratorOnClass")
	public Long getId() {
		return id;
	}
}
