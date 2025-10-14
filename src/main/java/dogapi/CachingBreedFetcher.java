package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private int callsMade = 0;
    private BreedFetcher fetcher;
    private HashMap<String, List<String>> cache;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.cache = new HashMap<>(); // New cache
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (cache.containsKey(breed))
            return cache.get(breed); // Index already in cache, no call needed.

        try {
            List<String> subBreeds = fetcher.getSubBreeds(breed);
            // If call is made successfully, add 1 to callsMade,
            // input the new data in the cache, and return the list
            cache.put(breed, subBreeds);
            callsMade++;
            return subBreeds;
        }
        catch (BreedNotFoundException event) {
            System.out.println(event.getMessage());
            return null;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}