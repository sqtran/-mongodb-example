package steve;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings; 
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class App0 {

	private static final String JDBC_URL = "mongodb://example.io:27017"; // example.io = 172.17.0.2
	private static final String DATABASE = "simpsons";

	public static void main(String[] args) {

		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

		MongoClientSettings clientSettings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(JDBC_URL)).codecRegistry(codecRegistry).build();

		// This method works too, but doesn't use the codecs
		// MongoClient mc = MongoClients.create(JDBC_URL);

		MongoClient mc = MongoClients.create(clientSettings);

		System.out.println("Databases:\n");
		for (String i : mc.listDatabaseNames()) {
			System.out.println(i);
		}

		MongoDatabase db = mc.getDatabase(DATABASE);

		System.out.println(String.format("\n\nDatabase=%s collections:\n", DATABASE));
		for (String i : db.listCollectionNames()) {
			System.out.println(i);
		}

		System.out.println("\n-------------------------------------------------------\n");

		MongoCollection<Person> collection = db.getCollection("person", Person.class);

		Person character = collection.find().first();
		System.out.println("First character: " + character.toString() + "\n");

		character = collection.find(eq("firstName", "Milhouse")).first();
		System.out.println("Character by name: " + character.toString() + "\n");
	 
		System.out.println("All characters: ");
		MongoCursor<Person> cursor = collection.find().iterator();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}


	}
}