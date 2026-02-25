# Guía para subir el proyecto a GitHub

Este documento explica cómo inicializar y subir este proyecto a un repositorio en GitHub usando la herramienta de línea de comandos `gh` (GitHub CLI) y `git`.

## Requisitos Previos

1. Tener instalado `git`.
2. Tener instalado `gh` (GitHub CLI).
3. Haber iniciado sesión en `gh` (`gh auth login`).

## Pasos para subir el proyecto

A continuación, se detallan los comandos exactos que se emplean para publicar el proyecto:

### 1. Inicializar el repositorio Git
Primero, asegúrate de estar en la carpeta raíz del proyecto y ejecuta:
```bash
git init
```

### 2. Agregar los archivos al área de preparación (staging)
El archivo `.gitignore` ya está configurado para omitir archivos binarios y compilados (`/target`, `/bin`, `*.class`, etc.). Para añadir el resto:
```bash
git add .
```

### 3. Crear el primer commit
Guarda el estado actual de los archivos añadiendo un mensaje de confirmación:
```bash
git commit -m "Initial commit"
```

### 4. Crear el repositorio en GitHub y subir el código
Usa GitHub CLI para crear el repositorio remoto, enlazarlo y subir los archivos. Puedes elegir si hacerlo público o privado:

**Para un repositorio público:**
```bash
gh repo create NombreDelProyecto --public --source=. --remote=origin --push
```

**Para un repositorio privado:**
```bash
gh repo create NombreDelProyecto --private --source=. --remote=origin --push
```

Una vez que se complete este comando, tu proyecto estará subido y disponible en tu cuenta de GitHub.
