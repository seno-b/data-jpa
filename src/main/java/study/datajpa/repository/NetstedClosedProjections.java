package study.datajpa.repository;

public interface NetstedClosedProjections {
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo{
        String getName();
    }
}
