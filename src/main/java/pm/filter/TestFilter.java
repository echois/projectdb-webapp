package pm.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TestFilter implements Filter {

    private String proxyIp;
    private String remoteUserHeader;
    private String remoteAddrHeader;
    
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

		try {
			HttpServletRequest request = (HttpServletRequest) req;
			String remoteUser = "nyou045@auckland.ac.nz";
		    String remoteAddr = request.getHeader(this.remoteAddrHeader);			
		    request.setAttribute(this.remoteUserHeader, remoteUser);

		    if (remoteAddr == null || remoteAddr.trim().equals("")) {
		    	remoteAddr = request.getRemoteAddr();
		    	if (remoteAddr == null || remoteAddr.trim().equals("")) {
		    		remoteAddr = "n/a";
		    	}
		    }
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
