# Sistema de Gestión de Restaurante 🍽️ (Java)

Sistema completo de gestión para restaurantes desarrollado en **Java** con arquitectura en capas, aplicando principios sólidos de Programación Orientada a Objetos.

## 📋 Características

- **Gestión de Mesas**: Registro y control de ocupación de mesas
- **Gestión de Personal**: Administración de meseros  
- **Menú Digital**: Registro de platillos y bebidas
- **Sistema de Pedidos**: Creación y seguimiento de órdenes
- **Procesamiento de Pagos**: Cálculo automático de impuestos y registro de pagos
- **Reportes**: Análisis de ventas diarias y estadísticas
- **Persistencia**: Almacenamiento automático en archivos JSON con Gson

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura en capas** (Layered Architecture):

```
ProyectoFinal/
├── src/main/java/com/restaurant/
│   ├── models/           # Entidades del dominio
│   │   ├── Person.java
│   │   ├── Waiter.java
│   │   ├── MenuItem.java (abstracta)
│   │   ├── FoodItem.java
│   │   ├── DrinkItem.java
│   │   ├── Table.java
│   │   ├── Order.java
│   │   └── OrderStatus.java (enum)
│   ├── services/         # Lógica de negocio
│   │   ├── FileStorage.java
│   │   ├── RestaurantManager.java
│   │   └── ReportService.java
│   └── Main.java         # Aplicación principal
├── data/                 # Archivos JSON de persistencia
├── lib/                  # Librerías (Gson)
├── bin/                  # Archivos compilados (.class)
├── pom.xml               # Configuración Maven
├── diagram.mermaid       # Diagrama UML
└── README.md             # Este archivo
```

## 🎯 Principios de POO Implementados

### 1. **Herencia**
- `Waiter` hereda de `Person`
- `FoodItem` y `DrinkItem` heredan de `MenuItem`

```java
public class Waiter extends Person {
    private String employeeId;
    
    public Waiter(String id, String name, String phone, String employeeId) {
        super(id, name, phone);
        this.employeeId = employeeId;
    }
}
```

### 2. **Abstracción**
- `MenuItem` es una clase abstracta que define la interfaz para items del menú
- Método abstracto `calculateTotal(int quantity)` debe ser implementado por subclases

```java
public abstract class MenuItem {
    private String id, name;
    private double price;
    
    public abstract double calculateTotal(int quantity);
}
```

### 3. **Enumeradores**
- `OrderStatus` define los estados posibles de un pedido

```java
public enum OrderStatus {
    PENDING("pending"),
    PAID("paid");
}
```

### 4. **Composición**
- `Order` contiene una lista de `MenuItem`
- Clase interna `OrderItem` para encapsular item + cantidad

```java
public class Order {
    private List<OrderItem> items;
    
    public static class OrderItem {
        private MenuItem item;
        private int quantity;
    }
}
```

### 5. **Encapsulación**
- Atributos privados con getters/setters públicos
- Validaciones en métodos de negocio

## 🚀 Compilación y Ejecución

### Requisitos Previos
- Java JDK 11 o superior
- Biblioteca Gson (incluida en `lib/`)

### Compilar el Proyecto

**En Linux / Mac:**
```bash
# Compilar todas las clases
javac -cp "lib/gson-2.10.1.jar" -d bin -sourcepath src/main/java src/main/java/com/restaurant/**/*.java src/main/java/com/restaurant/*.java
```

**En Windows:**
```cmd
# Compilar todas las clases
javac -cp "lib/gson-2.10.1.jar" -d bin -sourcepath src/main/java src/main/java/com/restaurant/**/*.java src/main/java/com/restaurant/*.java
```

### Ejecutar el Sistema

**En Linux / Mac:**
```bash
# Ejecutar la aplicación principal
java -cp "bin:lib/gson-2.10.1.jar" com.restaurant.Main
```

**En Windows:**
```cmd
# Ejecutar la aplicación principal (Nota el punto y coma)
java -cp "bin;lib/gson-2.10.1.jar" com.restaurant.Main
```

### Script de Ejecución Rápida

```bash
# Crear script run.sh
echo '#!/bin/bash' > run.sh
echo 'java -cp "bin:lib/gson-2.10.1.jar" com.restaurant.Main' >> run.sh
chmod +x run.sh

# Ejecutar
./run.sh
```

## 📖 Guía de Uso

### 1. Configuración Inicial (Admin)

**Registrar Mesas:**
```
Menú Principal > Menú Administrador > Registrar Mesa
Ejemplo: Mesa #1, Capacidad: 4 personas
```

**Registrar Meseros:**
```
Menú Principal > Menú Administrador > Registrar Mesero
Ejemplo: ID: W001, Nombre: Juan Pérez, Teléfono: 555-1234, Employee ID: EMP001
```

