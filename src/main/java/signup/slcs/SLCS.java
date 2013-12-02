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
	 * Download the SLCS IdP DN map, generate a Map from it and return the generated map
	 * @return Map
	 * @throws Exception
	 */
	public Map<String,String> getIdpDnMap() throws Exception {
		Map<String,String> m = new HashMap<String,String>();
        URL url = new URL(this.slcsMapUrl);
        URLConnection c = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
        if (br != null) {
            String line;
            while ((line = br.readLine()) != null) {
            	String[] tokens = line.split(",");
            	if (tokens.length != 2) {
            		throw new Exception("Bad SLCS URL, or format error in SLCS Idp Map " + this.slcsMapUrl);
            	}
            	m.put(tokens[0].trim(), tokens[1].trim());
            }        	
        }
        return m;
	}

	/**
	 * Look up a DN
	 * @param idpUrl
	 * @return DN that maps to the specified IdP
	 * @throws Exception
	 */
	public String getDn(String idpUrl) throws Exception {
		Map<String,String> m = this.getIdpDnMap();
		if(m.containsKey(idpUrl)) {
			return m.get(idpUrl);
		} else {
			return null;
		}
	}

	public void setSlcsMapUrl(String slcsMapUrl) {
		this.slcsMapUrl = slcsMapUrl;
	}
}
