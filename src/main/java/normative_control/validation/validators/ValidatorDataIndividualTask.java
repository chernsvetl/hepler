package normative_control.validation.validators;

public class ValidatorDataIndividualTask {
    public int minPages;
    public String font;
    public String sizeRange;
    public String style;
    public String orgStepEndDate;
    public String prepareAndDefendStepEndDate;
    public String prepareAndDefendStepContent;
    public String orgStepContent;
    public String individualStepContent;
    public String individualStepDate;
    public String individualStepForm;

    public ValidatorDataIndividualTask(int minPages, String font, String sizeRange, String style, String orgStepEndDate,
                                       String prepareAndDefendStepEndDate, String prepareAndDefendStepContent,
                                       String orgStepContent, String individualStepContent,
                                       String individualStepDate, String individualStepForm) {
        this.minPages = minPages;
        this.font = font;
        this.sizeRange = sizeRange;
        this.style = style;
        this.orgStepEndDate = orgStepEndDate;
        this.prepareAndDefendStepEndDate = prepareAndDefendStepEndDate;
        this.prepareAndDefendStepContent = prepareAndDefendStepContent;
        this.orgStepContent = orgStepContent;
        this.individualStepContent = individualStepContent;
        this.individualStepDate = individualStepDate;
        this.individualStepForm = individualStepForm;
    }
}
