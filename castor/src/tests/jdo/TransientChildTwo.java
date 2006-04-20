package jdo;

public class TransientChildTwo {

	private Integer id;
	private TransientMaster entityOneId;
	private String description;

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return Returns the entityOneId.
	 */
	public TransientMaster getTransientMaster() {
		return entityOneId;
	}
	/**
	 * @param entityOneId The entityOneId to set.
	 */
	public void setTransientMaster(TransientMaster entityOneId) {
		this.entityOneId = entityOneId;
	}

}
