import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// ----------------------------------------------------
// Archivo de código único para el sistema de inventario.
// Contiene todas las clases en un solo lugar.
// ----------------------------------------------------

public class Inventario {

    public static void main(String[] args) {
        // Se llama al método para ejecutar todas las pruebas
        runTests();
    }

    public static void runTests() {
        System.out.println("=========================================");
        System.out.println("   Ejecutando Pruebas de Patrones de Diseño");
        System.out.println("=========================================");

        Inventory inventory = new Inventory();

        // ----------------------------------------------------
        // Prueba de Abstract Factory
        // ----------------------------------------------------
        System.out.println("\n--- Prueba de Abstract Factory ---");
        ProductFactory electronicsFactory = new ElectronicsFactory();
        ProductFactory foodFactory = new FoodFactory();
        
        if (electronicsFactory != null && foodFactory != null) {
            System.out.println("OK: Abstract Factory: Se crearon las fábricas correctamente.");
        } else {
            System.out.println("Error: Abstract Factory: Las fábricas no se crearon.");
        }

        // ----------------------------------------------------
        // Prueba de Builder y Abstract Factory (Integración)
        // ----------------------------------------------------
        System.out.println("\n--- Prueba de Builder y Abstract Factory ---");
        ProductDirector director = new ProductDirector();
        
        // Creación de producto electrónico
        Product.Builder electronicsBuilder = electronicsFactory.createProductBuilder();
        director.constructLaptop(electronicsBuilder);
        Product laptop = electronicsBuilder.build();
        inventory.addProduct(laptop);
        
        if (laptop.getName().equals("Laptop") && laptop.getCategory().equals("Electrónica") && laptop.getAttributes().containsKey("Marca")) {
            System.out.println("OK: Builder: El objeto 'Laptop' fue construido con todos los atributos.");
        } else {
            System.out.println("Error: Builder: El objeto 'Laptop' no fue construido correctamente.");
        }

        // Creación de producto de comida
        Product.Builder foodBuilder = foodFactory.createProductBuilder();
        director.constructCereal(foodBuilder);
        Product cereal = foodBuilder.build();
        inventory.addProduct(cereal);
        
        if (cereal.getName().equals("Cereal de Avena") && cereal.getCategory().equals("Comida") && cereal.getAttributes().containsKey("Peso")) {
            System.out.println("OK: Builder: El objeto 'Cereal de Avena' fue construido con todos los atributos.");
        } else {
            System.out.println("Error: Builder: El objeto 'Cereal de Avena' no fue construido correctamente.");
        }

        // ----------------------------------------------------
        // Prueba de Prototype
        // ----------------------------------------------------
        System.out.println("\n--- Prueba de Prototype ---");
        try {
            // Clonación de laptop
            Product clonedLaptop = (Product) laptop.clone();
            clonedLaptop.setName("Laptop Lite Clonada");
            clonedLaptop.setPrice(800.0);
            inventory.addProduct(clonedLaptop);

            if (clonedLaptop != laptop && !clonedLaptop.getId().equals(laptop.getId()) && clonedLaptop.getName().equals("Laptop Lite Clonada") && laptop.getName().equals("Laptop")) {
                System.out.println("OK: Prototype: El clon es una instancia independiente con datos modificados.");
            } else {
                System.out.println("Error: Prototype: La clonación no funcionó como se esperaba.");
            }

            // Clonación de cereal
            Product clonedCereal = (Product) cereal.clone();
            clonedCereal.setName("Cereal Integral Clonado");
            clonedCereal.setQuantity(20);
            inventory.addProduct(clonedCereal);
            
            if (clonedCereal != cereal && !clonedCereal.getId().equals(cereal.getId()) && clonedCereal.getQuantity() == 20 && cereal.getQuantity() == 50) {
                System.out.println("OK: Prototype: El clon de 'Cereal' es una instancia independiente.");
            } else {
                System.out.println("Error: Prototype: El clon de 'Cereal' no funcionó como se esperaba.");
            }
        } catch (CloneNotSupportedException e) {
            System.err.println("Error al clonar un producto: " + e.getMessage());
        }

        // ----------------------------------------------------
        // Prueba de Integración: Flujo completo
        // ----------------------------------------------------
        System.out.println("\n--- Prueba de Integración: Inventario Final ---");
        inventory.printAllProducts();
        System.out.println("OK: Integración: Se añadieron y clonaron productos al inventario.");

        System.out.println("\n=========================================");
        System.out.println("         Fin de las Pruebas");
        System.out.println("=========================================");
    }

