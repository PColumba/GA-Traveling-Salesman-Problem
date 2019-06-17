import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Stream;

public class CitiesFromFile{

	static public Coordinates[] readFrom(String fileName) throws IOException,NumberFormatException{
		BufferedReader bufferedFileReader = new BufferedReader(new FileReader(fileName));
		Stream<Coordinates> coordinatesStream;
		coordinatesStream = bufferedFileReader.lines().map(line -> {
			String[] strCoords = line.split(";");
			return new Coordinates(Integer.valueOf(strCoords[0]), Integer.valueOf(strCoords[1]));
			});	
		Coordinates[] result = coordinatesStream.toArray(Coordinates[]::new);
		bufferedFileReader.close();	
		return result;	
	}
}