# Booksy - app de catalogo de libros

## Integrantes

- **Vicente Quezada** - diseño de interfaz, pantallas de login y home
- **Oscar Baez** - conexion con la api, base de datos local y camara



## que hace la app

es una app sencilla para ver y guardar libros. se pueden:
- crear una cuenta
- iniciar sesion
- ver un catalogo de libros
- cambiar la foto de perfil con la camara o galeria
- cerrar sesion



## tecnologias que usamos

- **lenguaje:** kotlin
- **ui:** jetpack compose
- **base de datos:** local
- **api:** retrofit
- **navegacion:** navigation compose
- **camara:** accompanist permissions + activityresultcontracts



## como correr el proyecto

### requisitos
- android studio
- un celular o emulador
- internet (para login y registro)

### pasos

1. clonar el repo:  
   `git clone https://github.com/oscarinpinpin/Booksy.git`
2. abrirlo en android studio
3. correr la app



## estructura del proyecto  
app/src/main/java/com/booksy/
├── data/
│ ├── local/ # base de datos
│ ├── remote/ # conexion con la api
│ └── models/ # clases como user, book, etc
├── ui/
│ ├── screens/ # pantallas de la app
│ └── theme/ # colores y estilos
├── viewmodel/ # logica de cada pantalla
├── navigation/ # navegacion entre pantallas
└── MainActivity.kt

## funcionalidades

### login
- campos: email y contraseña
- valida que el email tenga formato correcto y la contraseña minimo 8 caracteres
- muestra errores si algo esta mal
- boton para ir al registro

### registro
- nombre, email, contraseña y confirmar contraseña
- valida que no esten vacios y que las contraseñas coincidan
- si todo ok, crea la cuenta y entra a la app

### home
- lista de libros (por ahora estan fijos)
- cada libro muestra titulo, autor, precio y categoria
- boton para ir al perfil

### perfil
- muestra nombre y correo
- foto de perfil (por defecto un icono)
- se puede cambiar con camara o galeria
- boton para cerrar sesion

### base de datos local
se guarda el usuario en el telefono para no tener que loguearse cada vez
- tabla `user` (id, email, name, token, profileImagePath)
- guarda al usuario al iniciar sesion
- si cierras sesion, borra los datos

### conexion con la api
usa una api externa para:
- registrarse
- hacer login
- obtener datos del usuario

## api usada

**url base:** `https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW`
## Endpoints usados

| Metodo | Ruta        | Body                            | Respuesta                          | Errores comunes                  |
|--------|-------------|----------------------------------|------------------------------------|----------------------------------|
| POST   | /auth/signup | { email, password, name }       | { authToken, user_id }             | 400 (datos invalidos), 403 (ya existe) |
| POST   | /auth/login  | { email, password }             | { authToken, user_id }             | 401 (credenciales invalidas)     |
| GET    | /auth/me     | - (con header Authorization)    | { id, email, name }                | 401 (token invalido o expirado)  |


### registro  
POST /auth/signup
{
"email": "usuario@mail.com
",
"password": "asdasd123",
"name": "juan perez"
}


### login  
POST /auth/login
{
"email": "usuario@mail.com
",
"password": "asdasd123"
}

**credenciales de prueba:**
- email: `oscarin@hotmail.es`
- contraseña: `asdasd12`



## problemas y soluciones

**1. api rechazaba contraseñas**  
 cambiamos la validacion de 6 a 8 caracteres

**2. error con kapt y jdk 17**  
cambiamos a ksp para compilar room (la db)

**3. api devolvia otros datos**  
actualizamos el modelo authresponse

**4. iconos que no se encontraban**  
agregamos la dependencia de material-icons-extended

---

## lo que aprendimos

- usar compose para interfaces
- conectar retrofit con una api
- guardar datos con room
- usar la camara y permisos
- manejar estados con viewmodel y stateflow
- entender mejor como se navega entre pantallas  




