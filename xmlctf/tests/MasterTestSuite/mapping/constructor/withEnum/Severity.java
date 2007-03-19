public final class Severity {

    public static final Severity ERROR = new Severity("Error");

    public static final Severity WARNING = new Severity("Warning");

    public static final Severity OK = new Severity("OK");

    private final transient String name;

    private Severity(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Severity valueOf(String value) {
        if (value.equals("Error")) {
            return ERROR;
        } else if (value.equals("Warning")) {
            return WARNING;
        }
        return OK;
    }
}
