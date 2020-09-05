package smartinov.drinklist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import smartinov.drinklist.dao.DrinkRepository;
import smartinov.drinklist.model.Drink;

public class DrinkViewModel extends AndroidViewModel {

    private DrinkRepository repository;
    private LiveData<List<Drink>> allDrinks;

    public DrinkViewModel(@NonNull Application application) {
        super(application);
        repository  = new DrinkRepository(application);
        allDrinks = repository.getAllDrinks();
    }

    public void insert(Drink drink){
        repository.insert(drink);
    }

    public void update(Drink drink){
        repository.update(drink);
    }

    public void delete(Drink drink){
        repository.delete(drink);
    }

    public void deleteAllDrinks(){
        repository.getAllDrinks();
    }

    public LiveData<List<Drink>> getAllDrinks(){
        return allDrinks;
    }

}
