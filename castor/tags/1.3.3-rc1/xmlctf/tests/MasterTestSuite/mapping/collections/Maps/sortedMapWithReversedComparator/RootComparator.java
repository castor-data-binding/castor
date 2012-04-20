import java.util.Comparator;

public class RootComparator implements Comparator {

    public int compare(final Object arg0, final Object arg1) {
        if (arg0 == null || arg1 == null) {
            throw new NullPointerException("Objects to compare cannot be null");
        }

        if (!(arg0 instanceof java.lang.String)) {
            throw new ClassCastException(
                    "Problem converting objects to compare to java.lang.String");
        }

        if (!(arg0 instanceof java.lang.String)) {
            throw new ClassCastException(
                    "Problem converting objects to compare to java.lang.String");
        }

        return -1* ((java.lang.String) arg0).compareTo(
                ((java.lang.String) arg1));
    }
}
