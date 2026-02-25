# Guía Rápida - Sistema de Restaurante (Java)

## 🚀 Iniciar el Sistema

**En Linux / Mac:**
```bash
cd /home/chelo/POO/ProyectoFinal
./run.sh
```

**En Windows:**
```cmd
cd ruta\hacia\ProyectoFinal
java -cp "bin;lib/gson-2.10.1.jar" com.restaurant.Main
```

## 📦 Compilar (si es necesario)

**En Linux / Mac:**
```bash
javac -cp "lib/gson-2.10.1.jar" -d bin -sourcepath src/main/java \
  src/main/java/com/restaurant/**/*.java \
  src/main/java/com/restaurant/*.java
```

**En Windows:**
```cmd
javac -cp "lib/gson-2.10.1.jar" -d bin -sourcepath src/main/java src/main/java/com/restaurant/**/*.java src/main/java/com/restaurant/*.java
```

## 📂 Estructura del Proyecto

```
src/main/java/com/restaurant/
├── models/           → Entidades (Person, Waiter, MenuItem, Order, Table)
├── services/         → Lógica de negocio (Manager, Storage, Reports)
└── Main.java         → Aplicación principal

lib/                  → Gson JAR
bin/                  → Clases compiladas  
data/                 → Archivos JSON de persistencia
diagram.mermaid       → Diagrama UML
README.md             → Documentación completa
```

## 🔄 Flujo de Trabajo Típico

### 1. Primera Vez (Admin)
1. **Administrador → Registrar Mesa**
   - Número: 1, Capacidad: 4
2. **Administrador → Registrar Mesero**
   - ID: W001, Nombre: Juan, Employee ID: EMP001
3. **Administrador → Registrar Platillo**
   - ID: F001, Nombre: Tacos, Precio: $85, Tiempo: 15 min
4. **Administrador → Registrar Bebida**
   - ID: D001, Nombre: Agua, Precio: $35, Tamaño: Grande

### 2. Atender Clientes (Mesero)
1. **Mesero → Crear Pedido**
   - ID: ORD001, Mesa: 1, Mesero: W001
2. **Mesero → Agregar Items**
   - 2x Tacos (F001)
   - 1x Agua (D001)
3. Ver el **total automático con 16% IVA**

### 3. Cobrar (Cajero)
1. **Cajero → Registrar Pago**
   - Seleccionar pedido ORD001
   - Confirmar pago
2. **Mesa se libera automáticamente**

### 4. Reportes (Admin)
1. **Reporte de Ventas Diario**
2. **Ventas por Mesero**
3. **Items Más Populares**

## ✅ Principios POO Implementados

| Principio | Implementación |
|-----------|----------------|
| **Herencia** | `Waiter extends Person`<br>`FoodItem/DrinkItem extends MenuItem` |
| **Abstracción** | `abstract class MenuItem`<br>`abstract double calculateTotal(int)` |
| **Enumeradores** | `enum OrderStatus { PENDING, PAID }` |
| **Composición** | `Order` contiene `List<OrderItem>`<br>`OrderItem` contiene `MenuItem` |
| **Encapsulación** | Atributos `private`, getters/setters `public` |

## 📊 Archivos de Datos

Los datos se guardan automáticamente en `data/`:

- `tables.json` - Mesas del restaurante
- `waiters.json` - Meseros registrados
- `menu_items.json` - Platillos y bebidas (con polimorfismo)
- `orders.json` - Historial de pedidos

## 🔍 Verificar Compilación

```bash
# Ver clases compiladas
ls -R bin/com/restaurant/

# Verificar que Gson está disponible
ls -lh lib/gson-2.10.1.jar
```

## 🎯 Características Especiales

- **Persistencia Automática**: Guarda tras cada operación
- **Carga Automática**: Recupera datos al iniciar
- **Cálculo Automático**: Subtotal + IVA (16%) = Total
- **Gestión de Mesas**: Ocupadas al crear pedido, liberadas al pagar
- **Validaciones**: No duplicados, referencias válidas
- **Polimorfismo JSON**: FoodItem/DrinkItem se guardan correctamente

## 🐛 Solución de Problemas

### Error: "Cannot find symbol Streams"
Ya resuelto - agregado `import com.google.gson.internal.Streams`

### Error: "ClassNotFoundException: com.restaurant.Main"
**En Linux/Mac:**
```bash
# Verificar classpath al ejecutar
java -cp "bin:lib/gson-2.10.1.jar" com.restaurant.Main
```

**En Windows:**
*(Nota: En Windows se usa punto y coma `;` en lugar de dos puntos `:`)*
```cmd
java -cp "bin;lib/gson-2.10.1.jar" com.restaurant.Main
```

### Los datos no se guardan
Verificar que el directorio `data/` existe y tiene permisos de escritura

## 📚 Conceptos Java Destacados

- Classes abstractas con métodos abstractos
- Enums con constructores y métodos
- Inner classes (`Order.OrderItem`)
- Generics (`List<T>`, `Map<K,V>`)
- Streams API (`filter`, `findFirst`)
- Try-catch para manejo de errores
- File I/O con NIO
- Gson para JSON (con `TypeAdapter` personalizado)

## 📖 Más Información

Ver el archivo [README.md](file:///home/chelo/POO/ProyectoFinal/README.md) para documentación completa.

---

**Proyecto académico - POO en Java** ☕
