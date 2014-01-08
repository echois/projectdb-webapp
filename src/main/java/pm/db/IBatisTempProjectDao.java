package pm.db;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import pm.pojo.TempProject;

import java.util.HashMap;
import java.util.Map;

public class IBatisTempProjectDao extends SqlSessionDaoSupport implements TempProjectDao {

	public Integer createProject(final TempProject tp) {
		return (Integer) getSqlSession().insert("createTempProject", tp);
	}

	public TempProject getProject(Integer pid) {
		return (TempProject) getSqlSession().selectOne("getTempProjectById", pid);
	}

	public String getOwner(Integer pid) {
		return (String) getSqlSession().selectOne("getOwner", pid);
	}
	public void deleteExpiredProjects(Integer lifetimeSeconds) {
		Long maxTimestamp = (System.currentTimeMillis()/1000) - lifetimeSeconds;
		getSqlSession().insert("deleteExpiredTempProjects", maxTimestamp);
	}

	public Boolean projectExists(Integer pid) {
		Integer count = (Integer) getSqlSession().selectOne("countOccurences", pid);
		return count.equals(0) ? false : true;
	}

	public Boolean projectExistsAndOwnedByUser(Integer pid, String user) {
		String owner = (String) getSqlSession().selectOne("getOwner", pid);
		return this.projectExists(pid) && owner.equals(user);
	}

	public void updateLastVisited(Integer projectId) {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("id", projectId);
        params.put("lastVisited", System.currentTimeMillis()/1000);
		getSqlSession().insert("updateTempProjectLastVisited", params);
	}

	public void updateProject(TempProject tp) {
		getSqlSession().update("updateTempProject", tp);
	}

	public void deleteProject(Integer projectId) {
		getSqlSession().update("deleteTempProject", projectId);
	}

	public Integer getNextNewProjectId() {
		Integer id = ((Integer)getSqlSession().selectOne("getMinId"));
		if (id == null || id > -1) {
			id = 0;
		}
		return id-1;
	}


}
