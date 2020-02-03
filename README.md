# ASIO - Importador de datos simulados

Importador de datos simulados para el proyecto Backend SGI (ASIO). Se trata de un proceso batch configurado mediante Spring Batch. 

## Jobs disponibles

Se han configurado los siguientes Jobs:

- `academicInstitutionJob`: job encargado de simular datos de tipo "Academic institution"
- `researcherJob`: job encargado de simular datos de tipo "Researcher"
- `titleDegreeJob`: job encargado de simular datos de tipo "Title - Degree"

Estos jobs se encargan de leer el fichero CSV correspondiente, generar un JSON con los datos y posteriormente insertarlo en un topic de Kafka.

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11 (en caso de querer JDK8: Oracle JDK 8)
* Eclipse JEE 2019-09 con plugins:
** Spring Tools 4
** m2e-apt
** Lombok
* Docker

##  Parámetros de configuración

- app.kafka.input-topic-name: Nombre del topic para los datos de entrada. Valor por defecto: input-data
- app.data.path:Directorio en el que se encuentran los CSV para la carga de datos, en caso de estar vacío se tomarán del classpath. Valor por defecto vacío
- spring.kafka.bootstrap-servers: Dirección del servidor bootstrap de Kafka. Valor por defecto: localhost:29092

## Cómo crear un nuevo Job

Para la creación de Jobs, se deben seguir las instrucciones provistas en la documentación de Spring Batch.

## Ejecución selectiva de jobs

Si se crea un solo empaquetado con varios jobs y solamente se quiere ejecutar uno en cada ejecución, se puede hacer pasando el siguiente parametro a la máquina virtual:

	-Dspring.batch.job.names=job1,job2
	
## Configuración en entornos de (pre)producción

Para la configuración de la ejecución periodica de jobs, se utilizarán las herramientas proporcionadas por los sistemas operativos Windows / Linux en el que se ejecute la aplicación.

Simplemente habría que ordenarle ejecutar el comando necesario. Por ejemplo:

	java -jar -Dspring.batch.job.names=importUserJob simulator-importer-0.0.1-SNAPSHOT.jar
	
No es necesario especificar la clase de inicio de la aplicación, ya que el fichero MANIFEST.MF generado ya contiene la información necesaria. Solamente se especificarán los parametros necesarios.

En entornos más complejos, se pueden usar gestores de cron como por ejemplo JobScheduler: https://www.sos-berlin.com/jobscheduler

## TO-DO

Actualmente la gestión de los jobs se realiza mediante la utilización de una base de datos H2. En caso que sea necesaria una gestión más fina de los lanzamientos se podría externalizar a una base de datos relacional o bien una MongoDB, en este último caso requeriría más configuración.