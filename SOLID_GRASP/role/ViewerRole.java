package role;

public class ViewerRole implements RoleType {
    private static final String NAME = "Viewer";
    private String description;

    public ViewerRole(String projectName) {
        this.description = "Viewer of " + projectName;
    }
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String performResponsibility() {
        return String.format("%s is viewing project.", NAME);
    }
}