package normative_control.validation.validators;

public class ValidatorDataReview {
    public String sizeRange;
    public String style;
    public String dateFinishPractice;

    public ValidatorDataReview(String sizeRange,
                               String style,
                               String dateFinishPractice) {
        this.sizeRange = sizeRange;
        this.style = style;
        this.dateFinishPractice = dateFinishPractice;
    }
}
