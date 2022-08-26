# registration-customer
## It is a register and manipuliation of customers into system

## Setup required

* A actual version of docker
* An actual version of docker-compose

## How to use

There are three profiles in this project:
* **dev**
* **homolog**
* **prod**

To develop your must use the **dev** profile, it's configurated by default, use an postgresql instance of your preference and create a database named **customer** with the **URL** and **credentials** seted in *./src/main/resources/application-dev.yml* or if you prefer change the configuration into file.

To validate this application you must use de **homolog** profile, in this case there is a docker-compose.yml file that publish an funtional instance of the application with the database seted for you, for that into your terminal (bash or CDM) from project root path follow the steeps:

* mvnw clean package -P homolog 
* docker-compose build
* docker-compose up -d

To production you must from the project root path  follow the steeps:

* mvnw clean package -P prod
* docker build .
* docker run -P 'your_output_port':8081 -e CUSTOMER_DATASOURCE_URL='your_database_access_url' -e CUSTOMER_DATASOURCE_USER='your_database_user' -e CUSTOMER_DATASOURCE_PASSWORD='you_database_password' --restart=always -d .

# Application tests

To run the tests of application just run command from from project root path:

* ./mvnw tests

# API documentation

## The projec has a complete API doc on **URL**:

'your_aplication_path':'your_application_port'/api/swagger-ui/index.html#/customer-resource

## The project has a complete endpoints list to import into Postman by import/link on **url**:

'your_aplication_path':'your_application_port'/api/v3/api-docs

