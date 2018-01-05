import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.DateFormat;
import java.sql.Timestamp;

public class SightingTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void sighting_instantiatesSighting_true() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    assertEquals(true, newSighting instanceof Sighting);
  }

  @Test
  public void sighting_sightingRanger_String() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    assertEquals("Smith", newSighting.getRanger());
  }

  @Test
  public void sighting_sightingLocation_String() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    assertEquals("Park", newSighting.getLocation());
  }

  @Test
  public void save_assignsIdToObject() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    newSighting.save();
    Sighting savedSighting = Sighting.all().get(0);
    assertEquals(newSighting.getId(), savedSighting.getId());
  }

  @Test
  public void save_timestampsSighting_date() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    newSighting.save();
    Timestamp savedSighting = Sighting.find(newSighting.getId()).getDateSighted();
    Timestamp newTime = new Timestamp(new Date().getTime());
    assertEquals(newTime.getDay(), savedSighting.getDay());
  }

  @Test
  public void all_returnsAllInstancesOfSighting_true() {
    Sighting firstSighting = new Sighting(1, "Park", "Smith");
    firstSighting.save();
    Sighting secondSighting = new Sighting(1, "National Park", "Walker");
    secondSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(firstSighting));
    assertEquals(true, Sighting.all().get(1).equals(secondSighting));
  }

  @Test
  public void find_returnsSightingWithSameId_secondSighting() {
    Sighting firstSighting = new Sighting(1, "Park", "Smith");
    firstSighting.save();
    Sighting secondSighting = new Sighting(1, "National Park", "Walker");
    secondSighting.save();
    assertEquals(Sighting.find(secondSighting.getId()), secondSighting);
  }

  @Test
  public void update_updatesSighting_true() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    newSighting.save();
    newSighting.update("National Park", "Walker");
    assertEquals("National Park", Sighting.find(newSighting.getId()).getLocation());
    assertEquals("Walker", Sighting.find(newSighting.getId()).getRanger());
  }

  @Test
  public void delete_deletesSighting_true() {
    Sighting newSighting = new Sighting(1, "Park", "Smith");
    newSighting.save();
    int newSightingId = newSighting.getId();
    newSighting.delete();
    assertEquals(null, Sighting.find(newSightingId));
  }


}
