package role;

public class ContributorRole extends ViewerRole {
    private static final String NAME = "Contributor";

    public ContributorRole(String projectName) {
        super(projectName);
    }

    @Override
    public String performResponsibility() {
        return String.format("%s is contributing to project.", NAME);
    }
}