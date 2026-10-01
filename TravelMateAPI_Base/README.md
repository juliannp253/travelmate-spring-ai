# TravelMate AI

API de turismo con Spring Boot y Spring AI.

MySQL almacena el catálogo y la memoria conversacional.
PostgreSQL con pgvector almacena los documentos vectorizados.
OpenRouter proporciona los modelos de chat y embeddings.

## Requisitos

- JDK 25.
- Maven.
- Docker con Docker Compose v2, compatible con `up --wait`.
- Clave API de OpenRouter.
- Acceso a Internet para descargar imágenes, dependencias y utilizar modelos.

Los comandos siguientes se ejecutan desde `TravelMateAPI_Base/`.

## Variables de entorno

Para desarrollo en Bash, define las variables en la terminal donde
ejecutarás Docker Compose y Maven. Sustituye los valores de ejemplo.

```bash
export MYSQL_USER='travelmate'
export MYSQL_PASSWORD='cambia_esta_password_mysql'
export MYSQL_ROOT_PASSWORD='cambia_esta_password_root'
export MYSQL_PORT='3307'
export MYSQL_URL="jdbc:mysql://localhost:${MYSQL_PORT}/travelmate?useSSL=false&serverTimezone=UTC"

export PGVECTOR_USER='travelmate_ai'
export PGVECTOR_PASSWORD='cambia_esta_password_postgresql'
export PGVECTOR_PORT='5433'
export PGVECTOR_URL="jdbc:postgresql://localhost:${PGVECTOR_PORT}/travelmate_ai"

read -r -s -p 'Clave de OpenRouter: ' OPENROUTER_API_KEY
echo
export OPENROUTER_API_KEY
```

MYSQL_ROOT_PASSWORD se utiliza para administrar e importar el SQL.
La aplicación se conecta con MYSQL_USER y MYSQL_PASSWORD.

Opcionalmente puedes definir:

```bash
export SERVER_PORT='8080'
export OPENROUTER_CHAT_MODEL='openrouter/free'
export OPENROUTER_EMBEDDING_MODEL='nvidia/nemotron-3-embed-1b:free'
export OPENROUTER_TIMEOUT='60s'
```

Si ejecutas la aplicación desde un IDE, configura allí también sus
variables de entorno. Las variables exportadas en una terminal no
se aplican automáticamente a un IDE abierto previamente.

Spring Boot no carga automáticamente archivos `.env`.

## Arrancar las bases

```bash
docker compose config --quiet
docker compose up -d --wait
docker compose ps
```

Las bases quedan disponibles en localhost:

- MySQL: puerto 3307 por defecto, base `travelmate`.
- PostgreSQL: puerto 5433 por defecto, base `travelmate_ai`.

Las credenciales de los contenedores se establecen al inicializar
volúmenes vacíos. Cambiar las variables después no cambia las
contraseñas de una base ya inicializada.

## Importar el catálogo inicial

ADVERTENCIA: `../database/travelmate.sql` ejecuta
`DROP DATABASE IF EXISTS travelmate`.
La importación elimina y recrea esa base, incluyendo cualquier tabla
de memoria existente. Úsala para preparar el catálogo de desarrollo
con la aplicación detenida.

Ejecuta una vez, después de arrancar las bases:

```bash
docker compose exec -T mysql sh -c \
  'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" exec mysql -u root' \
  < ../database/travelmate.sql
```

Comprueba la carga:

```bash
docker compose exec -T mysql sh -c \
  'MYSQL_PWD="$MYSQL_PASSWORD" exec mysql -u "$MYSQL_USER" travelmate -e "SELECT COUNT(*) AS paquetes FROM paquetes_turisticos;"'
```

## Compilar y arrancar la aplicación

```bash
mvn -DskipTests compile
mvn spring-boot:run
```

Durante el arranque:

- JPA utiliza MySQL.
- El repositorio JDBC inicializa su tabla de memoria en MySQL.
- PgVectorStore inicializa extensiones y la tabla vectorial en PostgreSQL.
- La tabla vectorial existente se conserva.
- El catálogo todavía no se vectoriza automáticamente.

La cuenta PostgreSQL creada por esta imagen de desarrollo permite
inicializar las extensiones necesarias.

## Comprobar las tablas

Con la aplicación arrancada, abre otra terminal en esta carpeta:

```bash
docker compose exec -T mysql sh -c \
  'MYSQL_PWD="$MYSQL_PASSWORD" exec mysql -u "$MYSQL_USER" travelmate -e "SHOW TABLES;"'
```

Debe aparecer la tabla de memoria `SPRING_AI_CHAT_MEMORY`
junto con las tablas del catálogo.

```bash
docker compose exec -T pgvector sh -c \
  'exec psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "\d public.vector_store"'
```

La columna `embedding` debe tener tipo `vector(2048)`.

## Preparación del índice del catálogo

La fase 1 crea la infraestructura y la tabla vectorial.

La fase 2 implementará `POST /api/ai/index/rebuild`, protegido por un
secreto administrativo. La primera carga se ejecutará explícitamente
después de importar el catálogo y comprobar que el modelo de embeddings
está disponible y produce 2048 dimensiones.

Se utiliza búsqueda exacta (`NONE`), sin índice HNSW.
Un cambio del modelo de embeddings exige reconstruir los vectores.
Un cambio de dimensión también exige recrear la tabla vectorial.

## Modelos gratuitos

Los modelos iniciales son:

- Chat: `openrouter/free`.
- Embeddings: `nvidia/nemotron-3-embed-1b:free`.

Su disponibilidad y límites dependen de OpenRouter y sus proveedores.
La configuración no establece una alternativa automática de pago.
Mantén las variables de modelos apuntando a rutas gratuitas.

El arranque por sí solo no demuestra que las llamadas al proveedor
funcionen. La validación de embeddings se realizará antes de indexar.

## Detener las bases

Detén primero la aplicación y luego ejecuta:

```bash
docker compose down
```

Los volúmenes conservan los datos.
La opción `down -v` elimina los volúmenes y sus datos.