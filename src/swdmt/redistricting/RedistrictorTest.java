package swdmt.redistricting;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
/**
 * Tests for redistrictor.
 *
 * @author  Dr. Jody Paul
 * @version 20191006
 */
public class RedistrictorTest {
    /**
     * Default constructor for test class RedistrictorTest.
     */
    public RedistrictorTest() {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRegionParameterConstructorTest() {
        Redistrictor r = new Redistrictor(null);
    }

    @Test
    public void generateDistrictsSquareSingleDistrictTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertThat(districtSet.iterator().next().size(), is(0));
        region = new Region(1);
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertThat(districtSet.iterator().next().size(), is(1));
        region = new Region(4);
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertThat(districtSet.iterator().next().size(), is(4));
        region = new Region(9);
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertThat(districtSet.iterator().next().size(), is(9));
        region = new Region(16);
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertThat(districtSet.iterator().next().size(), is(16));
    }

    /**
     * Checks generation of districts for a square region,
     * verifying only the number of districts, the number of
     * locations per district, and the uniqueness of locations
     * per district.
     * N.B. The contiguity of locations in each district is NOT
     * verified.
     */
    @Test
    public void generateDistrictsSquareAppropriateNumberAndSizeTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertTrue(locationsUnique(districtSet));
        assertThat(districtSet.iterator().next().size(), is(0));

        region = new Region(1);
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertTrue(locationsUnique(districtSet));
        assertThat(districtSet.iterator().next().size(), is(1));

        region = new Region(4);
        districtSet = Redistrictor.generateDistricts(region, 2);
        assertThat(districtSet.size(), is(2));
        assertTrue(locationsUnique(districtSet));
        assertThat(districtSet.iterator().next().size(), is(2));

        region = new Region(9);
        districtSet = Redistrictor.generateDistricts(region, 1);
        assertThat(districtSet.size(), is(1));
        assertTrue(locationsUnique(districtSet));
        assertThat(districtSet.iterator().next().size(), is(9));

        districtSet = Redistrictor.generateDistricts(region, 3);
        assertThat(districtSet.size(), is(3));
        assertTrue(locationsUnique(districtSet));
        assertThat(districtSet.iterator().next().size(), is(3));
        for (District d : districtSet) {
            assertThat(d.size(), is(3));
        }

        districtSet = Redistrictor.generateDistricts(region, 2);
        assertThat(districtSet.size(), is(2));
        assertTrue(locationsUnique(districtSet));
        for (District d : districtSet) {
            assertThat(d.size(), anyOf(is(4), is(5)));
        }
        int numLocations = 0;
        for (District d : districtSet) {
            numLocations += d.size();
        }
        assertThat(numLocations, is(9));

        districtSet = Redistrictor.generateDistricts(region, 4);
        assertThat(districtSet.size(), is(4));
        assertTrue(locationsUnique(districtSet));
        for (District d : districtSet) {
            assertThat(d.size(), anyOf(is(2), is(3)));
        }
        numLocations = 0;
        for (District d : districtSet) {
            numLocations += d.size();
        }
        assertThat(numLocations, is(9));

