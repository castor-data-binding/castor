package org.exolab.castor.xml;

import java.util.Hashtable;
import java.util.Map;

/**
 * Default {@link IDResolver} for Castor XML during (un)marshaling.
 * 
 * @see org.exolab.castor.xml.IDResolver
 */
class IDResolverImpl implements IDResolver {

    /**
     * A collection of IDREF --> target object mappings.
     */
    private Map<String, Object> _idReferences = new Hashtable<String, Object>();

    /**
     * A custom (user-injected) IDResolver instance to be used for IDREF
     * resolution.
     */
    private IDResolver _idResolver = null;

    /**
     * Binds a mapping from an ID to the referenced target object.
     * 
     * @param id
     *            Object identifier
     * @param object
     *            Object being identified by ID
     * @param isValidating
     *            True if validation is enabled.
     * @throws ValidationException
     *             If an ID is used more than once.
     */
    void bind(final String id, final Object object, final boolean isValidating)
            throws ValidationException {

        if (isValidating && id == null) {
            throw new ValidationException("Invalid ID value 'null' encountered");
        }

        if (isValidating && id.equals("")) {
            throw new ValidationException("Empty ID value encountered");
        }

        if (isValidating && _idReferences.containsKey(id)) {
            if (!(id.equals("org.exolab.castor.mapping.MapItem") || id
                    .equals("HIGH-LOW"))) {
                throw new ValidationException("Duplicate ID " + id
                        + " encountered");
            }
        } else {
            _idReferences.put(id, object);
        }

    }

    /**
     * Returns the Object whose id matches the given IDREF, or 'null' if no
     * object was found.
     * 
     * @param idref
     *            the IDREF to resolve.
     * @return the Object whose id matches the given IDREF.
     */
    public Object resolve(final String idref) {

        Object object = _idReferences.get(idref);
        if (object != null) {
            return object;
        }

        if (_idResolver != null) {
            return _idResolver.resolve(idref);
        }

        return null;
    }

    /**
     * Sets a custom IDResolver instance to be used for IDRef resolution.
     * 
     * @param idResolver
     *            a custom IDResolver instance to be used.
     */
    void setResolver(final IDResolver idResolver) {
        _idResolver = idResolver;
    }

}
