package sg.edu.nus.iss.workshop28.model;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Review {
    
    private String _id;
    private Integer gid;
    private String name;
    private Integer rating;
    private String user;
    private String c_text;
    private String c_id;

    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getC_text() {
        return c_text;
    }
    public void setC_text(String c_text) {
        this.c_text = c_text;
    }
    public String getC_id() {
        return c_id;
    }
    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    public static Review createFromDoc(Document d) {
        Review r = new Review();

        r.set_id(d.getObjectId("_id").toString());
        r.setC_id(d.getString("c_id"));
        r.setUser(d.getString("user"));
        r.setRating(d.getInteger("rating"));
        r.setC_text(d.getString("c_text"));
        r.setGid(d.getInteger("gid"));
        r.setName(d.getString("game_name"));

        return r;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("_id", getGid())
                .add("name", getName())
                .add("rating", getRating())
                .add("user", getUser())
                .add("comment", getC_text())
                .add("review_id", getC_id())
                .build();
    }

    @Override
    public String toString() {
        return "Review [_id=" + _id + ", gid=" + gid + ", name=" + name + ", rating=" + rating + ", user=" + user
                + ", c_text=" + c_text + ", c_id=" + c_id + "]";
    }
    
}
