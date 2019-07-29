# Movies Database
#### Arquitecura
Usé la arquitectura MVVM primordialmente porque Los ViewModels respetan el ciclo de vida tanto de las Actividades como de los Fragmentos. También porque se eliminan los contratos y se negocia directamente con esa capa para saber si hacer descargas de datos o mostrar los datos offline.

#### Capa de red
La capa de red la llamé remote.
Usé Retrofit junto con las coroutines para obtener los datos desde la api de dbmovies.
Use la interfaz Service para saber que tipo de consultas y a exactamente a donde apuntar para la obtención de datos.
El objeto Network para realizar las llamadas.
Usé la clase ApiStatus que es una clase de constantes ENUM para determinar que me esta respondiendo el servicio web.
Luego tenemos los contenedores para recibir distintos datos y sus funciones para mappear a la capa de persistencia.

#### Capa de persistencia
La capa de persistencia la llamé database.
Usé Room para guardar los datos obtenidos desde la capa de red.
Hice dos interfaces la de Movie y Serie que son DAO (Data Access Object) para obtener o agregar datos con distintos queries
Hice distintas Entidades donde en Movies:
Se guardan categorías, películas, misma película en distintas categorías y videos.
Hice disntitnas Entidades donde en Series:
Se guardan categorías, series, misma serie en distintas categorías y videos.

La base de datos tiene accesso a las DAO para poder obtener los datos de las entidades y tiene las funciones para poder mappear los datos a la capa de dominio.

#### Capa de dominio
La capa de dominio la llamé domain.
Son los datos finales que obtiene la capa de negocio para mostrarlos en la capa de vistas.

#### Capa de negocio
La capa de negocio la llamé data
Hace llamadas online e interactúa con sus respectivos observadores con la ayuda de LiveData que escucha los cambios que se vayan presentando en la capa de persistencia los cuales tienen un efecto en la capa de vistas.
MoviesRepository hace llamadas a las categorías, películas y videos.
SeriesRepository hace llamadas a las categorías, series y videos.

#### Capa de vistas
La capa de vistas la llamé presentation
Que en realidad es una mezcla de la de presentación y vistas porque tiene a los ViewModels y a las Actividades y Fragmentos
Los ViewModels se encargan de hacer que la capa de negocio haga su trabajo y las vistas de escuchar cambios y modificar los adaptadores con los distintos datos que vayan obteniendo

#### ¿En qué consiste el principio de responsabilidad única? ¿Cuál es su propósito?
Consiste en que se delegue una sola función a cada clase, esto para cuando se agreguen funcionalidades no afecte a otras clases y solo a esta, para ello se dividen en capas para que cada una de ellas haga una función en específico.

#### ¿Qué características tiene, según su opinión, un “buen” código o código limpio?
Que se comprenda cuales son las responsabilidades que esta realizando cada clase y que los nombres hagan sentido a lo que se esta cumpliendo (Single responsibility).
Que se puedan instanciar clases y que cumplan diferentes funciones en mi caso MoviesRepository es usado para obtener videos, categorías y películas por dos distintas vistas (Open/ Closed).
Que se pueda heredar desde una clase y poder usarla como su padre en este caso se heredan desde un Serializable para poder obtener los distintos datos de una vista a otra de manera sencilla esto se realiza con películas, series y videos (Liskov substitution).
Por ello eliminé los contratos para evitar que las actividades dependan de cosas que no necesitan y que los ViewModels decidan que necesitan y que no de los repositorios (Interface segregation).
Evitar dependencias, en esta aplicación pueden existir catregorías sin películas y viceversa al igual con las series (Dependency inversion).

Entonces los principios SOLID y también una arquitectura que separe estos principios para que se tenga una mayor comprensión estas arquitecutas pueden ser MVVM o MVP