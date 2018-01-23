package smartAmigos.com.nammakarnataka.helper;

/**
 * Created by avinashk on 17/01/18.
 */
public class nearby_places_adapter {

    private String placename, category;
    private float distance;
    private int id;
    private double latitude, longitude;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public nearby_places_adapter(int id, String category,String placename, float distance, double latitude, double longitude) {
        this.id = id;
        this.category = category;
        this.placename = placename;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {

        return category;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlacename() {

        return placename;
    }

    public float getDistance() {
        return distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
