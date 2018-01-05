import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class EndangeredAnimalTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void endangeredAnimal_instantiatesNewEndangeredAnimal_true() {
    EndangeredAnimal newEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    assertEquals(true, newEndangeredAnimal instanceof EndangeredAnimal);
  }

  @Test
  public void getName_endangeredAnimalInstantiatesWithName_Bison() {
    EndangeredAnimal newEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    assertEquals("Bison", newEndangeredAnimal.getName());
  }

  @Test
  public void getHealth_endangeredAnimalHealth_good() {
    EndangeredAnimal newEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    assertEquals("good", newEndangeredAnimal.getHealth());
  }

  @Test
  public void getAge_endangeredAnimalAge_3() {
    EndangeredAnimal newEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    assertEquals(3, newEndangeredAnimal.getAge());
  }

  @Test
  public void save_savesIntoDatabase_true() {
    EndangeredAnimal newEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    newEndangeredAnimal.save();
    assertTrue(EndangeredAnimal.all().get(0).equals(newEndangeredAnimal));
  }

  @Test
  public void find_returnsEndangeredAnimalWithSameId_secondEndangeredAnimal() {
    EndangeredAnimal firstEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    firstEndangeredAnimal.save();
    EndangeredAnimal secondEndangeredAnimal = new EndangeredAnimal("Cougar", "poor", 1);
    secondEndangeredAnimal.save();
    assertEquals(EndangeredAnimal.find(secondEndangeredAnimal.getId()), secondEndangeredAnimal);
  }

  @Test
  public void update_updatesEndangeredAnimal_true() {
    EndangeredAnimal newEndangeredAnimal = new EndangeredAnimal("Bison", "good", 3);
    newEndangeredAnimal.save();
    newEndangeredAnimal.update("Cougar", "poor", 1);
    assertEquals("Cougar", EndangeredAnimal.find(newEndangeredAnimal.getId()).getName());
    assertEquals("poor", EndangeredAnimal.find(newEndangeredAnimal.getId()).getHealth());
    assertEquals(1, EndangeredAnimal.find(newEndangeredAnimal.getId()).getAge());
  }

}
