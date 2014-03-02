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

    private final String cn = "Martin Feller";
    private final String sharedToken = "g4eVuEfx26afmQq3O3mQdUzdiu8";
    private final String shibIdentityProvider = "http://iam.auckland.ac.nz/idp";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp,
            final FilterChain filterChain) throws IOException, ServletException {

        try {
            final HttpServletRequest request = (HttpServletRequest) req;
            request.setAttribute("shared-token", sharedToken);
            request.setAttribute("Shib-Identity-Provider", shibIdentityProvider);
            request.setAttribute("cn", cn);
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void init(final FilterConfig arg0) throws ServletException {
    }

}
