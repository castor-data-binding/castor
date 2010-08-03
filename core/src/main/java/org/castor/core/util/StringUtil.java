/*
 * Copyright 2007 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.core.util;

/**
 * Common functionality relate to String processing.
 *
 * @since 1.2
 */
public class StringUtil {
	
	/**
	 * The empty String <code>""</code>.
	 */
	public static final String _emptyString = "";
    
    /**
     * Replaces all occurences of a pattern within a String.
     * @param source The source string.
     * @param toReplace The character to replace.
     * @param replacement The replacement.
     * @return The new String with characters replaced.
     */
    public static String replaceAll(final String source, final String toReplace,
            final String replacement) {
        if (source == null) {
            return null;
        }
        String returnValue = source;
        int idx = source.lastIndexOf(toReplace);
        if (idx != -1) {
            StringBuffer ret = new StringBuffer(source);
            ret.replace(idx, idx + toReplace.length(), replacement);
            while ((idx = source.lastIndexOf(toReplace, idx - 1)) != -1) {
                ret.replace(idx, idx + toReplace.length(), replacement);
            }
            returnValue = ret.toString();
        }
        return returnValue;
    }
    
	/**
	 * Compares two Strings, returning true if they are equal.
	 * 
	 * @see org.apache.commons.lang.StringUtils
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * Checks if a String is empty ("") or null.
	 * 
	 * @see org.apache.commons.lang.StringUtils
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>
	 * Checks if a String is not empty ("") and not null.
	 * </p>
	 * 
	 * @see org.apache.commons.lang.StringUtils
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null
	 */
	public static boolean isNotEmpty(String str) {
		return !StringUtil.isEmpty(str);
	}

	/**
	 * Returns either the passed in String, or if the String is null, an empty
	 * String ("").
	 * 
	 * @see org.apache.commons.lang.StringUtils
	 * @param str
	 *            the String to check, may be null
	 * @return the passed in String, or the empty String if it was
	 *         <code>null</code>
	 */
	public static String defaultString(String str) {
		return str == null ? _emptyString : str;
	}
}
