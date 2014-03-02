package signup.slcs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class SLCS {

    private String slcsMapUrl;

    /**
     * Create a DN of a user as the DN of a credential generated by SLCS
     * 
     * @param idpUrl
     * @param fullName
     * @param token
     * @return user DN
     * @throws Exception
     */
    public String createUserDn(final String idpUrl, final String fullName,
            final String token) throws Exception {
        String baseDn = getDn(idpUrl);
        if (idpUrl.contains("iam.auckland.ac.nz/idp")) {
            baseDn = "/DC=nz/DC=org/DC=bestgrid/DC=slcs/O=The University of Auckland";
        }
        if (baseDn == null || baseDn.isEmpty()) {
            throw new Exception(
                    "Failed to create user DN: idpUrl not found in ACL map.");
        }
        return baseDn + "/CN=" + fullName + " " + token;
    }

    /**
     * Look up a DN
     * 
     * @param idpUrl
     * @return DN that maps to the specified IdP
     * @throws Exception
     */
    public String getDn(final String idpUrl) throws Exception {
        final Map<String, String> m = getIdpDnMap();
        if (m.containsKey(idpUrl)) {
            return m.get(idpUrl);
        } else {
            return null;
        }
    }

    /**
     * Download the SLCS IdP DN map, generate a Map from it and return the
     * generated map
     * 
     * @return Map
     * @throws Exception
     */
    public Map<String, String> getIdpDnMap() throws Exception {
        final Map<String, String> m = new HashMap<String, String>();
        final URL url = new URL(slcsMapUrl);
        final URLConnection c = url.openConnection();
        final BufferedReader br = new BufferedReader(new InputStreamReader(
                c.getInputStream()));
        if (br != null) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] tokens = line.split(",");
                if (tokens.length != 2) {
                    throw new Exception(
                            "Bad SLCS URL, or format error in SLCS Idp Map "
                                    + slcsMapUrl);
                }
                m.put(tokens[0].trim(), tokens[1].trim());
            }
        }
        return m;
    }

    public void setSlcsMapUrl(final String slcsMapUrl) {
        this.slcsMapUrl = slcsMapUrl;
    }
}
