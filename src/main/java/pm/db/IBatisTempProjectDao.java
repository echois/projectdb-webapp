package pm.db;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import pm.pojo.TempProject;

public class IBatisTempProjectDao extends SqlSessionDaoSupport implements
        TempProjectDao {

    @Override
    public Integer createProject(final TempProject tp) {
        return getSqlSession().insert("createTempProject", tp);
    }

    @Override
    public void deleteExpiredProjects(final Integer lifetimeSeconds) {
        final Long maxTimestamp = (System.currentTimeMillis() / 1000)
                - lifetimeSeconds;
        getSqlSession().insert("deleteExpiredTempProjects", maxTimestamp);
    }

    @Override
    public void deleteProject(final Integer projectId) {
        getSqlSession().update("deleteTempProject", projectId);
    }

    @Override
    public Integer getNextNewProjectId() {
        Integer id = ((Integer) getSqlSession().selectOne("getMinId"));
        if (id == null || id > -1) {
            id = 0;
        }
        return id - 1;
    }

    @Override
    public String getOwner(final Integer pid) {
        return (String) getSqlSession().selectOne("getOwner", pid);
    }

    @Override
    public TempProject getProject(final Integer pid) {
        return (TempProject) getSqlSession().selectOne("getTempProjectById",
                pid);
    }

    @Override
    public Boolean projectExists(final Integer pid) {
        final Integer count = (Integer) getSqlSession().selectOne(
                "countOccurences", pid);
        return count.equals(0) ? false : true;
    }

    public Boolean projectExistsAndOwnedByUser(final Integer pid,
            final String user) {
        final String owner = (String) getSqlSession()
                .selectOne("getOwner", pid);
        return projectExists(pid) && owner.equals(user);
    }

    @Override
    public void updateLastVisited(final Integer projectId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", projectId);
        params.put("lastVisited", System.currentTimeMillis() / 1000);
        getSqlSession().insert("updateTempProjectLastVisited", params);
    }

    @Override
    public void updateProject(final TempProject tp) {
        getSqlSession().update("updateTempProject", tp);
    }

}
