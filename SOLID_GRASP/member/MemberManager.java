package member;

import java.util.HashMap;
import java.util.Map;

public class MemberManager {
    private AtomicReference static MemberManager memberManager;
    private Map<String, ProjectType> projects;
    private Map<String, TeamMemberType> teamMembers;

    private MemberManager() {
        this.projects = new HashMap<>();
        this.teamMembers = new HashMap<>();
    }

    public static MemberManager getInstance() {
        if (memberManager == null) {
            synchronized (MemberManager.class) {
                if (memberManager == null) {
                    memberManager = new MemberManager();
                }
            }
        }
        return memberManager;
    }

    public ProjectType getProjectByName(String projectName) {
        return projects.get(projectName);
    }

    public TeamMemberType getTeamMemberByName(String teamMemberName) {
        return teamMembers.get(teamMemberName);
    }

    public void joinProject(String projectName, TeamMember teamMember) {
        ProjectType p = getProjectByName(projectName);
        p.addTeamMember(teamMember.getName());
    }

    public void leaveProject(String projectName, TeamMember teamMember) {
        ProjectType p = getProjectByName(projectName);
        p.removeTeamMember(teamMember.getName());
    }

    public void addTeamMember(Project project, String teamMemberName) {
        TeamMemberType t = getTeamMemberByName(teamMemberName);
        t.joinProject(project.getName(), new ViewerRole(project.getName()));
    }

    public void removeTeamMember(Project project, String teamMemberName) {
        TeamMemberType t = getTeamMemberByName(teamMemberName);
        t.leaveProject(project.getName());
    }
}