        districtSet = Redistrictor.generateDistricts(region, 5);
        assertThat(districtSet.size(), is(5));
        assertTrue(locationsUnique(districtSet));
        for (District d : districtSet) {
            assertThat(d.size(), anyOf(is(1), is(2)));
        }
        numLocations = 0;
        for (District d : districtSet) {
            numLocations += d.size();
        }
        assertThat(numLocations, is(9));
    }

    /**
     * Utility to verify that all locations in a collection
     * of districts are unique; that is, no two districts
     * contain the same location.
     * @param districts the districts to consider
     * @return true if all locations are unique; false otherwise
     */
    private static boolean locationsUnique(final Set<District> districts) {
        boolean allUnique = true;
        for (District d : districts) {
            for (Location loc : d.locations()) {
                for (District other : districts) {
                    if (d != other) {
                        allUnique &= !other.locations().contains(loc);
                    }
                }
            }
        }
        return allUnique;
    }

    /**
     * Checks cases where a single district is the
     * appropriate response for all districts of a specified size.
     */
    @Test
    public void allDistrictsOfSpecificSizeSingleDistrictTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 1).size(), is(0));
        region = new Region(1);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 1).size(), is(1));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(1));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(1));
        region = new Region(4);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(1));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 9).size(), is(1));
    }

    @Test
    public void allDistrictsOfSpecificSizeTest() {
        Region region;
        Set<District> districtSet;
        region = new Region(4);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(4));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(4));
        region = new Region(9);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(12));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(22));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(28));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 8).size(), is(5));
        region = new Region(16);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(24));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(52));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(89));
        region = new Region(25);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(40));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(94));
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(180));
        region = new Region(64);
        assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(112));
    }

    /**
     * Checks generation of districts for a square region,
     * verifying only the contiguity of locations in each
     * district.
     */
    @Test
    public void generateDistrictsSquareContiguityTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        districtSet = Redistrictor.generateDistricts(region, 1);
        for (District d : districtSet) {
            assertTrue("Checking contiguity of district " + d, d.contiguityValid());
        }
        region = new Region(1);
        districtSet = Redistrictor.generateDistricts(region, 1);
        for (District d : districtSet) {
            assertTrue("Checking contiguity of district " + d, d.contiguityValid());
        }

        region = new Region(4);
        districtSet = Redistrictor.generateDistricts(region, 2);
        for (District d : districtSet) {
            assertTrue("Checking contiguity of district " + d, d.contiguityValid());
        }

        region = new Region(9);
        districtSet = Redistrictor.generateDistricts(region, 1);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 2);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 3);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 4);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 5);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
    }
  
    /**
     * Tests the normal behavior of subDivideGraph,
     * in this example, subDivideGraph should find
     * two distinct contiguous sets of locations.
     * The first set contains locations A,B, and C
     * while the second set contains D, E, and F.
     */
    @Test
    public void subDivideGraphBasicTest() {
    	//Creating locations by hand
    	TreeSet<Location> locations = new TreeSet<Location>();
    	Location locationA = new Location(0,0);
    	locations.add(locationA);
    	Location locationB = new Location(1,0);
    	locations.add(locationB);
    	Location locationC = new Location(-1,0);
    	locations.add(locationC);
    	Location locationD = new Location(4,0);
    	locations.add(locationD);
    	Location locationE = new Location(5,0);
    	locations.add(locationE);
    	Location locationF = new Location(4,1);
    	locations.add(locationF);
    	
    	//Creating the graph by hand
    	HashMap<Location, HashSet<Location>> graph = new HashMap<Location, HashSet<Location>>();
    	HashSet<Location> locationAConnections = new HashSet<Location>();
    	locationAConnections.add(locationB);
    	locationAConnections.add(locationC);
    	graph.put(locationA, locationAConnections);
    	HashSet<Location> locationBConnections = new HashSet<Location>();
    	locationBConnections.add(locationA);
    	graph.put(locationB, locationBConnections);
    	HashSet<Location> locationCConnections = new HashSet<Location>();
    	locationCConnections.add(locationA);
    	graph.put(locationC, locationCConnections);
    	HashSet<Location> locationDConnections = new HashSet<Location>();
    	locationDConnections.add(locationE);
    	locationDConnections.add(locationF);
    	graph.put(locationD, locationDConnections);
    	HashSet<Location> locationEConnections = new HashSet<Location>();
    	locationEConnections.add(locationD);
    	graph.put(locationE, locationEConnections);
    	HashSet<Location> locationFConnections = new HashSet<Location>();
    	locationFConnections.add(locationD);
    	graph.put(locationF, locationFConnections);
    	
    	//Creating the subgraphs by hand
    	HashSet<HashSet<Location>> subGraphs = new HashSet<HashSet<Location>>();
    	HashSet<Location> subGraphA = new HashSet<Location>();
    	subGraphA.add(locationA);
    	subGraphA.add(locationB);
    	subGraphA.add(locationC);
    	HashSet<Location> subGraphB = new HashSet<Location>();
    	subGraphB.add(locationD);
    	subGraphB.add(locationE);
    	subGraphB.add(locationF);
    	subGraphs.add(subGraphA);
    	subGraphs.add(subGraphB);
    	
    	assertTrue(Redistrictor.subDivideGraph(graph, locations).equals(subGraphs));	
    }
    
    /**
     * Tests to see that subDivideGraph will return an empty
     * set if the input graph is empty.
     */
    @Test
    public void subDivideGraphEmptyTest() {
    	TreeSet<Location> locations = new TreeSet<Location>();
    	HashMap<Location, HashSet<Location>> graph = new HashMap<Location, HashSet<Location>>();
    	HashSet<HashSet<Location>> subGraphs = new HashSet<HashSet<Location>>();
    	assertTrue(Redistrictor.subDivideGraph(graph, locations).equals(subGraphs));
    }
    
    /**
     * Tests to see that subDivideGraph will return a set
     * containing all locations in a graph if the graph is
     * contiguous.
     */
    @Test
    public void subDivideGraphOneRegionTest() {
    	TreeSet<Location> locations = new TreeSet<Location>();
    	Location locationA = new Location(0,0);
    	locations.add(locationA);
    	Location locationB = new Location(1,0);
    	locations.add(locationB);
    	Location locationC = new Location(-1,0);
    	locations.add(locationC);
    	
    	HashMap<Location, HashSet<Location>> graph = new HashMap<Location, HashSet<Location>>();
    	HashSet<Location> locationAConnections = new HashSet<Location>();
    	locationAConnections.add(locationB);
    	locationAConnections.add(locationC);
    	graph.put(locationA, locationAConnections);
    	HashSet<Location> locationBConnections = new HashSet<Location>();
    	locationBConnections.add(locationA);
    	graph.put(locationB, locationBConnections);
    	HashSet<Location> locationCConnections = new HashSet<Location>();
    	locationCConnections.add(locationA);
    	graph.put(locationC, locationCConnections);
    	
    	HashSet<HashSet<Location>> subGraphs = new HashSet<HashSet<Location>>();
    	HashSet<Location> subGraphA = new HashSet<Location>();
    	subGraphA.add(locationA);
    	subGraphA.add(locationB);
    	subGraphA.add(locationC);
    	subGraphs.add(subGraphA);
    	
    	assertTrue(Redistrictor.subDivideGraph(graph, locations).equals(subGraphs));
    }
  
    /**
     * Test the generateGraphFromRegion method to verify it is
     * generating a HashMap object correctly.
     */
    @Test
    public void generateGraphFromRegionTest() {
        Region region = new Region(9);
        HashMap<Location, HashSet<Location>> map = Redistrictor.generateGraphFromRegion(region);
        Location tempLocation = new Location(0,0);
        Location testLocaiton1 = new Location(1,0);
        Location testLocaiton2 = new Location(0,1);
        HashSet<Location> testConnections1 = map.get(tempLocation);

        assertTrue(testConnections1.size() == 2);
        assertTrue(testConnections1.contains(testLocaiton1));
        assertTrue(testConnections1.contains(testLocaiton2));
    }
}

