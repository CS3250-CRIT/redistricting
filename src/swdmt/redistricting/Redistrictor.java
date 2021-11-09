package swdmt.redistricting;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * A redistrictor attempts to determine a set of districts
 * for a given region under specified constraints.
 * Typical constraints may include:
 * achieving a specified number of districts,
 * achieving the same number of voters per district,
 * creating intra-district parity by party,
 * creating region-level parity by party,
 * favoring a party at region-level.
 *
 * Most functionality is available via utility methods.
 *
 * @author Dr. Jody Paul
 * @version 20191201
 */
public final class Redistrictor implements java.io.Serializable {
    /** Serialization version requirement. */
    private static final long serialVersionUID = 3L;

    /** Region associated with this redistrictor. */
    private Region region;

    /**
     * Establishes a specific region as associated with
     * the redistrictor.
     * @param theRegion region associated with this redistrictor
     * @throws illegalArgumentException if the region is null
     */
    public Redistrictor(final Region theRegion) {
        if (null == theRegion) {
            throw new IllegalArgumentException(
                    "Cannot associate null region with new Redistrictor");
        }
        this.region = theRegion;
    }

    /**
     * Accesses this redistrictor's region.
     * @return the region
     */
    public Region region() {
        return this.region;
    }

    /**
     * Utility: Apply a generate-and-test algorithm to search for
     * any feasible redistricting solution.
     * @TODO Contiguity for non rectangluar districts are NOT yet considered!
     * @param theRegion the region to be redistricted
     * @param numDistricts the number of districts for the region;
     *        defaults to 1 if value is less-than or equal to 0
     * @return a set of districts matching the parameters, if feasible;
     *         an empty set if no feasible solution is found.
     */
    public static Set<District> generateDistricts(final Region theRegion,
                                                  final int numDistricts) {
        Set<District> districts = new HashSet<District>();
        List<List<Location>> districtLocs = new ArrayList<List<Location>>();
        int numberOfDistricts = (numDistricts < 1) ? 1 : numDistricts;
        int minimumNumberOfVotersPerDistrict
                = theRegion.numberOfVoters() / numDistricts;
        int numberOfAugmentedDistricts
                = theRegion.numberOfVoters() % numDistricts;
        Iterator<Location> locit = theRegion.locations().iterator();

        Location[] snakingLocations =
          new Location[theRegion.locations().size()];
        for (int i = 0; i < theRegion.locations().size(); i++) {
          snakingLocations[i] = locit.next();
        }

        Arrays.sort(snakingLocations, new SnakingLocationComparer());

        int currentLocation = 0;

        // Create covering districts with the proper
        // number of locations.
        // TODO: Contiguity for non rectangluar districts
        // are NOT yet considered.
        for (int i = 0; i < numberOfDistricts; i++) {
            List<Location> locList = new ArrayList<Location>();
            for (int vi = 0; vi < minimumNumberOfVotersPerDistrict; vi++) {
              locList.add(snakingLocations[currentLocation++]);
            }
            if (i < numberOfAugmentedDistricts) {
              locList.add(snakingLocations[currentLocation++]);
            }

            districtLocs.add(new ArrayList<Location>(locList));
        }

        for (List<Location> locs : districtLocs) {
            districts.add(new District(locs));
        }
        return districts;
    }

    /**
     * Utility: Generates all possible districts of the
     * specified size from a given region.
     * If the region is smaller than the specified size,
     * then a single district is returned.
     * Otherwise, creates a set of all districts of
     * approximately equal size; that is, each district's
     * size is within ±1 of the district size parameter.
     * @param theRegion the region
     * @param districtSize the size of the districts
     * @return a set of all districts of the specified size
     *     within a tolerance of ±1
     */
    public static Set<District> allDistrictsOfSpecificSize(
                                    final Region theRegion,
                                    final int districtSize) {
        Set<District> districts = new HashSet<District>();
        if (districtSize > 0 && theRegion.size() > 0) {
            if (theRegion.size() <= districtSize) {
                districts.add(new District(theRegion.locations()));
            } else if (districtSize == 1) {
                for (Location loc : theRegion.locations()) {
                    List<Location> locList = new ArrayList<Location>(1);
                    locList.add(loc);
                    districts.add(new District(locList));
                }
            } else {
              int size = theRegion.sideSize();
              ArrayList<District> allDistricts =
                AllDistrictGen.generateDistricts(size,
                                                 size,
                                                 districtSize);
              for (District d : allDistricts) {
                districts.add(d);
              }
            }
        }
        return districts;
    }

    /**
     * Utility: Iterator over all districts of the specified size
     * for a given region.
     * @param theRegion the region
     * @param districtSize the size of the districts
     * @return a set of all districts of the specified size
     */
    public static Iterator<District> allDistrictsOfSpecificSizeIterator(
                                    final Region theRegion,
                                    final int districtSize) {
        return allDistrictsOfSpecificSize(theRegion, districtSize).iterator();
    }
    
    /**
     * Creates a graph out of a given region,
     * using locations as vertices and connecting
     * locations to any other location with a position
     * which is ((x + 1) OR (x - 1)) XOR ((y + 1) OR (y - 1))
     * from itself.
     * <p>
     * Returns the graph in the form of a HashMap with locations
     * as keys and the set of locations they are connected to
     * as values.
     * @TODO Implement functionality as described
     * @param input The region to generate a graph from.
     * @return A HashMap of locations, with values of the locations 
     *         they are connected to.
     */
    protected HashMap<Location, HashSet<Location>> generateGraphFromRegion(final Region input){
    	return null;
    }
    
    /**
     * Returns a set of all contiguous sub graphs of
     * a given graph. In other words, divides up a graph
     * into its individual contiguous components. Therefore
     * it will return a set containing only the original graph
     * if the entire original graph is contiguous.
     * @TODO Implement functionality as described
     * @param input The graph to be subdivided
     * @return A set containing all contiguous sub-graphs within 
     *         the input graph.
     */
    protected HashSet<HashMap<Location, HashSet<Location>>> subDivideGraph(
    									final HashMap<Location, HashSet<Location>> input){
    	return null;
    }
    
    /**
     * Returns a copy of the input graph which has all
     * specified locations removed from it. Functionally
     * subtracts a selection of locations from the graph
     * and returns the result. Any connections to the 
     * specified locations will also be removed.
     * @TODO Implement functionality as described
     * @param inputGraph The graph to perform the removal operation on. 
     * 		  This method does not mutate the original object.
     * @param removeList The set of locations to be removed
     * @return A new graph with specified locations removed
     */
    protected HashMap<Location, HashSet<Location>> removeLocationsFromGraph(
    									final HashMap<Location, HashSet<Location>> inputGraph, 
    									final Set<Location> removeList){
    	return null;
    }
    
    /**
     * Checks to see if it is hypothetically possible 
     * for a given graph to be divided into districts
     * of a specified size. 
     * <p>
     * Functionally, this means that all contiguous
     * subgraphs of the input graph possess a number
     * of locations which is divisible by the 
     * specified district size.
     * @TODO Implement functionality as described
     * @param input The graph to check for possible redistrictings.
     * @param districtSize The size of the desired districts
     * @return True if all contiguous subgraphs of input graph are divisible by districtSize
     */
    protected boolean redistrictingPossible(final HashMap<Location, HashSet<Location>> input, 
    									final int districtSize) {
    	return false;
    }
}
