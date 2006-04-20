/*
 * File: XslNumberFormat
 *
 * $Id$
 */

package org.mitre.tjt.xsl;

public class XslNumberFormat {
    
    public XslNumberFormat() {
        super();
    }

    public static String format(int[] counts, String format) {
        StringBuffer buf = new StringBuffer();
        XslFormatToken formatTok = XslFormatToken.parseFormat(format);

        for(int x = 0; x < counts.length; x++) {
            buf.append(formatTok.format(counts[x]));
            if (formatTok.nextToken() != null)
                formatTok = formatTok.nextToken();
        }
        
        // Print any post separator that was specified in the format template
        buf.append(formatTok.getPostSeparator());
        return buf.toString();
    }

    public static void main(String args[]) {
        if(args.length < 2) {
            System.out.println("Usage: java XslNumberFormat " + 
                "format_str count_1 count_2 ...");
            return;
        }
        String formatStr = args[0];
        int counts[] = new int[args.length - 1];
        for(int x = 1; x < args.length; x++) {
            counts[x - 1] = Integer.parseInt(args[x]);
        }
        System.out.println("\"" + format(counts, formatStr) + "\"");
    }
}
