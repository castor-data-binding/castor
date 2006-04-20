package jdo;

import java.util.Collection;

public final class TransientMaster {
    
    private Integer id;
    private String name;
    private Integer property1;
    private Integer property2;
    private Integer property3;
    private TransientChildOne entityTwo;
    private Collection entityThrees;
    
    public Integer getProperty2() {
        return this.property2;
    }
    
    public void setProperty2(Integer property2) {
        this.property2 = property2;
    }
    
    public Integer getProperty1() {
        return this.property1;
    }
    
    public void setProperty1(Integer property) {
        this.property1 = property;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }

	/**
	 * @return Returns the entityTwo.
	 */
	public TransientChildOne getEntityTwo() {
		return entityTwo;
	}

	/**
	 * @param entityTwo The entityTwo to set.
	 */
	public void setEntityTwo(TransientChildOne entityTwo) {
		this.entityTwo = entityTwo;
	}

	/**
	 * @return Returns the entityThrees.
	 */
	public Collection getEntityThrees() {
		return entityThrees;
	}

	/**
	 * @param entityThrees The entityThrees to set.
	 */
	public void setEntityThrees(Collection entityThrees) {
		this.entityThrees = entityThrees;
	}

	/**
	 * @return Returns the property3.
	 */
	public Integer getProperty3() {
		return property3;
	}

	/**
	 * @param property3 The property3 to set.
	 */
	public void setProperty3(Integer property3) {
		this.property3 = property3;
	}
}
