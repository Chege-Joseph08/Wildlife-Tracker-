import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class NormalAnimalTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void normalAnimal_instantiatesNewNormalAnimal_true() {
    NormalAnimal newNormalAnimal = new NormalAnimal("Cat");
    assertEquals(true, newNormalAnimal instanceof NormalAnimal);
  }

  @Test
  public void getName_normalAnimalInstantiatesWithName_Cat() {
    NormalAnimal newNormalAnimal = new NormalAnimal("Cat");
    assertEquals("Cat", newNormalAnimal.getName());
  }

  @Test
  public void save_savesIntoDatabase_true() {
    NormalAnimal newNormalAnimal = new NormalAnimal("Cat");
    newNormalAnimal.save();
    assertTrue(NormalAnimal.all().get(0).equals(newNormalAnimal));
  }

  @Test
  public void find_returnsNormalAnimalWithSameId_secondAnimal() {
    NormalAnimal firstNormalAnimal = new NormalAnimal("Cat");
    firstNormalAnimal.save();
    NormalAnimal secondNormalAnimal = new NormalAnimal("Dog");
    secondNormalAnimal.save();
    assertEquals(NormalAnimal.find(secondNormalAnimal.getId()), secondNormalAnimal);
  }

  @Test
  public void update_updatesAnimalNames_true() {
    NormalAnimal newNormalAnimal = new NormalAnimal("Cat");
    newNormalAnimal.save();
    newNormalAnimal.update("Dog");
    assertEquals("Dog", NormalAnimal.find(newNormalAnimal.getId()).getName());
  }

}
