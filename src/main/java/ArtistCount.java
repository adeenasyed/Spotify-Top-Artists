import java.util.Objects;

public class ArtistCount implements Comparable<ArtistCount> {

    /**
     * The name of the Spotify artist.
     */
    private String artistName;

    /**
     * The count of song entries by the user.
     */
    private int count;

    /**
     * Creates a new ArtistCount object with the given screen
     * name and count.
     * 
     * @param artistName the artist name
     * @param count      the initial count of charted songs by that
     *                   user.
     */
    public ArtistCount(String artistName, int count) {
        this.artistName = artistName;
        this.count = count;
    }

    /**
     * Returns the screen name of the Spotify artist.
     * 
     * @return the screen name of the Spotify artist.
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Returns the count of song listings by this user.
     * 
     * @return the count of song listings by this user.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the count of song listings by this artist.
     */
    public void increment() {
        count++;
    }

    /**
     * Checks for equality of this ArtistCount with another.
     */
    @Override
    public boolean equals(Object other) {

        // self check
        if (this == other)
            return true;

        // null check
        if (other == null)
            return false;

        // type check and cast
        if (getClass() != other.getClass())
            return false;

        ArtistCount count = (ArtistCount) other;

        // field comparison (only check degree for equality)
        return Objects.equals(artistName, count.artistName);
    }

    @Override
    public int compareTo(ArtistCount o) {
        return Integer.compare(this.getCount(), o.getCount());
    }

    /**
     * Ensures that using a HashMap or HashSet on ArtistCount
     * objects uses the screen name.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(artistName);
    }

}
