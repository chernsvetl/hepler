package normative_control.validation.validators;

public class ValidatorDataReport {
    public int minPages;
    public String font;
    public String sizeRange;
    public String style;

    public ValidatorDataReport(int minPages, String font, String sizeRange, String style) {
        this.minPages = minPages;
        this.font = font;
        this.sizeRange = sizeRange;
        this.style = style;
    }
}
