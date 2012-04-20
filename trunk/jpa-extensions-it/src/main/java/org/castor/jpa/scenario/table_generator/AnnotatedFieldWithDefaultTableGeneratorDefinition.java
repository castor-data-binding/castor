package org.castor.jpa.scenario.table_generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity(name="annotated_field_with_default_table_generator_definition")
public class AnnotatedFieldWithDefaultTableGeneratorDefinition {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@TableGenerator(name="tableGeneratorOnField")
	@GeneratedValue(strategy=GenerationType.TABLE, generator="tableGeneratorOnField")
	public Long getId() {
		return id;
	}
}
