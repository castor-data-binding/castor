public enum Complex2 {

    FIVE("5"), TEN("10");

    private final String value;

    Complex2(String v) {
        value = v;
    }

    public static Complex2 fromValue(String value) {
        for (Complex2 c : Complex2.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);

    }
    
    public String value() {
        return value;
    }
    
}
