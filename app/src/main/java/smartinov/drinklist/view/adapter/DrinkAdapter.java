package smartinov.drinklist.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import smartinov.drinklist.R;
import smartinov.drinklist.model.Drink;

public class DrinkAdapter extends ListAdapter<Drink, DrinkAdapter.DrinkHolder> {


    private DrinkClickListener listener;

    public DrinkAdapter() {

        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Drink> DIFF_CALLBACK = new DiffUtil.ItemCallback<Drink>() {
        @Override
        public boolean areItemsTheSame(@NonNull Drink oldItem, @NonNull Drink newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Drink oldItem, @NonNull Drink newItem) {
            return (oldItem.getName().equals(newItem.getName())
            && oldItem.getIngredients().equals(newItem.getIngredients())
            && oldItem.getAlcoholPercentage() == newItem.getAlcoholPercentage());
        }
    };

    @NonNull
    @Override
    public DrinkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drink_item, parent, false);
        return new DrinkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkHolder holder, int position) {

        Drink currentDrink = getItem(position);
        holder.textViewName.setText(currentDrink.getName());
        holder.textViewIngredients.setText(currentDrink.getIngredients());
        if (currentDrink.getAlcoholPercentage() > 0) {
            holder.textViewAlcohol.setText("Alc:" + String.valueOf(currentDrink.getAlcoholPercentage()) + "%");
        } else {
            holder.textViewAlcohol.setText("");
        }

        if(currentDrink.getImageLocation() != null){
            Picasso.get()
                    .load("file://" + currentDrink.getImageLocation())
                    .placeholder(R.drawable.image_loader)
                    .fit().centerCrop()
                    .into(holder.image);
        }
    }


    public Drink getNoteAtPosition(int position) {
        return getItem(position);
    }

    class DrinkHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewIngredients;
        private TextView textViewAlcohol;
        private ImageView image;

        public DrinkHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewIngredients = itemView.findViewById(R.id.text_view_ingredients);
            textViewAlcohol = itemView.findViewById(R.id.text_view_alcohol);
            image = itemView.findViewById(R.id.image_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnDrinkClicked(getItem(position));
                    }
                }
            });
        }
    }

    public void setOnDrinkClickListener(DrinkClickListener listener) {
        this.listener = listener;

    }

}
