/*
 * Copyright 2006 Assaf Arkin, Ralf Joachim
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
 *
 * $Id$
 */
package org.castor.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * I18N message formatting class. A static factory for obtaining
 * messages and formatting messages with arguments.
 * <p>
 * The resource file <tt>org.exolab.castor.util.resources.messages</tt>
 * contains a list of all the messages in English. Additional resource
 * files can be added for other languages and locales by placing them
 * in the same package with a language/locale prefix. See the I18N
 * documentation and use of resource bundles in the JDK docs.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0.1
 */
public final class Messages {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Messages.class);
    
    /** The name of the resource holding all the messages in the English
     *  language. Resources for other languages and locales use the same
     *  name with a language/locale prefix. */
    public static final String RESOURCE_NAME = "org.castor.messages";
    
    /** The resource bundle holds all the messages. */
    private static ResourceBundle   _messages;
    
    /** Once a format has been created once, it is cached here. */
    private static Hashtable        _formats;
    //--------------------------------------------------------------------------

    static { setDefaultLocale(); }

    //--------------------------------------------------------------------------

    /**
     * Set the default locale to use for loading messages. Calling this method
     * will reload all the messages based on the new locale name.
     */
    public static void setDefaultLocale() {
        setLocale(Locale.getDefault());
    }
    
    /**
     * Set the locale to use for loading messages. Calling this method
     * will reload all the messages based on the new locale name.
     * 
     * @param locale the locale for which a resource bundle is desired.
     */
    public static void setLocale(final Locale locale) {
        try {
            _messages = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        } catch (Exception except) {
            _messages = new EmptyResourceBundle();
            LOG.error("Failed to locate messages resource " + RESOURCE_NAME);
        }
        _formats = new Hashtable();
    }
    
    /**
     * Format the named message using a single argument and return the
     * full message text.
     *
     * @param message The message name
     * @param arg1 The first argument
     * @return The full message text
     */
    public static String format(final String message, final Object arg1) {
        return format(message, new Object[] {arg1});
    }
    
    /**
     * Format the named message using two argument and return the
     * full message text.
     *
     * @param message The message name
     * @param arg1 The first argument
     * @param arg2 The second argument
     * @return The full message text
     */
    public static String format(final String message,
            final Object arg1, final Object arg2) {
        return format(message, new Object[] {arg1, arg2});
    }
    
    /**
     * Format the named message using three argument and return the
     * full message text.
     *
     * @param message The message name
     * @param arg1 The first argument
     * @param arg2 The second argument
     * @param arg3 The third argument
     * @return The full message text
     */
    public static String format(final String message,
            final Object arg1, final Object arg2, final Object arg3) {
        return format(message, new Object[] {arg1, arg2, arg3});
    }

    /**
     * Format the named message using any number of arguments and return the
     * full message text.
     *
     * @param message The message name
     * @param args Argument list
     * @return The full message text
     */
    public static String format(final String message, final Object[] args) {
        
        try {
            MessageFormat mf = (MessageFormat) _formats.get(message);
            if (mf == null) {
                String msg;
                try {
                    msg = _messages.getString(message);
                } catch (MissingResourceException except) {
                    return message;
                }
                mf = new MessageFormat(msg);
                _formats.put(message, mf);
            }
            return mf.format(args);
        } catch (Exception except) {
            return "An internal error occured while processing message " + message;
        }
    }

    /**
     * Return the text of the named message without formatting.
     *
     * @param message The message name
     * @return The full message text
     */
    public static String message(final String message) {
        try {
            return _messages.getString(message);
        } catch (MissingResourceException except) {
            return message;
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * Hide default constructor of utility class.
     */
    private Messages() { }
    
    //--------------------------------------------------------------------------

    /**
     * A empty resource bundle.
     */
    private static class EmptyResourceBundle
    extends ResourceBundle implements Enumeration {
        /**
         * {@inheritDoc}
         * @see java.util.ResourceBundle#getKeys()
         */
        public Enumeration getKeys() {
            return this;
        }
        
        /**
         * {@inheritDoc}
         * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
         */
        protected Object handleGetObject(final String name) {
            return "[Missing message " + name + "]";
        }
        
        /**
         * {@inheritDoc}
         * @see java.util.Enumeration#hasMoreElements()
         */
        public boolean hasMoreElements() {
            return false;
        }
        
        /**
         * {@inheritDoc}
         * @see java.util.Enumeration#nextElement()
         */
        public Object nextElement() {
            return null;
        }
    }

    //--------------------------------------------------------------------------
}
