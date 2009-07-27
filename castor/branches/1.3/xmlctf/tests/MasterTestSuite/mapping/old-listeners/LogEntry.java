/**
 * Small wrapper class used to log pre/post marshal
 * calls.  Used rather than Strings directly to
 * allow MarshalListening to detect and avoid endless-loops.
 * If we are Marshalling the MarshalListener, then the
 * MarshalListener will end up continually modifying itself
 * as its marshalled -- each object that's marshalled will
 * add an entry to the list that will in turn get marshalled.
 *
 * So, by using an inner class we can detect the attempt to
 * marshal an instance of this class and skip it.
 */
public class LogEntry {
    public LogEntry () {
        setEntry("");
    }

    public LogEntry (String entry) {
        setEntry(entry);
    }
    public String toString () { return getEntry(); }

    public void setEntry (String entry) { _entry = entry; }
    public String getEntry () { return _entry; }
    String _entry;
}
