package test;

public class MirrorSchemaCompare {
  public static void usage() {
    System.out.println("Usage: MirrorSchemaCompare <schema>");
  }

  public static void main(String args[]) {
    
    
    if (args.length < 1) {
      usage();
    } else {
      String[] newArgs = new String[3]; 
      newArgs[0]= "jdbc:db2:localhost";
      newArgs[1]= "QIBM_DB2M_00001";
      newArgs[2] = args[0];
      SchemaCompare.main(newArgs); 
   }
  }
}
