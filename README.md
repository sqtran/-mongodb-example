# mongodb-example
Super simple example for connecting to a MongoDB instance and running some queries.

## Containerized MongoDB
```bash
docker run -it --name mangodb -d mongo:4.2
```
## Setup
`mongodb` contains some sample data that can be inserted into the database.

All the code in this repository assumes the database is named `simpsons`.

If your MongoDB is running inside of a container, you can connect to it with 
```bash
docker exec -it mangodb bash
```

Once inside the container, type `mongo` to open an interactive session.  Now use your normal MongoDB commands.