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
    private String remoteAddrHeader;
    private String remoteUserHeader;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp,
            final FilterChain filterChain) throws IOException, ServletException {

        try {
            final HttpServletRequest request = (HttpServletRequest) req;
            final String remoteUser = "nyou045@auckland.ac.nz";
            String remoteAddr = request.getHeader(remoteAddrHeader);
            request.setAttribute(remoteUserHeader, remoteUser);

            if (remoteAddr == null || remoteAddr.trim().equals("")) {
                remoteAddr = request.getRemoteAddr();
                if (remoteAddr == null || remoteAddr.trim().equals("")) {
                    remoteAddr = "n/a";
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        filterChain.doFilter(req, resp);
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public String getRemoteAddrHeader() {
        return remoteAddrHeader;
    }

    public String getRemoteUserHeader() {
        return remoteUserHeader;
    }

    @Override
    public void init(final FilterConfig arg0) throws ServletException {
    }

    public void setProxyIp(final String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public void setRemoteAddrHeader(final String remoteAddrHeader) {
        this.remoteAddrHeader = remoteAddrHeader;
    }

    public void setRemoteUserHeader(final String remoteUserHeader) {
        this.remoteUserHeader = remoteUserHeader;
    }
}
