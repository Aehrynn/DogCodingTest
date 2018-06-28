package dogs;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/DogController")
@Api(description = "Class that contains the CRUD REST endpoints which handle the Dog object")
public class DogController {

    @Autowired
    DogRepository dogRepository;

    @RequestMapping(value = "/dogs", method = GET)
    @ApiOperation("Returns all the dogs and there sub types in the database")
    public ResponseEntity<?> getDogs() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Dog> allDogs = dogRepository.findAll();
        return new ResponseEntity<>(new Gson().toJson(allDogs), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/export", method = GET)
    @ApiOperation("Returns all the dogs and there sub types in the database, based JSON import document dogs.json")
    public ResponseEntity<?> exportDogs() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Dog> allDogs = dogRepository.findAll();
        JSONObject export = new JSONObject();
        for (Dog allDog : allDogs) {
            JSONArray aDog = new JSONArray(allDog.getTypes());
            export.put(allDog.getName(), aDog);
        }

        return new ResponseEntity<>(export.toString(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/dogs/{name}", method = GET)
    @ApiOperation("Returns a dog and it's sub types in the database")
    public ResponseEntity<?> getDog(@ApiParam("Name of the dog to retrieve") @PathVariable("name") String dogName) {
        Dog aDog = dogRepository.findByName(dogName);
        if (aDog != null) {
            return new ResponseEntity<>(new Gson().toJson(aDog), HttpStatus.OK);
        }
        return new ResponseEntity<>("No Dog found with that id", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/dogs/{name}", method = PUT)
    @ApiOperation("Update a dog and it's sub types in the database")
    public ResponseEntity<?> updateDog(@ApiParam("Name of the dog to update") @PathVariable("name") String dogName, @ApiParam("JSON representation of the dog object to update a dog") @RequestBody Dog updateDog) {
        Dog aDog = dogRepository.findByName(dogName);
        if (aDog != null) {
            if (!aDog.getName().equals(updateDog.getName())) {
                //find by name
                Dog testDog = dogRepository.findByName(updateDog.getName());
                if(testDog != null) {
                    if (!aDog.getId().equals(testDog.getId())) {
                        return new ResponseEntity<>("Dog with that name already exists", HttpStatus.CONFLICT);
                    }
                }
            }

            aDog.setName(updateDog.getName());
            aDog.setTypes(updateDog.getTypes());
             dogRepository.save(aDog);
            //convert to json
            return new ResponseEntity<>(new Gson().toJson(aDog), HttpStatus.OK);
        }
        return new ResponseEntity<>("No Dog found with that name", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/dogs", method = POST)
    @ApiOperation("Creates a dog and it's sub types in the database")
    public ResponseEntity<?> createDog(@ApiParam("JSON representation of the dog object to create a dog") @RequestBody Dog newDog) {
        Dog testDog = dogRepository.findByName(newDog.getName());
        if (testDog == null) {
            dogRepository.save(newDog);
            return new ResponseEntity<>(new Gson().toJson(newDog), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Dog with that name already exists", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/dogs/{name}", method = DELETE)
    @ApiOperation("Deletes a dog and it's sub types in the database")
    public ResponseEntity<?> deleteDog(@ApiParam("Name of the dog to delete") @PathVariable("name") String dogName) {
        Dog aDog = dogRepository.findByName(dogName);
        if (aDog != null) {
            //convert to json
            dogRepository.delete(aDog);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("No Dog found with that name", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/import", method = POST)
    @ApiOperation("Import a dog and it's sub types in the database")
    public ResponseEntity<?> importDogs(@ApiParam("Based JSON import document dogs.json") @RequestBody Map<String, Object> importJson) {
        List<Dog> successfullyImportedDogs = new ArrayList<>();
        for (String dogBreedName : importJson.keySet()) {
            Dog importDog = new Dog(dogBreedName);
            List<String> types = (List<String>) importJson.get(dogBreedName);
            importDog.setTypes(types);
            //Save, for now not handling merge and just not making a dog
            Dog existingDog = dogRepository.findByName(dogBreedName);
            if(existingDog == null)
            {
                dogRepository.save(importDog);
                successfullyImportedDogs.add(importDog);
            }
        }
        return new ResponseEntity<>(new Gson().toJson(successfullyImportedDogs), HttpStatus.OK);
    }
}
