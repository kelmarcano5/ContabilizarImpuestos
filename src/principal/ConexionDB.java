package principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {
	
	protected Connection cn;
	private String host="";
	private String port ="";
	private String dbName="";
	private String user="";
	private String pass="";
	
	public ConexionDB(String host, String port, String dbName, String user, String pass) {
		super();
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		this.user = user;
		this.pass = pass;
	}
	
	public ConexionDB() {
		this.conectardb();
	}
	
	public Connection Cn() {
		return cn;
	}

	public void setCn(Connection cn) {
		this.cn = cn;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void conectardb(){
		
		String linea;
		String cadena="";
		
		try {
			FileReader f = new FileReader("config.txt");
			BufferedReader b = new BufferedReader(f);
			
		    while((linea = b.readLine())!=null) {
		    	cadena+=linea;
		    }
		    b.close();
		    
		    String[] array = cadena.split(";");
		    
		    //HOST
		    String[] hst = array[0].split("=");
	        this.setHost(hst[1]);
	          
		    //PUERTO
		    String[] prt = array[1].split("=");
	        this.setPort(prt[1]);

	        //DATABASE NAME
		    String[] dbn = array[2].split("=");
	        this.setDbName(dbn[1]);

		    //USER NAME
		    String[] usr = array[3].split("=");
	        this.setUser(usr[1]);
	          
		    //PASSWORD
		    String[] pwd = array[4].split("=");
	        this.setPass(pwd[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        try {
        	
            // Registramos el driver de PostgresSQL
            try { 
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            Connection connection = null;
            
            // Conectamos con la base de datos
            connection = DriverManager.getConnection("jdbc:postgresql://"+this.host+":"+this.port+"/"+this.dbName,this.user,this.pass);
            
            boolean valid = connection.isValid(50000);
            System.out.println(valid ? "Connection OK" : "Connection FAIL");
            this.cn=connection;
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
    }
}
