/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.util;

import org.castor.util.StringUtil;

import junit.framework.TestCase;

/**
 * JUnit test case for {@link StringUtil}.
 * @since 1.1.2
 */
public class StringUtilTest extends TestCase {
    
    /**
     * Tests various combinations of replacement functionality.
     */
    public void testReplaceAll() {
        String source, expected;
        String toReplace = "hello";
        String replacement = "bonjour";
        /* no replacement */
        source = "hallo world";
        expected = source;
        assertEquals("no replacement exist", expected, StringUtil.replaceAll(
                source, toReplace, replacement));

    }
    
    public void testOneReplacement() throws Exception {
        String source = "hello world";
        String expected = "bonjour world";
        String toReplace = "hello";
        String replacement = "bonjour";
        assertEquals("one replacement", expected, StringUtil.replaceAll(source,
                toReplace, replacement));
        assertEquals("one replacement", expected, StringUtil.replaceAll(source,
                toReplace, replacement));

    }
    public void testMultipleeplacement() throws Exception {
        String source = "hello Bob, hello Alice, hello hell, hello world!";
        String expected = "bonjour Bob, bonjour Alice, bonjour hell, bonjour world!";
        String toReplace = "hello";
        String replacement = "bonjour";
        assertEquals("multiple replacements", expected, StringUtil.replaceAll(
                source, toReplace, replacement));
    }
    
    public void testEmptyString() throws Exception {
        String source = "";
        String expected = "";
        String toReplace = "hello";
        String replacement = "bonjour";
        assertEquals("empty source string", expected, StringUtil.replaceAll(
                source, toReplace, replacement));
    }
    
    public void testNull() throws Exception {
        String source = null;
        String expected = null;
        String toReplace = "hello";
        String replacement = "bonjour";
        assertEquals("null source string", expected, StringUtil.replaceAll(
                source, toReplace, replacement));
    }
}
