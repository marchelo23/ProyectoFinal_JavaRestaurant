package com.restaurant.services;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import com.restaurant.models.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de persistencia usando JSON (Gson).
 */
public class FileStorage {
    private final String dataDir;
    private final Gson gson;

    public FileStorage(String dataDir) {
        this.dataDir = dataDir;

        // Crear directorio si no existe
        try {
            Files.createDirectories(Paths.get(dataDir));
        } catch (IOException e) {
            System.err.println("Error creando directorio de datos: " + e.getMessage());
        }

        // Configurar Gson con adaptador personalizado para polimorfismo de MenuItem
        RuntimeTypeAdapterFactory<MenuItem> menuItemAdapter = RuntimeTypeAdapterFactory
                .of(MenuItem.class, "type")
                .registerSubtype(FoodItem.class, "food")
                .registerSubtype(DrinkItem.class, "drink");

        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(menuItemAdapter)
                .setPrettyPrinting()
                .create();
    }

    // Tables
    public void saveTables(List<Table> tables) {
        saveToFile("tables.json", tables, new TypeToken<List<Table>>() {
        }.getType());
    }

    public List<Table> loadTables() {
        return loadFromFile("tables.json", new TypeToken<List<Table>>() {
        }.getType());
    }

    // Waiters
    public void saveWaiters(List<Waiter> waiters) {
        saveToFile("waiters.json", waiters, new TypeToken<List<Waiter>>() {
        }.getType());
    }

    public List<Waiter> loadWaiters() {
        return loadFromFile("waiters.json", new TypeToken<List<Waiter>>() {
        }.getType());
    }

    // Menu Items
    public void saveMenuItems(List<MenuItem> menuItems) {
        saveToFile("menu_items.json", menuItems, new TypeToken<List<MenuItem>>() {
        }.getType());
    }

    public List<MenuItem> loadMenuItems() {
        return loadFromFile("menu_items.json", new TypeToken<List<MenuItem>>() {
        }.getType());
    }

    // Orders
    public void saveOrders(List<Order> orders) {
        saveToFile("orders.json", orders, new TypeToken<List<Order>>() {
        }.getType());
    }

    public List<Order> loadOrders() {
        return loadFromFile("orders.json", new TypeToken<List<Order>>() {
        }.getType());
    }

    // Métodos genéricos privados
    private <T> void saveToFile(String filename, T data, Type type) {
        String filepath = dataDir + File.separator + filename;
        try (Writer writer = new FileWriter(filepath)) {
            gson.toJson(data, type, writer);
        } catch (IOException e) {
            System.err.println("Error guardando " + filename + ": " + e.getMessage());
        }
    }

    private <T> T loadFromFile(String filename, Type type) {
        String filepath = dataDir + File.separator + filename;
        File file = new File(filepath);

        if (!file.exists()) {
            return null;
        }

        try (Reader reader = new FileReader(filepath)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println("Error cargando " + filename + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Factory para manejar polimorfismo en JSON de MenuItem.
     */
    private static class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
        private final Class<?> baseType;
        private final String typeFieldName;
        private final java.util.Map<String, Class<?>> labelToSubtype = new java.util.LinkedHashMap<>();
        private final java.util.Map<Class<?>, String> subtypeToLabel = new java.util.LinkedHashMap<>();

        private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName) {
            this.baseType = baseType;
            this.typeFieldName = typeFieldName;
        }

        public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
            return new RuntimeTypeAdapterFactory<>(baseType, typeFieldName);
        }

        public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, String label) {
            labelToSubtype.put(label, type);
            subtypeToLabel.put(type, label);
            return this;
        }

        @Override
        public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
            if (!baseType.isAssignableFrom(type.getRawType())) {
                return null;
            }

            final java.util.Map<String, TypeAdapter<?>> labelToDelegate = new java.util.LinkedHashMap<>();
            final java.util.Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new java.util.LinkedHashMap<>();

            for (java.util.Map.Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
                TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));
                labelToDelegate.put(entry.getKey(), delegate);
                subtypeToDelegate.put(entry.getValue(), delegate);
            }

            return new TypeAdapter<R>() {
                @Override
                public void write(JsonWriter out, R value) throws IOException {
                    Class<?> srcType = value.getClass();
                    String label = subtypeToLabel.get(srcType);
                    @SuppressWarnings("unchecked")
                    TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToDelegate.get(srcType);

                    JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();
                    jsonObject.addProperty(typeFieldName, label);

                    Streams.write(jsonObject, out);
                }

                @Override
                public R read(JsonReader in) throws IOException {
                    JsonElement jsonElement = Streams.parse(in);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonElement labelJsonElement = jsonObject.remove(typeFieldName);

                    if (labelJsonElement == null) {
                        throw new JsonParseException("No se encontró el campo " + typeFieldName);
                    }

                    String label = labelJsonElement.getAsString();
                    @SuppressWarnings("unchecked")
                    TypeAdapter<R> delegate = (TypeAdapter<R>) labelToDelegate.get(label);

                    if (delegate == null) {
                        throw new JsonParseException("Subtipo desconocido: " + label);
                    }

                    return delegate.fromJsonTree(jsonObject);
                }
            };
        }
    }
}
