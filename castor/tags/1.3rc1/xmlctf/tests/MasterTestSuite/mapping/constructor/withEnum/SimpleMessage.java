public final class SimpleMessage {

    private Severity severity;

    private String text;

    public SimpleMessage(String text, Severity severity) {
        this.text = text;
        this.severity = severity;
    }

    public Severity severity() {
        return severity;
    }

    public String formattedText() {
        return text;
    }

}
