#!/bin/bash
# ============================================================
# DEMO AUTOMÁTICA - Sistema de Gestión de Restaurante
# Ejecuta TODAS las funcionalidades sin necesidad de input manual
# ============================================================

set -e

# Colores para la salida
GREEN='\033[0;32m'
CYAN='\033[0;36m'
YELLOW='\033[1;33m'
BOLD='\033[1m'
NC='\033[0m'

CLASSPATH="bin:lib/gson-2.10.1.jar"
MAIN_CLASS="com.restaurant.Main"

# Respaldar datos existentes
echo -e "${YELLOW}╔══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║   DEMO AUTOMÁTICA - SISTEMA DE GESTIÓN DE RESTAURANTE      ║${NC}"
echo -e "${YELLOW}╚══════════════════════════════════════════════════════════════╝${NC}"
echo ""

if [ -d "data_backup" ]; then
    rm -rf data_backup
fi
if [ -d "data" ]; then
    cp -r data data_backup
    echo -e "${CYAN}[INFO] Datos actuales respaldados en data_backup/${NC}"
fi

# Limpiar datos para demo limpia
rm -f data/tables.json data/waiters.json data/menu_items.json data/orders.json
echo -e "${CYAN}[INFO] Datos limpiados para demo fresca${NC}"
echo ""

# Función para ejecutar una sección de la demo
run_section() {
    local TITLE="$1"
    local INPUT="$2"
    
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BOLD}  📋 $TITLE${NC}"
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    
    # Ejecutar con input redirigido, filtrando ANSI escape codes de clear screen
    echo "$INPUT" | java -cp "$CLASSPATH" "$MAIN_CLASS" 2>&1 | sed 's/\x1b\[H\x1b\[2J//g'
    
    echo ""
    
    # Limpiar para la siguiente sección si es necesario
    sleep 0.5
}

# ============================================================
# SECCIÓN 1: ADMINISTRADOR - Registrar Mesas
# ============================================================
# Flujo: pause -> mainMenu(1=Admin) -> admin(1=RegMesa) -> datos -> pause -> admin(1) -> datos -> pause -> admin(2=ListMesas) -> pause -> admin(0=Back) -> main(5=Exit)

run_section "1. REGISTRAR MESAS (Admin)" "
1
1
1
4

1
2
6

1
3
2

2

0
5"

# ============================================================
# SECCIÓN 2: ADMINISTRADOR - Registrar Meseros
# ============================================================
run_section "2. REGISTRAR MESEROS (Admin)" "
1
3
W001
Juan Pérez
555-1234
EMP001

3
W002
María García
555-5678
EMP002

4

0
5"

# ============================================================
# SECCIÓN 3: ADMINISTRADOR - Registrar Menú (Comidas y Bebidas)
# ============================================================
run_section "3. REGISTRAR MENÚ - PLATILLOS Y BEBIDAS (Admin)" "
1
5
F001
Tacos al Pastor
85.00
Plato Fuerte
15

5
F002
Enchiladas Suizas
95.00
Plato Fuerte
20

5
F003
Guacamole
55.00
Entrada
5

6
D001
Agua de Horchata
35.00
Bebida
Grande

6
D002
Coca-Cola
28.00
Refresco
Mediano

6
D003
Margarita
120.00
Alcohol
Grande

7

0
5"

# ============================================================
# SECCIÓN 4: MESERO - Crear Pedidos y Agregar Items
# ============================================================
run_section "4. CREAR PEDIDOS Y AGREGAR ITEMS (Mesero)" "
2
1
ORD001
1
W001

2
ORD001
F001
2
F003
1
D001
2
fin

4
ORD001

0
5"

# ============================================================
# SECCIÓN 5: MESERO - Segundo Pedido
# ============================================================
run_section "5. CREAR SEGUNDO PEDIDO (Mesero)" "
2
1
ORD002
2
W002

2
ORD002
F002
1
F001
3
D002
2
D003
1
fin

3

4
ORD002

0
5"

# ============================================================
# SECCIÓN 6: CAJERO - Ver Pendientes y Pagar Pedido 1
# ============================================================
run_section "6. REGISTRAR PAGO DEL PEDIDO ORD001 (Cajero)" "
3
2

1
ORD001
s

3

0
5"

# ============================================================
# SECCIÓN 7: CAJERO - Pagar Pedido 2
# ============================================================
run_section "7. REGISTRAR PAGO DEL PEDIDO ORD002 (Cajero)" "
3
1
ORD002
s

3

0
5"

# ============================================================
# SECCIÓN 8: REPORTES - Ventas Diarias
# ============================================================
run_section "8. REPORTE DE VENTAS DIARIAS (Reportes)" "
4
1


2


3


0
5"

# ============================================================
# SECCIÓN 9: Verificar datos persistidos
# ============================================================
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BOLD}  📋 9. DATOS PERSISTIDOS EN ARCHIVOS JSON${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

echo -e "${CYAN}--- data/tables.json ---${NC}"
cat data/tables.json 2>/dev/null || echo "(archivo no encontrado)"
echo ""

echo -e "${CYAN}--- data/waiters.json ---${NC}"
cat data/waiters.json 2>/dev/null || echo "(archivo no encontrado)"
echo ""

echo -e "${CYAN}--- data/menu_items.json ---${NC}"
cat data/menu_items.json 2>/dev/null || echo "(archivo no encontrado)"
echo ""

echo -e "${CYAN}--- data/orders.json ---${NC}"
cat data/orders.json 2>/dev/null || echo "(archivo no encontrado)"
echo ""

# ============================================================
# SECCIÓN 10: Ejecutar TestRunner
# ============================================================
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BOLD}  📋 10. PRUEBAS AUTOMATIZADAS (TestRunner - 133 tests)${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

java -cp "$CLASSPATH" com.restaurant.TestRunner 2>&1
echo ""

# ============================================================
# Restaurar datos originales
# ============================================================
if [ -d "data_backup" ]; then
    rm -rf data
    mv data_backup data
    echo -e "${CYAN}[INFO] Datos originales restaurados desde backup${NC}"
fi

echo ""
echo -e "${YELLOW}╔══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║   ✅ DEMO COMPLETADA - TODAS LAS FUNCIONALIDADES PROBADAS  ║${NC}"
echo -e "${YELLOW}╚══════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BOLD}Funcionalidades demostradas:${NC}"
echo "  1. ✓ Registrar mesas (HU-01)"
echo "  2. ✓ Registrar meseros"
echo "  3. ✓ Registrar platillos y bebidas (HU-02)"
echo "  4. ✓ Crear pedido con múltiples items (HU-03)"
echo "  5. ✓ Segundo pedido con otro mesero"
echo "  6. ✓ Calcular total con subtotal e impuestos (HU-04)"
echo "  7. ✓ Registrar pago y cambiar estado (HU-05)"
echo "  8. ✓ Consultar pedidos pendientes/pagados (HU-06)"
echo "  9. ✓ Persistencia en archivos JSON (HU-07)"
echo " 10. ✓ Reportes de ventas diarias (HU-08)"
echo " 11. ✓ 133 pruebas unitarias automáticas"