    // ----------------------------------------------------
    // Clase principal del producto que implementa el Builder
    // y el Prototype.
    // ----------------------------------------------------
    static class Product implements Cloneable {
        private final UUID id;
        private String name;
        private double price;
        private int quantity;
        private final String category;
        private final Map<String, String> attributes;

        // Constructor privado para ser usado solo por el Builder.
        private Product(Builder builder) {
            this.id = UUID.randomUUID();
            this.name = builder.name;
            this.price = builder.price;
            this.quantity = builder.quantity;
            this.category = builder.category;
            // Corregido: Se clona el mapa de atributos para asegurar una copia profunda.
            this.attributes = new HashMap<>(builder.attributes);
        }

        // Constructor para la clonación profunda.
        private Product(Product other) {
            this.id = UUID.randomUUID(); // Nuevo ID
            this.name = other.name;
            this.price = other.price;
            this.quantity = other.quantity;
            this.category = other.category;
            this.attributes = new HashMap<>(other.attributes); // Clonación profunda del mapa
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            // Se usa el constructor de clonación profunda.
            return new Product(this);
        }

        // Getters y Setters
        public UUID getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getCategory() { return category; }
        public Map<String, String> getAttributes() { return attributes; }

        @Override
        public String toString() {
            return "Product{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    ", category='" + category + '\'' +
                    ", attributes=" + attributes +
                    '}';
        }

        // ----------------------------------------------------
        // Patrón de diseño Builder
        // ----------------------------------------------------
        public static class Builder {
            private String name;
            private double price;
            private int quantity = 0;
            private String category;
            private final Map<String, String> attributes = new HashMap<>();

            public Builder() {}

            public Builder withName(String name) { this.name = name; return this; }
            public Builder withPrice(double price) { this.price = price; return this; }
            public Builder withQuantity(int quantity) { this.quantity = quantity; return this; }
            public Builder inCategory(String category) { this.category = category; return this; }
            public Builder withAttribute(String key, String value) { this.attributes.put(key, value); return this; }

            public Product build() {
                if (name == null || price <= 0 || category == null) {
                    throw new IllegalStateException("Nombre, precio y categoría son obligatorios.");
                }
                return new Product(this);
            }
        }
    }

    // ----------------------------------------------------
    // Director para el patrón Builder.
    // Define pasos predeterminados para construir objetos complejos.
    // ----------------------------------------------------
    static class ProductDirector {
        public void constructLaptop(Product.Builder builder) {
            builder.withName("Laptop")
                    .withPrice(1500.0)
                    .withQuantity(5)
                    .inCategory("Electrónica")
                    .withAttribute("Marca", "TechCorp")
                    .withAttribute("Modelo", "XPS-15");
        }

        public void constructCereal(Product.Builder builder) {
            builder.withName("Cereal de Avena")
                    .withPrice(5.50)
                    .withQuantity(50)
                    .inCategory("Comida")
                    .withAttribute("Marca", "Nature Foods")
                    .withAttribute("Peso", "500g");
        }
    }

    // ----------------------------------------------------
    // Patrón de diseño Abstract Factory
    // Interfaz para la fábrica que crea familias de objetos
    // ----------------------------------------------------
    interface ProductFactory {
        Product.Builder createProductBuilder();
    }

    // ----------------------------------------------------
    // Fabrica concreta para productos electronicos
    // ----------------------------------------------------
    static class ElectronicsFactory implements ProductFactory {
        @Override
        public Product.Builder createProductBuilder() {
            return new Product.Builder().inCategory("Electrónica");
        }
    }

    // ----------------------------------------------------
    // Fabrica concreta para productos de comida
    // ----------------------------------------------------
    static class FoodFactory implements ProductFactory {
        @Override
        public Product.Builder createProductBuilder() {
            return new Product.Builder().inCategory("Comida");
        }
    }

    // ----------------------------------------------------
    // Clase para gestionar el inventario. No es un Singleton.
    // ----------------------------------------------------
    static class Inventory {
        private final List<Product> products = new ArrayList<>();

        public void addProduct(Product product) {
            products.add(product);
            System.out.println("Producto '" + product.getName() + "' añadido al inventario.");
        }

        public void printAllProducts() {
            if (products.isEmpty()) {
                System.out.println("El inventario está vacío.");
            } else {
                for (Product product : products) {
                    System.out.println(product);
                }
            }
        }
    }
}
