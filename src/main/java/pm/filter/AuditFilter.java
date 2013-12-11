package pm.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AuditFilter implements Filter {

	private Logger flog = Logger.getLogger("file."+AuditFilter.class.getName()); 
	private Logger log = Logger.getLogger(AuditFilter.class.getName()); 
    private String proxyIp;
    private String remoteUserHeader;
    private String remoteAddrHeader;
    
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) resp;
			String remoteUser = request.getHeader(this.remoteUserHeader);
		    String remoteAddr = request.getHeader(this.remoteAddrHeader);
			
		    // Comment out for testing
		    if (!request.getRemoteAddr().equals(this.proxyIp)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				log.error("Denying access from host " + request.getRemoteAddr() + " (doesn't match " + this.proxyIp + ")");
				return;
			}

		    // Set remoteUser to Tuakiri unique id for testing
		    //if (remoteUser == null) remoteUser = "behat-adviser";
		    if (remoteUser == null || remoteUser.trim().equals("")) {
				log.error("Denying access for anonymous user");
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
		    }
		    request.setAttribute(this.remoteUserHeader, remoteUser);

		    if (remoteAddr == null || remoteAddr.trim().equals("")) {
		    	remoteAddr = request.getRemoteAddr();
		    	if (remoteAddr == null || remoteAddr.trim().equals("")) {
		    		remoteAddr = "n/a";
		    	}
		    }
		    StringBuffer sb = new StringBuffer();
			sb.append("remoteIP=").append(remoteAddr)
			  .append(" ")
			  .append("user=").append(remoteUser)
			  .append(" ")
			  .append("method=").append(request.getMethod())
			  .append(" ")
			  .append("path=").append(request.getPathInfo());
			if (request.getQueryString() != null) {
				sb.append("?").append(request.getQueryString());
			}
			flog.info(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		filterChain.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void destroy() {
	}

	public String getRemoteUserHeader() {
		return remoteUserHeader;
	}

	public void setRemoteUserHeader(String remoteUserHeader) {
		this.remoteUserHeader = remoteUserHeader;
	}

	public String getRemoteAddrHeader() {
		return remoteAddrHeader;
	}

	public void setRemoteAddrHeader(String remoteAddrHeader) {
		this.remoteAddrHeader = remoteAddrHeader;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}
}
