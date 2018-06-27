package dogs;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        List<Dog> allDogs = dogRepository.findAll();
        return new ResponseEntity<>(allDogs.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/dogs/{name}", method = GET)
    @ApiOperation("Returns a dog and it's sub types in the database")
    public ResponseEntity<?> getDog(@ApiParam("Name of the dog to retrieve") @PathVariable("name") String dogName) {
        Dog aDog = dogRepository.findByName(dogName);
        if (aDog != null) {
            return new ResponseEntity<>(aDog.toString(), HttpStatus.OK);
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
            return new ResponseEntity<>(aDog.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("No Dog found with that name", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/dogs", method = POST)
    @ApiOperation("Creates a dog and it's sub types in the database")
    public ResponseEntity<?> createDog(@ApiParam("JSON representation of the dog object to create a dog") @RequestBody Dog newDog) {
        Dog testDog = dogRepository.findByName(newDog.getName());
        if (testDog == null) {
            dogRepository.save(newDog);
            return new ResponseEntity<>(newDog.toString(), HttpStatus.CREATED);
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
}
