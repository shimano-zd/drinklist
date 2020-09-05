package smartinov.drinklist.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "drinks_table")
public class Drink {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String ingredients;

    private int alcoholPercentage;

    private String imageLocation;

    public Drink(String name, String ingredients, int alcoholPercentage) {
        this.name = name;
        this.ingredients = ingredients;
        this.alcoholPercentage = alcoholPercentage;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getIngredients() {
//        return ingredients;
//    }
//
//    public int getAlcoholPercentage() {
//        return alcoholPercentage;
//    }
//
//    public String getImageLocation(){
//        return this.imageLocation;
//    }
//
//    public void setImageLocation(String location){
//        this.imageLocation = location;
//    }

}
