# Backend Developer Challenge
Challenge made for ClickBus backend-developer position.


## Instalation and usage

### Requirements
You must have `Docker` and `Docker-compose` installed in your machine.

1. Go to folder `docker` within the project.
2. Run the follow command:
```
docker-compose up
```

The project will:
1. Run maven to compile, test and generate a executable .jar.
2. Generate a docker image from above's jar.
3. Configure and install a MySQL's docker image.
4. Run the images to start-up the project.

## Documentation
After boot-up, the documentation can be found in the [documentation page](http://localhost:8080/docs/api-guide.html).