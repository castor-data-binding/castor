
public class MainApp {

    /**
     * The main method. 
     * Usage: test [-verbose] [-info] [-gui] [-res test.xml | -file test.xml | -url urlOfTest] testcases
     */
    public static void main( String args[] ) throws Exception {
        int     cur       = 0;
        boolean infoOnly  = false;
        boolean verbose   = false;
        boolean gui       = false;
        String  file      = null;
        String  url       = null;
        String  res       = null;
    
        if ( args.length == 0 ) {
            usage();
            return;
        }
    
        while ( cur < args.length && args[cur].startsWith( "-" ) ) {
            if ( args[cur].equals("-verbose") ) {
                verbose  = true;
            } else if ( args[cur].equals("-info") ) {
                infoOnly = true;
            } else if ( args[cur].equals("-gui") ) {
                gui = true;
            } else if ( args[cur].equals("-file") ) {
                cur++;
                if ( cur < args.length && args[cur] != null && !args[cur].trim().equals("") )
                    file = args[cur].trim();
                else {
                    usage();
                    return;
                }
            } else if ( args[cur].equals("-url") ) {
                cur++;
                if ( cur < args.length && args[cur] != null && !args[cur].trim().equals("") )
                    url = args[cur].trim();
                else {
                    usage();
                    return;
                }
            } else if ( args[cur].equals("-res") ) {
                cur++;
                if ( cur < args.length && args[cur] != null && !args[cur].trim().equals("") )
                    res = args[cur].trim();
                else {
                    usage();
                    return;
                }
            }
            cur++;
        }
        if ( cur >= args.length ) {
            if ( !infoOnly ) {
                usage();
                return;
            }
            Main main = new Main( verbose, infoOnly, gui, res, file, url, null );
    		junit.textui.TestRunner.run(main);
    		return;
        }
        if ( cur < (args.length-1) ) {
            System.out.println( "argument(s) ignored:" );
            for ( int i = (cur+1); i < args.length; i++ ) {
                System.out.println( args[i] + "\t" );
            }
        }
        Main main = new Main(verbose, infoOnly, gui, res, file, url, args[cur]);
        junit.textui.TestRunner.run(main);
    }

    /**
     * Print the usage
     */
    private static void usage() {
        System.out.println( "Usage: test [-verbose] [-info] [-gui] [-res test.xml | -file test.xml | -url urlOfTest] testcases" );
    }

    
}