**Registrar Platillos:**
```
Menú Principal > Menú Administrador > Registrar Platillo
Ejemplo: ID: F001, Nombre: Tacos al Pastor, Precio: $85.00, Tiempo Prep: 15 min
```

**Registrar Bebidas:**
```
Menú Principal > Menú Administrador > Registrar Bebida
Ejemplo: ID: D001, Nombre: Agua de Horchata, Precio: $35.00, Tamaño: Grande
```

### 2. Flujo de Pedido (Mesero)

1. **Crear Pedido**: Asignar a mesa y mesero
2. **Agregar Items**: Seleccionar platillos y bebidas con cantidades
3. **Ver Total**: El sistema calcula automáticamente subtotal + IVA (16%)

### 3. Procesamiento de Pago (Cajero)

1. Seleccionar pedido pendiente
2. Revisar el total
3. Confirmar pago
4. La mesa se libera automáticamente

### 4. Reportes (Admin)

- **Ventas Diarias**: Total de ventas con desglose
- **Ventas por Mesero**: Rendimiento individual
- **Items Populares**: Productos más vendidos

## 💾 Persistencia de Datos

El sistema utiliza **Gson** para serialización/deserialización JSON:

- **Automático**: Guarda en cada operación de creación/modificación
- **Al salir**: Guarda todo al seleccionar "Salir"
- **Carga automática**: Lee datos existentes al iniciar

### Formato de Datos

#### data/tables.json
```json
[
  {
    "tableNumber": 1,
    "capacity": 4,
    "isOccupied": false
  }
]
```

#### data/menu_items.json
```json
[
  {
    "type": "food",
    "id": "F001",
    "name": "Tacos al Pastor",
    "price": 85.0,
    "category": "Plato Fuerte",
    "preparationTime": 15
  },
  {
    "type": "drink",
    "id": "D001",
    "name": "Agua de Horchata",
    "price": 35.0,
    "category": "Bebida",
    "size": "Grande"
  }
]
```

## 🔧 Reglas de Negocio

1. **Tasa de Impuesto**: 16% (IVA) aplicado automáticamente
2. **Estados de Pedido**: 
   - `PENDING`: Pedido creado, esperando pago
   - `PAID`: Pedido pagado y completado
3. **Ocupación de Mesas**:
   - Se marca como ocupada al crear un pedido
   - Se libera automáticamente al procesar el pago
4. **Validaciones**:
   - No se pueden crear mesas duplicadas
   - No se pueden crear pedidos sin mesa o mesero válidos
   - No se pueden agregar items inexistentes a pedidos

## 📊 Diagrama UML

El archivo `diagram.mermaid` contiene el diagrama de clases completo mostrando:

- ✅ Todas las clases con atributos y métodos
- ✅ Relaciones de herencia
- ✅ Composición
- ✅ Enumeradores
- ✅ Clases abstractas

Visualizar en:
- [Mermaid Live Editor](https://mermaid.live/)
- GitHub (auto-renderizado)
- VSCode con extensión Mermaid

## 🎓 Conceptos Java Demostrados

- ✅ **Herencia**: `extends`
- ✅ **Abstracción**: Clases abstractas con `abstract`
- ✅ **Enumeradores**: `enum` con valores
- ✅ **Composición**: Clases que contienen otras clases
- ✅ **Encapsulación**: Modificadores de acceso `private`/`public`
- ✅ **Polimorfismo**: Serialización polimórfica con Gson
- ✅ **Generics**: `List<T>`, `Map<K,V>`
- ✅ **Streams**: Java 8+ para filtrado y búsqueda
- ✅ **Inner Classes**: `Order.OrderItem`
- ✅ **Exception Handling**: `try-catch` y validaciones

## 🏛️ Patrones de Diseño

- **Repository Pattern**: `FileStorage` para persistencia
- **Service Layer**: `RestaurantManager`, `ReportService`
- **Builder Pattern**: Constructores encadenados
- **Factory Pattern**: `RuntimeTypeAdapterFactory` para polimorfismo JSON

## 📦 Dependencias

- **Gson 2.10.1**: Serialización/deserialización JSON
  - Manejo de polimorfismo con `RuntimeTypeAdapterFactory`
  - Pretty printing para legibilidad
  - Soporte para tipos genéricos con `TypeToken`

## 🤝 Contribución

Este es un proyecto educativo. Para mejoras o extensiones:

1. Mantén la separación de capas
2. Sigue los principios OOP establecidos
3. Actualiza el diagrama UML si agregas clases
4. Documenta nuevas funcionalidades

## 📝 Licencia

Proyecto académico - Programación Orientada a Objetos

## 👨‍💻 Autor

Desarrollado como proyecto final de POO en Java

---

**¡Disfruta gestionando tu restaurante con Java! ☕🍴**
