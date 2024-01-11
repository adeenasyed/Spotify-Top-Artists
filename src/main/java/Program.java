import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Program {

    public static final String SONG_DATA_FILENAME = "charts.csv";

    /**
     * This constant will allow you to either read the data into
     * local memory (_your_ code will be faster), or to use persistent
     * storage (_your_ code will be slower, but uses a smaller
     * amount of memory).
     */
    public static final boolean READ_INTO_MEMORY = true;

    /**
     * This constant will allow you to display the header
     * information for the data files (i.e., what columns have
     * what names). Set this to false before handing in the
     * assignment.
     */
    // TODO: adjust this constant as necessary
    public static final boolean SHOW_HEADERS = false;

    /**
     * When testing, you may want to choose a smaller portion of
     * the dataset. This number lets you limit it to only the
     * first MAX_ENTRIES. Setting this over 3.5 million will get
     * all the data. IMPORTANT NOTE: this will ONLY have an
     * effect when READ_INTO_MEMORAY is true
     */
    // TODO: adjust this constant as necessary
    public static final int DEFAULT_MAX_SONGS = Integer.MAX_VALUE;
    public static int max_songs = DEFAULT_MAX_SONGS;



    public static final int DEFAULT_N_ARTISTS = 20;

    /**
     * This constant represents the folder that contains the
     * data. You should not have to adjust this.
     */
    public static final String DATA_DIRECTORY = "data";

    /**
     * This static field manages timing in your code. You can
     * and should reuse it to time your code.
     */
    public static Stopwatch stopwatch = new Stopwatch();
    
    
    /**
     * CSV Record constants
     */
    private static final String ARTIST_VARIABLE_NAME = "artists";

    /**
     * The main method of your program. The first half of this
     * is written for you (don't adjust this!). Where it says
     * "Add your code here..." you should put your main program
     * (remember to use methods where appropriate).
     * 
     * @param args the arguments to this main program provided
     *             on the command line (none)
     * @throws IOException when the data files cannot be read
     *                     properly
     */
    public static void main(String[] args) throws IOException {
        /*
         * We have already setup all the data to make the rest of
         * your assignment easier for you.
         * 
         * All of the data is available in an Iterable object. While
         * you may not know what an Iterable object is, you've
         * already used these many times before! ArrayList,
         * LinkedList, the keySet for a HashMap, etc. are all
         * Iterable objects. To use them, you just need to use a for
         * each loop.
         * 
         * IMPORTANT NOTE: the big difference between what you might
         * be used to and the Iterable is that YOU CAN ONLY ITERATE
         * OVER IT ONE TIME. Once you've done that, the Iterable
         * object will be "exhausted". Therefore, make sure in that
         * first pass that you store the information you need in the
         * appropriate Map object (as per the assignment
         * instructions).
         */
        Path dir = Paths.get(DATA_DIRECTORY);

        

        /*
         ************************ IMPORTANT *********************
         * 
         * NOTE: You should NOT change anything about this argument processing
         * or data importing. You should only read stats from index 0 to index
         * list.size() - 1 Do NOT sort the list or change them in any way.
         * 
         ********************************************************/


        //this program requires an argument
        if(args.length == 0) {
                //no argument found, print usage
                System.out.println("ERROR: No arguments provided.");
                printUsage();
        } else {
                //arguments found, process them and go through main program logic

                //read in the first argument: strategy (list, tree, or hash)
                String strategy = args[0].trim().toLowerCase();

                //read in the second argument if provided (top n to print out)
                int nArtists = DEFAULT_N_ARTISTS;
                if(args.length >= 2) {
                        nArtists = Integer.parseInt(args[1]);
                }

                //read in the third argument if provided (number of song entries)
                if(args.length >= 3) {
                        max_songs = Integer.parseInt(args[2]);
                }


                //read in data from data file
                Iterable<CSVRecord> songData = readData(
                dir.resolve(SONG_DATA_FILENAME), "song chart entries",
                READ_INTO_MEMORY, false);


                //apply the appropriate strategy
                if(strategy.equals("list")) {
                        findTopUsingArrayList(songData, nArtists);
                } else if (strategy.equals("tree")) {
                        findTopUsingTreeMap(songData, nArtists);
                } else if (strategy.equals("hash")) {
                        findTopUsingHashMap(songData, nArtists);
                } else {
                        //error: strategy incorrect. Print usage
                        System.out.println("ERROR: \""+args[0]+"\"" + "not recognized as a strategy.");
                        printUsage();
                }


        }

        
    }



    /**
     * TODO: Add your code for unsorted array list here. You
     * will do best to create many methods to organize your work
     * and experiments.
     * 
     * @param allSongs the Iterable object that lets you
     *                  iterate over all of the data
     * @param n         the number of users to report the top N
     *                  for
     */
    public static void findTopUsingArrayList(
            Iterable<CSVRecord> allSongs, int n) {
 
        int numEntries = 0;
        int numMissing = 0;
        ArrayList<ArtistCount> list = new ArrayList<>(); // creates ArrayList of type ArtistCount
        stopwatch.reset(); 
        stopwatch.start();
 
        for (CSVRecord record : allSongs) { // iterates through every line in CSV file
            numEntries++;
                if (record.isSet(ARTIST_VARIABLE_NAME))
                {
                        String[] artists = parseRawArtistList(record.get(ARTIST_VARIABLE_NAME)); // creates array of type String and adds artists of current song to array
                        //System.out.println("For song \"" + record.get("name") + "\":");
                        for(int i = 0; i < artists.length; i++) { // iterates through array created in previous line
                                //System.out.printf("\tEncountered artist %s.\n", artists[i]);
                                ArtistCount x = new ArtistCount(artists[i], 1); // creates new ArtistCount object with artist name and sets count to 1
                                if (list.contains(x)) { // checks if ArrayList contains ArtistCount object created in previous line
                                        list.get(list.indexOf(x)).increment(); // increments count of ArtistCount object if ArrayList contains object
                                } else {
                                        list.add(x); // adds object to ArrayList if ArrayList does not contain object
                                }  
                        }
                        //System.out.println();
                } else {
                        numMissing++;
                }
        }
 
        //System.out.printf("There are %,d entries in total, with %,d missing artists.\n",
                //numEntries, numMissing);

        System.out.printf("ArrayList took %.6f seconds to process %d entries.", stopwatch.getElapsedSeconds(), numEntries);
        System.out.println();

        Collections.sort(list, Collections.reverseOrder()); // sorts ArrayList in ascending order using counts of ArtistCount objects

        for (int i = 0; i < n; i++) { // iterates n times to print n top artists 
                System.out.println(list.get(i).getArtistName() + " " + list.get(i).getCount());  // retrieves and prints top artist and count from sorted list
        }
    }


    /**
     * TODO: Add your code for tree maps here. You will do best
     * to create many methods to organize your work and
     * experiments.
     * 
     * @param allSongs the Iterable object that lets you
     *                  iterate over all of the data
     * @param n         the number of users to report the top N
     *                  for
     */
    public static void findTopUsingTreeMap(
            Iterable<CSVRecord> allSongs, int n) { 

        int numEntries = 0;
        TreeMap<String, ArtistCount> treemap = new TreeMap<>(); // creates TreeMap of type <String, ArtistCount>
        stopwatch.reset(); 
        stopwatch.start();

        for (CSVRecord record : allSongs) { // iterates through every line in CSV file
                numEntries++;
        	if (record.isSet(ARTIST_VARIABLE_NAME))
        	{
                        String[] artists = parseRawArtistList(record.get(ARTIST_VARIABLE_NAME)); // creates array of type String and adds artists of current song to array
                        for(int i = 0; i < artists.length; i++) { // iterates through array created in previous line
                                String artist = artists[i]; // creates string with artist name 
                                ArtistCount x = new ArtistCount(artists[i], 1); // creates new ArtistCount object with artist name and sets count to 1
                                if (treemap.containsKey(artist)) { // checks if TreeMap contains ArtistCount object created in previous line
                                        treemap.get(artist).increment(); // increments count of ArtistCount object if TreeMap contains object
                                } else { 
                                        treemap.put(artist, x); // adds object to TreeMap if TreeMap does not contain object
                                }  
                        }
        	}   
        }

        System.out.printf("TreeMap took %.6f seconds to process %d entries.", stopwatch.getElapsedSeconds(), numEntries);
        System.out.println();

        for (int i = 1; i <= n; i++) { // iterates n times to print n top artists 
                mapSort(treemap); // retrieves and prints top artist and count
        }

    }

    /**
     * TODO: Add your code for hash maps here. You will do best
     * to create many methods to organize your work and
     * experiments.
     * 
     * @param allSongs the Iterable object that lets you
     *                  iterate over all of the data
     * @param n         the number of users to report the top N
     *                  for
     */
    public static void findTopUsingHashMap(
            Iterable<CSVRecord> allSongs, int n) {

        int numEntries = 0;
        HashMap<String, ArtistCount> hashmap = new HashMap<>(); // creates HashMap of type <String, ArtistCount>
        stopwatch.reset(); 
        stopwatch.start();

        for (CSVRecord record : allSongs) { // iterates through every line in CSV file
                numEntries++;
        	if (record.isSet(ARTIST_VARIABLE_NAME))
        	{
                        String[] artists = parseRawArtistList(record.get(ARTIST_VARIABLE_NAME)); // creates array of type String and adds artists of current song to array
                        for(int i = 0; i < artists.length; i++) { // iterates through array created in previous line
                                String artist = artists[i]; // creates string with artist name
                                ArtistCount x = new ArtistCount(artists[i], 1); // creates new ArtistCount object with artist name and sets count to 1
                                if (hashmap.containsKey(artist)) { // checks if HashMap contains ArtistCount object created in previous line
                                        hashmap.get(artist).increment(); // increments count of ArtistCount object if HashMap contains object
                                } else { 
                                        hashmap.put(artist, x); // adds object to HashMap if HashMap does not contain object
                                }  
                        }
        	}   
        }

        System.out.printf("HashMap took %.6f seconds to process %d entries.", stopwatch.getElapsedSeconds(), numEntries);
        System.out.println();

        for (int i = 1; i <= n; i++) { // iterates n times to print n top artists 
                mapSort(hashmap); // retrieves and prints top artist and count
        }
    }

    public static void mapSort(Map<String, ArtistCount> map) { 
        int highestCount = 0; // variable of type int to store highest count
        String key = ""; // string to store name of artist with highest count
        for (String artist: map.keySet()) { // iterates through every key in map
                if (map.get(artist).getCount() > highestCount) { // checks if ArtistCount object count is higher than the highest count (using key to access ArtistCount object)
                        highestCount = map.get(artist).getCount(); // sets highest count to current ArtistCount object count
                        key = artist; // sets key to current ArtistCount object artist name
                }
        }
        map.remove(key); // removes key mapped to highest count from map 
        System.out.println(key + " " + highestCount); // prints top artist and count
    }

    /**
     * YOU SHOULD NOT CHANGE THIS METHOD.
     * 
     * This method reads the data into an Iterable object.
     * 
     * @param path           the path of the file to read from
     * @param description    the description to use when
     *                       reporting the type of data
     * @param readIntoMemory true if the data should be read
     *                       into memory (it takes a lot of
     *                       memory!), false if the Iterable
     *                       object should just go through the
     *                       file.
     * @param printHeader    true if this method should print
     *                       the header information (i.e., which
     *                       column has what name).
     * @return an Iterable object with all of the data in
     *         CSVRecord objects
     * @throws IOException if the file could not be read
     */
    public static Iterable<CSVRecord> readData(Path path,
            String description, boolean readIntoMemory,
            boolean printHeader) throws IOException {
        stopwatch.reset();
        stopwatch.start();

        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .parse(new FileReader(path.toFile()));
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Iterable<CSVRecord> iterable = () -> parser.iterator();

        if (readIntoMemory) {
            List<CSVRecord> list2 = StreamSupport
                    .stream(iterable.spliterator(), false)
                    .limit(max_songs).collect(Collectors.toList());

            System.out.printf(
                    "Finished reading %,d %s into memory in %f seconds.\n",
                    list2.size(), description,
                    stopwatch.getElapsedSeconds());

            iterable = list2;
        }

        if (printHeader) {
            System.out.println("Data available:");
            for (String key : headerMap.keySet()) {
                int value = headerMap.get(key);
                System.out.printf("\t%d = %s\n", value, key);
            }
        }

        return iterable;
    }


    /**
     * YOU SHOULD NOT CHANGE THIS METHOD.
     * 
     * printUsage
     * 
     * Prints out the usage instructions to standard out
     */
    public static void printUsage() {
        System.out.println("USE: \'gradle run --args \"STRATEGY N_ARTISTS MAX_SONGS\"\'");
        System.out.println("\twhere:");
        System.out.println("\t\tSTRATEGY is one of \"list\", \"tree\", \"hash\" (required)");
        System.out.println("\t\tN_ARTISTS is an integer specifying the number of songs used in the analysis (optional, 20)");
        System.out.println("\t\tMAX_SONGS is an integer specifying the number of songs used in the analysis (optional, defaults to the entire list)");
        System.out.println("\texample:");
        System.out.println("\t\tgradle run --args \"hash 20 50000\"");
    }


    /**
     * YOU SHOULD NOT CHANGE THIS METHOD.
     * 
     * parseRawArtistList
     * 
     * Helper function to handle a list of artists in the CSV file
     * 
     * @param rawArtistList a string in the format "['ARTIST1', 'ARTIST2', 'ARTIST3']"
     * @return an array of strings representing the full set of artist names (e.g., {"ARTIST1", "ARTIST2", "ARTIST3"})
     */
    private static String[] parseRawArtistList(String rawArtistList) {
        int rawArtistNameLength = rawArtistList.length();
        return rawArtistList.substring(2, rawArtistNameLength-2).split("\', \'");
    }
}
