package dogs;


import com.google.gson.Gson;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilmariand on 25/06/2018.
 */
@Entity
public class Dog {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    @NotNull
    private String name;

    @Column(name="dog_types")
    @ElementCollection(targetClass=String.class)
    private List<String> types;

    public Dog() {
        this.types = new ArrayList<String>();
    }

    public Dog(String name) {
        this.name = name;
        this.types = new ArrayList<String>();
    }

    public Dog(String name, List<String> types) {
        this.name = name;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void addTypeToTypeList(String theType)
    {
        this.types.add(theType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {

        return "{" +
                "name\":\"" + name + '\"' + "," + new Gson().toJson(types) +
                '}';
    }

}
