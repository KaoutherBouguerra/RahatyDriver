package art4muslim.macbook.rahatydriver.models;

/**
 * Created by macbook on 02/01/2018.
 */

public class OrderModel {

    private String id;
    private String date;
    private String time;
    private String status;

    public OrderModel(String id, String date, String time, String status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
