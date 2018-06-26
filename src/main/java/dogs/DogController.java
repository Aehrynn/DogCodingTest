package dogs;

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
public class DogController {

    @Autowired
    DogRepository dogRepository;



    @RequestMapping(value = "/dogs", method = GET)
    public ResponseEntity<?> getDogs() {
        List<Dog> allDogs = dogRepository.findAll();
        //TODO convert to JSON
        return new ResponseEntity<>(allDogs.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/dogs/import", method = POST)
    public void importDogs() {
         //TODO put something in here to use the supplied JSON
    }

    @RequestMapping(value = "/dogs/{name}", method = GET)
    public ResponseEntity<?> getDog(@PathVariable("name") String dogName) {
        Dog aDog = dogRepository.findByName(dogName);
        if (aDog != null) {
            //TODO convert to json
            return new ResponseEntity<>(aDog.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("No Dog found with that id", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/dogs/{name}", method = PUT)
    public ResponseEntity<?> updateDog(@PathVariable("name") String dogName, @RequestBody Dog updateDog) {
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
        return new ResponseEntity<>("No Dog found with that id", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/dogs", method = POST)
    public ResponseEntity<?> createDog(@RequestBody Dog newDog) {
        Dog testDog = dogRepository.findByName(newDog.getName());
        if (testDog == null) {
            dogRepository.save(newDog);
            return new ResponseEntity<>(newDog.toString(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Dog with that name already exists", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/dogs/{name}", method = DELETE)
    public ResponseEntity<?> deleteDog(@PathVariable("name") String dogName) {
        Dog aDog = dogRepository.findByName(dogName);
        if (aDog != null) {
            //convert to json
            dogRepository.delete(aDog);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("No Dog found with that id", HttpStatus.NOT_FOUND);
    }
}
