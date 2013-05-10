package org.castor.cpa.test.test882;

import java.util.Collection;

public class Product {
	private Integer _id;
	private String _name;
	private Collection<ProductDetail> _details;
	private Collection<ProductDetailLazy> _detailslazy;

	public Collection<ProductDetail> getDetails() {
		return _details;
	}

	public void setDetails(Collection<ProductDetail> details) {
		this._details = details;
	}

	public Collection<ProductDetailLazy> getDetailslazy() {
		return _detailslazy;
	}

	public void setDetailslazy(Collection<ProductDetailLazy> detailsLazy) {
		this._detailslazy = detailsLazy;
	}

	public Integer getId() {
		return _id;
	}

	public void setId(final Integer id) {
		_id = id;
	}

	public String getName() {
		return _name;
	}

	public void setName(final String name) {
		_name = name;
	}
}
