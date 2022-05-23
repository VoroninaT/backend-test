package homework4.spoonaccular.homework5;

import com.google.gson.Gson;

public class SerializeExampleJson {

    private static final Gson gson = new Gson();

    public static <T> String getJson(T object) {
        return gson.toJson(object);
    }
}
