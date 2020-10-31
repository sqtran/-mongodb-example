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

## Sample Queries

```bash
# Get all from the person database
db.person.find({})

# select all
db.contact.aggregate( [{$lookup: {from:"address", localField:"address_id", foreignField:"_id", as: "address"}}])

# limit 1
db.contact.aggregate( [{$lookup: {from:"address", localField:"address_id", foreignField:"_id", as: "address"}}, {$limit: 1}])

# Find exactly 1
db.contact.aggregate( [{$match: {firstName: "Milhouse"}}, {$limit: 1}, {$lookup: {from:"address", localField:"address_id", foreignField:"_id", as: "address"}} ])
```

## Compile and Run

This demo builds an uber jar so you can just run it after packaging it up.

```bash
mvn clean package
java -jar target/demo-0.1.jar
```