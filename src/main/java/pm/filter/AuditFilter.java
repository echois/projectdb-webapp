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

    private final Logger flog = Logger.getLogger("file."
            + AuditFilter.class.getName());
    private final Logger log = Logger.getLogger(AuditFilter.class.getName());
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
            final HttpServletResponse response = (HttpServletResponse) resp;
            String remoteUser = request.getHeader(remoteUserHeader);
            String remoteAddr = request.getHeader(remoteAddrHeader);

            // Comment out for testing
            if (!request.getRemoteAddr().equals(proxyIp)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.error("Denying access from host " + request.getRemoteAddr()
                        + " (doesn't match " + proxyIp + ")");
                return;
            }

            // Set remoteUser to Tuakiri unique id for testing
            if (remoteUser == null) {
                remoteUser = "behat-admin";
            }
            if (remoteUser == null || remoteUser.trim().equals("")) {
                log.error("Denying access for anonymous user");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            request.setAttribute(remoteUserHeader, remoteUser);

            if (remoteAddr == null || remoteAddr.trim().equals("")) {
                remoteAddr = request.getRemoteAddr();
                if (remoteAddr == null || remoteAddr.trim().equals("")) {
                    remoteAddr = "n/a";
                }
            }
            final StringBuffer sb = new StringBuffer();
            sb.append("remoteIP=").append(remoteAddr).append(" ")
                    .append("user=").append(remoteUser).append(" ")
                    .append("method=").append(request.getMethod()).append(" ")
                    .append("path=").append(request.getPathInfo());
            if (request.getQueryString() != null) {
                sb.append("?").append(request.getQueryString());
            }
            flog.info(sb.toString());
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
