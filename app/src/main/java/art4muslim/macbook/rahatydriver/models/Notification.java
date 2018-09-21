package art4muslim.macbook.rahatydriver.models;

/**
 * Created by macbook on 04/01/2018.
 */

public class Notification {
    private String idOder;
    private String date;
    private String time;
    private String title_ar;
    private String body_ar;
    private String title_en;
    private String body_en;

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getBody_ar() {
        return body_ar;
    }

    public void setBody_ar(String body_ar) {
        this.body_ar = body_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getBody_en() {
        return body_en;
    }

    public void setBody_en(String body_en) {
        this.body_en = body_en;
    }

    public Notification(String title_ar,String title_en,String body_ar,String body_en,String idOder, String date, String time) {
        this.idOder = idOder;
        this.date = date;
        this.time = time;
        this.title_ar = title_ar;
        this.title_en = title_en;
        this.body_ar = body_ar;
        this.body_en = body_en;
    }

    public String getIdOder() {
        return idOder;
    }

    public void setIdOder(String idOder) {
        this.idOder = idOder;
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
