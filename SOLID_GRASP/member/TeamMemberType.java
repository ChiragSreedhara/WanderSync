package member;

public interface TeamMemberType {
    String getName();
    String getEmail();
    void setName(String name);
    void setEmail(String email);
    RoleType getProjectRole(String projectName);
    void joinProject(String projectName, RoleType role);
    void leaveProject(String projectName);
    String performResponsibilitiesOnProject(String projectName);
}
