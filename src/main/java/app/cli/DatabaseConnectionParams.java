package app.cli;

public class DatabaseConnectionParams {

    private String hostName;
    private int port;
    private String dataBaseName;

    public DatabaseConnectionParams(String hostName, int port, String dataBaseName) {
        this.hostName = hostName;
        this.port = port;
        this.dataBaseName = dataBaseName;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }
}