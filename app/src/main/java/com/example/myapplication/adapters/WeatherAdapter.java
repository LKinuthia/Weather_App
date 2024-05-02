package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.objects.CityNameExtractor;
import com.example.myapplication.objects.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> implements Filterable {
    private List<Weather> weatherList; // Weather objects are used instead of Strings
    private List<Weather> filteredList; // Filtered weather list

    private Map<String, String> matchedCities;

    private Context context;
    private OnItemClickListener listener;

    public WeatherAdapter(Context context,List<Weather> weatherList, Map<String, String> matchedCities) {
        this.context=context;
        this.weatherList = weatherList;
        this.filteredList = new ArrayList<>(weatherList); // Initialize filteredList with the original list
        this.matchedCities = matchedCities;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.bind(weather);

        // Set click listener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(weather); // Trigger the click listener
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityTextView;
        private TextView descriptionTextView;
        private TextView temperatureTextView;
        private TextView humidityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.textView3);
            descriptionTextView = itemView.findViewById(R.id.textView4);
            temperatureTextView = itemView.findViewById(R.id.textView5);
            humidityTextView = itemView.findViewById(R.id.textView6);
        }

        public void bind(Weather weather) {
            String locationId = weather.getlocationId();
            String cityName = matchedCities.get(locationId);
            Log.d("WeatherAdapter", "City name: " + cityName); // Log each city name
            if (cityName != null) {
                cityTextView.setText(cityName);
            } else {
                cityTextView.setText("Unknown City");
            }
            descriptionTextView.setText(weather.getDescription());
            temperatureTextView.setText("Temperature: " + weather.getTemperature());
            humidityTextView.setText(weather.getHumidity());
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint.toString().toLowerCase().trim();
                List<Weather> filteredWeather = new ArrayList<>();

                if (searchText.isEmpty()) {
                    filteredWeather.addAll(weatherList); // If search query is empty, display the original list
                } else {
                    for (Weather weather : weatherList) {
                        // Check if weather data contains the search query in city name or description
                        if (matchedCities.get(weather.getlocationId()).toLowerCase().contains(searchText) ||
                                weather.getDescription().toLowerCase().contains(searchText)) {
                            filteredWeather.add(weather);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredWeather;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Weather>) results.values);
                notifyDataSetChanged(); // Notify adapter about the new filtered list
            }
        };
    }
}
