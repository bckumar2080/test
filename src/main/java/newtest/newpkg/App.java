package newtest.newpkg;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
                Object json = null;
        ObjectMapper mapper1 = new ObjectMapper();
	     ObjectWriter writer = mapper1.writer(new DefaultPrettyPrinter());
	     writer.writeValue(new File("C:\\Applications\\jenkins\\workspace\\newApi.txt"),json);
        System.out.println(json);
    }
}
