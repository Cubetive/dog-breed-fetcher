package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private static final String API_URL = "https://dog.ceo/api/breed";
    private static final String STATUS_CODE = "status";
    private static final String SUCCESS_CODE = "success";

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        ArrayList<String> subBreedList = new ArrayList<String>();

        final Request request = new Request.Builder()
                .url(String.format("%s/%s/list", API_URL, breed))
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getString(STATUS_CODE).equals(SUCCESS_CODE)) {
                final JSONArray list = responseBody.getJSONArray("message");

                for (int i = 0; i < list.length(); i++) {
                    String subBreed = list.getString(i);
                    subBreedList.add(subBreed);
                }
            }
            else {
                throw new BreedNotFoundException(breed);
            }

        }
        catch (IOException | JSONException e) {
            throw new BreedNotFoundException(breed);
        }

        return subBreedList;
    }
}