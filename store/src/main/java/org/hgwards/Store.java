package org.hgwards;

import java.sql.*;
import java.util.Scanner;

public class Store {


    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "D1pper_001";

    private Scanner scanner = new Scanner(System.in);

    public void connectToDatabase(String dbUrl, String dbUser, String dbPassword) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");

        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Store store = new Store();
        store.connectToDatabase(DB_URL, DB_USER, DB_PASSWORD);
        store.products();
    }

    private void products() {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showMenu();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    viewProducts();
                    break;
                case 4:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private void showMenu() {
        System.out.println("----- Меню магазина -----");
        System.out.println("1. Добавить товар");
        System.out.println("2. Просмотреть товары");
        System.out.println("3. Изменить товар");
        System.out.println("4. Поиск товара");
        System.out.println("5. Удалить товар");
        System.out.println("6. Выход");
        System.out.print("Введите ваш выбор: ");
    }

    private void addProduct() {
        System.out.print("Введите название товара: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание: ");
        String description = scanner.nextLine();
        System.out.print("Введите цену: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Введите количество на складе: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(name, description, price, quantity);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO products (name, description, price, quantity) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Товар добавлен успешно.");
            } else {
                System.out.println("Ошибка при добавлении товара.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении товара: " + e.getMessage());
        }
    }

    private void viewProducts() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("----- Список товаров -----");
                do {
                    Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), rs.getInt("quantity"));
                    System.out.println(product);
                } while (rs.next());
            } else {
                System.out.println("В магазине нет товаров.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении товаров: " + e.getMessage());
        }
    }
}