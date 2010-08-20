package org.castor.jdo.jpa.info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity
public class TableGeneratedValueTestClass {

	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Id
	@TableGenerator(name="tableGenerator", table="generatorTable")
	@GeneratedValue(strategy=GenerationType.TABLE, generator="tableGenerator")
	public Long getId() {
		return id;
	}
}
