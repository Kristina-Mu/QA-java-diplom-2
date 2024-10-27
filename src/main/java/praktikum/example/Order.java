package praktikum.example;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private List<String> ingredients;

    // Конструктор с параметром
    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // Пустой конструктор
    public Order() {
        this.ingredients = new ArrayList<>();
    }

    // Геттер для ingredients
    public List<String> getIngredients() {
        return ingredients;
    }

    // Сеттер для ingredients
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // Метод для добавления ингредиента в заказ
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    // Метод для получения количества ингредиентов
    public int getIngredientCount() {
        return ingredients.size();
    }
}
