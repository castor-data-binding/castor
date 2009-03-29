/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $$
 */


package harness;


import java.util.Enumeration;
import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestResult;

public class TestHarness extends TestSuite {

    private String _description;

    private TestHarness _suite;

    private String _name;

    public static PrintStream _stream;

    public static boolean _verbose;

    public TestHarness(final TestHarness suite, final String name, final String description) {
        super(name);
        setName(name);
        setDescription(description);
        setSuite(suite);
    }
    
    public static void setVerboseStream(final PrintStream verboseStream) {
        _stream = verboseStream;
    }
    
    public static PrintStream getVerboseStream() {
        return _stream;
    }
    
    public static void setVerbose(final boolean vb) {
        _verbose = vb;
    }
    
    public static boolean getVerbose() {
        return _verbose;
    }
    
    public void setName(final String name) {
        this._name = name;
    }
    
    public String getName() {
        return this._name;
    }
    
    public String getDescription() {
        return _description;
    }
    
    public void setDescription(final String desc) {
        this._description = desc;
    }
    
    public void setSuite(final TestHarness superTest) {
        this._suite = superTest;
    }
    
    public TestHarness getSuite() {
        return _suite;
    }
    
    public void printInfo(final PrintStream ps) {
        printInfo(ps, null);
    }
    
    public void printInfo(final PrintStream ps, final String branch) {
        if ((branch == null) || branch.equals("") || branch.startsWith(getName())) {
            String sub = null;
            if ((branch != null) && branch.startsWith(getName())) {
                sub = branch.substring(
                        (branch.indexOf(".") == -1)
                        ? branch.length()
                        : branch.indexOf(".") + 1);
            }
            StringBuffer sb = new StringBuffer();
            sb.append(getName());
            TestHarness upper = _suite;
            while (upper != null) {
                sb.insert(0, ".");
                sb.insert(0, upper.getName());
                upper = upper._suite;
            }
            sb.insert(0, "[");
            sb.append("]");
            sb.append(' ');
            sb.append(_description);
            sb.append('\n');
            ps.print(sb.toString());
            Enumeration<Test> enumeration = tests();
            while (enumeration.hasMoreElements()) {
                Object obj = enumeration.nextElement();
                if (obj instanceof TestHarness) {
                    ((TestHarness) obj).printInfo(ps, sub);
                } else if (obj instanceof CastorTestCase) {
                    ((CastorTestCase) obj).printInfo(ps, sub);
                }
            }
        }
    }
    public void run(final TestResult result) {
        System.out.println();
        System.out.print("testsuite: " + getName());
        super.run(result);
    }
    public void run(final TestResult result, final String branch) {
        System.out.println("testsuite: " + getName() + " branch: " + branch);
        if ((branch == null) || branch.equals("")) {
            run(result);
        } else if (branch.startsWith(getName())) {
            String sub = branch.substring(
                    (branch.indexOf(".") == -1
                    ? branch.length()
                    : branch.indexOf(".") + 1));
            for (Enumeration<Test> e = tests(); e.hasMoreElements(); ) {
                if (result.shouldStop()) {
                    break;
                }
                TestHarness test = (TestHarness) e.nextElement();
                test.run(result, sub);
            }
        }
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        TestHarness upper = _suite;
        while (upper != null) {
            sb.insert(0, ".");
            sb.insert(0, upper.getName());
            upper = upper._suite;
        }
        sb.insert(0, "[");
        sb.append("]");
        sb.append(' ');
        sb.append(_description);
        sb.append('\n');

        Enumeration<Test> enumeration = tests();
        while (enumeration.hasMoreElements()) {
            sb.append(enumeration.nextElement()).toString();
        }
        return sb.toString();
    }
}
