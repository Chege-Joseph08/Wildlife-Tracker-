import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

     ProcessBuilder process = new ProcessBuilder(); 
    Integer port;
    // This tells our app that if Heroku sets a port for us, we need to use that port.
    // Otherwise, if they do not, continue using port 4567.
    if (process.environment().get("PORT") != null) {
        port = Integer.parseInt(process.environment().get("PORT"));
    } else {
        port = 4567;
    }
    setPort(port);

    // Get Routes

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("animals", NormalAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      NormalAnimal animal = NormalAnimal.find(Integer.parseInt(request.params(":id")));
      model.put("sightings", animal.getSightings());
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/endangeredanimal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal animal = EndangeredAnimal.find(Integer.parseInt(request.params(":id")));
      model.put("sightings", animal.getSightings());
      model.put("animal", animal);
      model.put("template", "templates/endangeredanimal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/sighting/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params(":id")));
      model.put("sighting", sighting);
      model.put("template", "templates/sighting.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Post Routes

    post("/normalanimal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("animal");
      try{
        NormalAnimal newNormalAnimal = new NormalAnimal(name);
        newNormalAnimal.save();
      }
      catch (UnsupportedOperationException exception)
      { }
      model.put("animals", NormalAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/endangeredanimal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("animal");
      int userHealth = Integer.parseInt(request.queryParams("health"));
      int userAge = Integer.parseInt(request.queryParams("age"));
      String logHealth = "";
      String logAge = "";
      if (userHealth == 1){
        logHealth = EndangeredAnimal.Good;
      } else if (userHealth == 2) {
        logAge = EndangeredAnimal.Poor;
      }
      EndangeredAnimal newAnimal = new EndangeredAnimal(name, logHealth, userAge);
      try{
        newAnimal.save();
      }
      catch (UnsupportedOperationException exception)
      { }
      model.put("animals", NormalAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      NormalAnimal animal = NormalAnimal.find(Integer.parseInt(request.params(":id")));
      String location = request.queryParams("location");
      String ranger = request.queryParams("name");
      Sighting newSighting = new Sighting(animal.getId(), location, ranger);
      newSighting.save();
      model.put("sightings", animal.getSightings());
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animal/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      NormalAnimal animal = NormalAnimal.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");
      try{
        animal.update(name);
      }
      catch (UnsupportedOperationException exception)
      { }
      String url = String.format("/animal/%d", animal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/endangeredanimal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal animal = EndangeredAnimal.find(Integer.parseInt(request.params(":id")));
      String location = request.queryParams("location");
      String ranger = request.queryParams("name");
      Sighting newSighting = new Sighting(animal.getId(), location, ranger);
      newSighting.save();
      model.put("sightings", animal.getSightings());
      model.put("animal", animal);
      model.put("template", "templates/endangeredanimal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/endangeredanimal/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal animal = EndangeredAnimal.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");
      String health = request.queryParams("health");
      int age = Integer.parseInt(request.params("age"));
      try {
        animal.update(name, health, age);
      }
      catch (UnsupportedOperationException exception)
      { }
      String url = String.format("/endangeredanimal/%d", animal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animal/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      NormalAnimal animal = NormalAnimal.find(Integer.parseInt(request.params(":id")));
      animal.delete();
      animal.deleteSightings();
      model.put("animals", NormalAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/endangeredanimal/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal animal = EndangeredAnimal.find(Integer.parseInt(request.params(":id")));
      animal.delete();
      animal.deleteSightings();
      model.put("animals", NormalAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/sighting/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params(":id")));
      sighting.delete();
      model.put("animals", NormalAnimal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/sighting/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params(":id")));
      String location = request.queryParams("location");
      String name = request.queryParams("name");
      sighting.update(location, name);
      String url = String.format("/sighting/%d", sighting.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
