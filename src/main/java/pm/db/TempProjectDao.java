package pm.db;

import pm.pojo.TempProject;

public interface TempProjectDao {

    public Integer createProject(TempProject tp);

    public void deleteExpiredProjects(Integer lifetimeSeconds);

    public void deleteProject(Integer pid);

    public Integer getNextNewProjectId();

    public String getOwner(Integer pid);

    public TempProject getProject(Integer pid);

    public Boolean projectExists(Integer pid);

    public void updateLastVisited(Integer pid);

    public void updateProject(TempProject tp);
}
