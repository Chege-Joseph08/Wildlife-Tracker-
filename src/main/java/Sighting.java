import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;

public class Sighting {

  private int id;
  private int animal_id;
  private String location;
  private String ranger;
  private Timestamp date_sighted;

  // Object Constructor

  public Sighting(int animal_id, String location, String ranger) {
    this.animal_id = animal_id;
    this.location = location;
    this.ranger = ranger;
  }

  // Getters/Setters

  public int getId() {
    return id;
  }

  public int getAnimalId() {
    return animal_id;
  }

  public String getLocation() {
    return location;
  }

  public String getRanger() {
    return ranger;
  }

  public Timestamp getDateSighted() {
    return date_sighted;
  }

  // Database Methods

  @Override
  public boolean equals(Object otherSighting){
    if (!(otherSighting instanceof Sighting)) {
      return false;
    } else {
      Sighting newSighting = (Sighting) otherSighting;
      return this.getLocation().equals(newSighting.getLocation()) &&
             this.getId() == newSighting.getId() &&
             this.getAnimalId() == newSighting.getAnimalId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO sightings (animal_id, location, ranger, date_sighted) VALUES (:animal_id, :location, :ranger, now());";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("animal_id", this.animal_id)
        .addParameter("location", this.location)
        .addParameter("ranger", this.ranger)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Sighting> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings";
      return con.createQuery(sql)
      .throwOnMappingFailure(false)
      .executeAndFetch(Sighting.class);
    }
  }

  public static Sighting find(int id) {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM sightings WHERE id=:id";
      return con.createQuery(sql)
      .addParameter("id", id)
      .throwOnMappingFailure(false)
      .executeAndFetchFirst(Sighting.class);
    }
  }

  public void update(String location, String ranger){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE sightings SET location = :location , ranger = :ranger WHERE id=:id";
      con.createQuery(sql)
      .addParameter("location", location)
      .addParameter("ranger", ranger)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM sightings WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }


}
