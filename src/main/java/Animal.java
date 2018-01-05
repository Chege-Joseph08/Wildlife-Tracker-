import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;
   
   //Animal Constructor
public abstract class Animal {
  public int id;
  public String name;
  public String endangered;

  // Getters/Setters

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  // Database Methods

  @Override
  public boolean equals(Object testObject) {
    if (!(testObject instanceof Animal)) {
      return false;
    } else {
      Animal newAnimal = (Animal) testObject;
      return this.getId() == newAnimal.getId() && this.getName().equals(newAnimal.getName());
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM animals WHERE id = :id;";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public List<Sighting> getSightings() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings where animal_id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Sighting.class);
    }
  }

  public void deleteSightings() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM sightings where animal_id=:id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }


}
