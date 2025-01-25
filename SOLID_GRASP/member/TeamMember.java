package member;

import java.util.HashMap;
import java.util.Map;

public class TeamMember implements TeamMemberType {
    private String name;
    private String email;
    private Map<String, RoleType> roles;

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
        this.roles = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public RoleType getProjectRole(String projectName) {
        return roles.get(projectName);
    }

    @Override
    public void joinProject(String projectName, RoleType role) {
        roles.put(projectName, role);
        MemberManager.getInstance().joinProject(projectName, this);
    }

    @Override
    public void leaveProject(String projectName) {
        roles.remove(projectName);
        MemberManager.getInstance().leaveProject(projectName, this);
    }

    @Override
    public String performResponsibilitiesOnProject(String projectName) {
        return getProjectRole(projectName).performResponsibility();
    }
}