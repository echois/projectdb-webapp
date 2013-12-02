package signup.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TestFilter implements Filter {

	private String sharedToken = "g4eVuEfx26afmQq3O3mQdUzdiu8";
	private String shibIdentityProvider = "http://iam.auckland.ac.nz/idp";
	private String cn = "Martin Feller";
    
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

		try {
			HttpServletRequest request = (HttpServletRequest) req;
			request.setAttribute("shared-token", this.sharedToken);
			request.setAttribute("Shib-Identity-Provider", this.shibIdentityProvider);
			request.setAttribute("cn", this.cn);
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

}

