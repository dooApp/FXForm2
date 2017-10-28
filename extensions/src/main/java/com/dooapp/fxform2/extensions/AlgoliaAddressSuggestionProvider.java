package com.dooapp.fxform2.extensions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 27/10/2017
 * Time: 15:17
 */
public class AlgoliaAddressSuggestionProvider implements AddressSuggestionProvider {

    public final static Logger logger = Logger.getLogger(AlgoliaAddressSuggestionProvider.class.getName());
    
    public static final String ALGOLIA_QUERY_URL = "https://places-dsn.algolia.net/1/places/query";

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Collection<Address> getSuggestions(String address) {
        try {
            String response = executeQuery(address);
            JsonNode node = mapper.readTree(response).get("hits");
            List result = new ArrayList(node.size());
            for (int i = 0; i < node.size(); i++) {
                JsonNode hit = node.get(i);
                result.add(new Address(hit.get("locale_names").get("default").get(0).asText(),
                        hit.get("city").get("default").get(0).asText(),
                        hit.get("postcode").get(0).asText()));
            }
            return result;
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Collections.emptyList();

    }

    private String executeQuery(String address) throws UnsupportedEncodingException {
        String urlParameters = "{\"query\": \"" + address + "\"}";
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(ALGOLIA_QUERY_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(urlParameters);
            outputStreamWriter.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
