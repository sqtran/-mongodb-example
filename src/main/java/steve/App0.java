package steve;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.match;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class App0 {

	private static final String JDBC_URL = "mongodb://example.io:27017"; // example.io = 172.17.0.2
	private static final String DATABASE = "simpsons";

	private static MongoDatabase getDatabase() {
		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

		MongoClientSettings clientSettings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(JDBC_URL)).codecRegistry(codecRegistry).build();

		// This method works too, but doesn't use the codecs
		// MongoClient mc = MongoClients.create(JDBC_URL);

		return MongoClients.create(clientSettings).getDatabase(DATABASE);
	}

	public static void main(String[] args) {

		MongoDatabase db = getDatabase();

		System.out.println(String.format("\n\nDatabase=%s collections:\n", DATABASE));
		for (String i : db.listCollectionNames()) {
			System.out.println(i);
		}

		System.out.println("\n-------------------------------------------------------\n");

		MongoCollection<Person> personCollection = db.getCollection("person", Person.class);

		Person character = personCollection.find().first();
		System.out.println("First character: " + character.toString() + "\n");

		character = personCollection.find(eq("firstName", "Milhouse")).first();
		System.out.println("Character by name: " + character.toString() + "\n");

		System.out.println("All characters: ");
		MongoCursor<Person> cursor = personCollection.find().iterator();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		System.out.println("\n-------------------------------------------------------\n");

		MongoCollection<Address> addressCollection = db.getCollection("address", Address.class);

		Address address = addressCollection.find().first();
		System.out.println("First address: " + address.toString() + "\n");

		System.out.println("All addresses: ");
		MongoCursor<Address> cursor3 = addressCollection.find().iterator();
		while (cursor3.hasNext()) {
			System.out.println(cursor3.next());
		}

		MongoCollection<Contact> contactCollection = db.getCollection("contact", Contact.class);

		AggregateIterable<Contact> contactAggregate = contactCollection.aggregate(
			Arrays.asList(
				lookup("address", "address_id", "_id", "address"),
				unwind("$address"),
				limit(1)
			)
		);

		System.out.println("\nFirst contact: " + contactAggregate.iterator().tryNext() + "\n");
		
		contactAggregate = contactCollection.aggregate(
			Arrays.asList(
				lookup("address", "address_id", "_id", "address"),
				unwind("$address"), 
				match(eq("firstName", "Milhouse")),
				limit(1)
			)
		);

		System.out.println("\nContact by name: " + contactAggregate.iterator().tryNext() + "\n");

		contactAggregate = contactCollection.aggregate(
			Arrays.asList(
				lookup("address", "address_id", "_id", "address"),
				unwind("$address")
			)
		);

		MongoCursor<Contact> cursor2 = contactAggregate.iterator();
		while(cursor2.hasNext()) {
			System.out.println(cursor2.next());
		}
	}

}