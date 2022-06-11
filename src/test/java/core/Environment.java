package core;

public class Environment {

    //bootstrap
    public static final String basePetURI = PropertiesReader.readPropertyFile("baseURI") + "pet/";
    public static final String basePetStoreURI =  PropertiesReader.readPropertyFile("baseURI") + "store/";
    public static final String petFileBodiesRoot =  PropertiesReader.readPropertyFile("petFileBodiesRoot");
    public static final String userURI =  PropertiesReader.readPropertyFile("baseURI") + "user/";
}
