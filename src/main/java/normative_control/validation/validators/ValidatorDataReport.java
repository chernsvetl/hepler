package normative_control.validation.validators;

public class ValidatorDataReport {
    public int minPages;
    public String font;
    public String sizeRange;
    public String style;
    public String full_text;
    public String full_text2;

    public ValidatorDataReport(int minPages, String font, String sizeRange, String style, String full_text, String full_text2) {
        this.minPages = minPages;
        this.font = font;
        this.sizeRange = sizeRange;
        this.style = style;
        this.full_text = full_text;
        this.full_text2 = full_text2;
    }
}
