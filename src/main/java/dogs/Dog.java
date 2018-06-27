package dogs;


import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erin Maguire on 25/06/2018.
 */

@ApiModel(description = "Class representing a Dog.")
@Entity
public class Dog {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "Auto generated database id", hidden = true)
    private Long id;

    @Column(unique=true)
    @NotNull
    @ApiModelProperty(notes = "Name of Dog", required = true)
    private String name;

    @Column(name="dog_types")
    @ElementCollection(targetClass=String.class)
    @ApiModelProperty(notes = "List of Dog sub types")
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
                "\"name\":\"" + name + '\"' + "," + new Gson().toJson(types) +
                '}';
    }

}
