package de.dpunkt.myaktion.model;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpendeListMBR implements MessageBodyReader<List<Spende>> {

    @Override
    public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2,
	    MediaType arg3) {
	return true;
    }

    @Override
    public List<Spende> readFrom(Class<List<Spende>> arg0, Type arg1,
	    Annotation[] arg2, MediaType arg3,
	    MultivaluedMap<String, String> arg4, InputStream is)
	    throws IOException, WebApplicationException {
	JsonReader reader = Json.createReader(is);
	JsonArray values = reader.readArray();
	List<Spende> spendeList = new ArrayList<>(values.size());
	for (JsonValue value : values) {
	    if (value.getValueType() == ValueType.OBJECT) {
		Spende spende = new Spende();
		JsonObject object = (JsonObject) value;
		spende.setBetrag(object.getJsonNumber("betrag").doubleValue());
		spende.setSpenderName(object.getString("spenderName"));
		spendeList.add(spende);
	    }
	}
	reader.close();
	return spendeList;
    }

}
