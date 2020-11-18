# Job Scheduler Manager
Dynamic quartz scheduler with SpringBoot and Quartz 

### Build Status

#### Code Quality
* [![Codacy Badge](https://app.codacy.com/project/badge/Grade/ee4157bda60d4dc38e1416defc6b48d1)](https://www.codacy.com/gh/josousa82/Job-Scheduler-Manager-SpringBoot/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=josousa82/Job-Scheduler-Manager-SpringBoot&amp;utm_campaign=Badge_Grade)

#### CircleCI
* [![<CircleCI>](https://circleci.com/gh/josousa82/Job-Scheduler-Manager-SpringBoot.svg?style=shield)](https://circleci.com/gh/josousa82/Job-Scheduler-Manager-SpringBoot/)
  

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them
to be able to check the result of the job that you are running as well for testing , 
MailHog or another SMTP server should be running on host 127.0.0.0 port 1025.

There are two options to setup MailHog to test the app. You can use either the docker-compose
file on the project or run the command below on the terminal. 
Docker must be installed in the machine.

Pulling the image:
```
docker run -d --name mailhog -p 8025:8025 -p 1025:1025 mailhog/mailhog

```

Running from docker-compose:
```
// To start
docker-compose up -d mailhog

// To Stop 
docker-compose down

```

### Installing

After having the SMTP server running, just run the APP through IDEA 
or with maven, running in the terminal:

```
$ ./mvnw clean spring-boot:run
```

To check the local H2 DB just go to :

```
http://localhost:8080/h2-console/

JDBC URL :  jdbc:h2:mem:jobScheduler

User: admin
No pass 
(change the user and pass on the application.properties file)
```

* [Open API link (Swagger) to test endpoints](http://localhost:8080/swagger-ui.html#)

- http://localhost:8080/swagger-ui.html#

- Cron expression generator 
* http://www.cronmaker.com


Create a new Job:

```
{
  "name": "Sender",
  "subject": "Report email",
  "messageBody": "Sample report email",
  "to": ["example@example.com"],
  "triggers":
    [
       {
         "name": "manager",
         "group": "email",
         "cron": "0/10 * * * * ?"
       }
    ]
}
```


Update a job :

```
{
  "name": "manager",
  "subject": "Daily Report emai",
  "messageBody": "Daily sample report email",
  "to": ["example@example.com", "example2@example.com", "example2@example.com"],
  "cc": ["example@example.co"]
}
```
## Running the tests

WIP


### Break down into end to end tests

WIP

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [SpringBoot](https://spring.io/) - MVC Framework
* [Quartz scheduler](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-quartz) - 
* [Swagger](https://swagger.io/tools/swagger-ui/) 
* [Docker](https://www.docker.com/products)
* [MailHog](https://github.com/mailhog/MailHog) - SMTP fake server



## Contributing

Please read [CONTRIBUTING.md](link to contibuitions file) for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

jira test

