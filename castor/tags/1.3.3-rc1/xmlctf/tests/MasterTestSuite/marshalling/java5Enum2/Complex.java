public enum Complex {

    FIVE("5"), TEN("10");

    private final String value;

    Complex(String v) {
        value = v;
    }

    public static Complex fromValue(String value) {
        for (Complex c : Complex.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);

    }
    
    public String value() {
        return value;
    }
    
    public String toString() {
        return value;
    }

}
