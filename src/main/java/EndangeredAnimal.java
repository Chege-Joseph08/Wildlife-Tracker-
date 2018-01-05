import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class EndangeredAnimal extends Animal {
  private String health;
  private int age;
     
     // Constant
  
  public static final String Good = "good";
  public static final String Poor = "poor";
  public static final String isEndangered = "yes";

  // Object Constructor

  public EndangeredAnimal(String name, String health, int age) {
    this.name = name;
    this.health = health;
    this.age = age;
    endangered = isEndangered;

  }

  // Getters/Setters

  public String getHealth() {
    return health;
  }

  public int getAge() {
    return age;
  }

  // Database Methods

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      if(name.equals("")) {
        throw new UnsupportedOperationException("Enter name");
      }
      String sql = "INSERT INTO animals (name, health, age, endangered) VALUES (:name, :health, :age, :endangered)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("health", this.health)
        .addParameter("age", this.age)
        .addParameter("endangered", endangered)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<EndangeredAnimal> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM animals WHERE endangered='yes'";
      return con.createQuery(sql)
      .throwOnMappingFailure(false)
      .executeAndFetch(EndangeredAnimal.class);
    }
  }

  public static EndangeredAnimal find(int id) {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM animals WHERE id=:id";
      return con.createQuery(sql)
      .addParameter("id", id)
      .throwOnMappingFailure(false)
      .executeAndFetchFirst(EndangeredAnimal.class);
    }
  }

  public void update(String name, String health, int age){
    try(Connection con = DB.sql2o.open()) {
      if(name.equals("")) {
        throw new UnsupportedOperationException("Enter name");
      }
      String sql = "UPDATE animals SET name = :name , health = :health , age = :age WHERE id=:id";
         con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("health", health)
        .addParameter("age", age)
        .addParameter("id", id)
        .executeUpdate();
    }
  }


}
