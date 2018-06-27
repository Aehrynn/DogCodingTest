import dogs.Application;
import dogs.Dog;
import dogs.DogController;
import dogs.DogRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Erin Maguire on 27/06/2018.
 */
@ContextConfiguration(classes = Application.class)
@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
public class DogControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    DogController dogController;

    @Mock
    DogRepository dogRepository;

    List<Dog> dogs = new ArrayList<Dog>();
    Dog firstDog;
    Dog secondDog;
    Dog thirdDog;
    List<String> dogTypes = new ArrayList<>();

    @Before
    public void setup() {
        // Process mock annotations
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dogController).build();

        firstDog = new Dog("pug", new ArrayList<String>());
        firstDog.setId(1L);
        thirdDog = new Dog("pug", new ArrayList<String>());
        thirdDog.setId(1L);
        dogTypes.add("border");
        dogTypes.add("long hair");
        secondDog = new Dog("collie", dogTypes);
        dogs.add(firstDog);
        dogs.add(secondDog);
    }

    @Test
    public void testRetrieveDogs() throws Exception {
        Mockito.when(dogRepository.findAll()).thenReturn(dogs);
        MvcResult result = mockMvc.perform(get("/DogController/dogs"))
                .andExpect(status().isOk()).andReturn();

        Assert.assertEquals(dogs.toString(), result.getResponse().getContentAsString());
    }

    @Test
    public void testRetrieveADogSuccess() throws Exception {
        Mockito.when(dogRepository.findByName("pug")).thenReturn(firstDog);
        MvcResult result = mockMvc.perform(get("/DogController/dogs/pug"))
                .andExpect(status().isOk()).andReturn();

        Assert.assertEquals(firstDog.toString(), result.getResponse().getContentAsString());
    }

    @Test
    public void testRetrieveADogNotFound() throws Exception {
        Mockito.when(dogRepository.findByName("pug")).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/DogController/dogs/pug"))
                .andExpect(status().isNotFound()).andReturn();

        Assert.assertEquals("No Dog found with that id", result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateADogSuccess() throws Exception {
        Mockito.when(dogRepository.findByName("labrador")).thenReturn(null);
        Mockito.when(dogRepository.save(Mockito.any(Dog.class))).thenReturn(Mockito.any(Dog.class));
        MvcResult result = mockMvc.perform(post("/DogController/dogs").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"labrador\"}"))
                .andExpect(status().isCreated()).andReturn();

        Assert.assertEquals("{\"name\":\"labrador\",[]}", result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateADogWithTypesSuccess() throws Exception {
        Mockito.when(dogRepository.findByName("labrador")).thenReturn(null);
        Mockito.when(dogRepository.save(Mockito.any(Dog.class))).thenReturn(Mockito.any(Dog.class));
        MvcResult result = mockMvc.perform(post("/DogController/dogs").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"labrador\",\"types\":[\"Black\",\"Chocolate\"]}"))
                .andExpect(status().isCreated()).andReturn();

        Assert.assertEquals("{\"name\":\"labrador\",[\"Black\",\"Chocolate\"]}", result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateADogNameAlreadyExists() throws Exception {
        Mockito.when(dogRepository.findByName("pug")).thenReturn(firstDog);
        MvcResult result = mockMvc.perform(post("/DogController/dogs").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"pug\"}"))
                .andExpect(status().isConflict()).andReturn();

        Assert.assertEquals("Dog with that name already exists", result.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteADogSuccess() throws Exception {
        Mockito.when(dogRepository.findByName("pug")).thenReturn(firstDog);
        MvcResult result = mockMvc.perform(delete("/DogController/dogs/pug").content("name=pug"))
                .andExpect(status().isOk()).andReturn();

        Assert.assertEquals("Deleted", result.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteADogNotFound() throws Exception {
        Mockito.when(dogRepository.findByName("labrador")).thenReturn(null);
        MvcResult result = mockMvc.perform(delete("/DogController/dogs/pug").content("name=pug"))
                .andExpect(status().isNotFound()).andReturn();

        Assert.assertEquals("No Dog found with that name", result.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateADogSuccess() throws Exception {
        Mockito.when(dogRepository.findByName("pug")).thenReturn(firstDog);
        MvcResult result = mockMvc.perform(put("/DogController/dogs/pug").content("name=pug").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"pug\"}"))
                .andExpect(status().isOk()).andReturn();

        Assert.assertEquals(firstDog.toString(), result.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateADog_DogDoesNotExist() throws Exception {
        Mockito.when(dogRepository.findByName("pug")).thenReturn(null);
        MvcResult result = mockMvc.perform(put("/DogController/dogs/pug").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"pug\"}"))
                .andExpect(status().isNotFound()).andReturn();

        Assert.assertEquals("No Dog found with that name", result.getResponse().getContentAsString());
    }

//    @Test
//    public void testUpdateADogNameAlreadyExists() throws Exception {
//        Mockito.when(dogRepository.findByName("pug")).thenReturn(firstDog, thirdDog);
//        Mockito.when(dogRepository.findByName("labrador")).thenReturn(firstDog, thirdDog);
//        MvcResult result = mockMvc.perform(put("/DogController/dogs/pug").content("name=pug").contentType(MediaType.APPLICATION_JSON)
//                .content("{\"name\":\"labrador\"}"))
//                .andExpect(status().isConflict()).andReturn();
//
//        Assert.assertEquals("Dog with that name already exists", result.getResponse().getContentAsString());
//    }
}
