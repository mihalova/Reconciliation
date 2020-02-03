package rec;

public class Reconciliation {
    public static String directoryPath;

    public static void main(String[] args){
        if (args.length != 3 || args[1].length()!=1 || "pdra".indexOf(args[1].charAt(0))<0) {
            throw new IllegalArgumentException("Start the program with 3 arguments: [path] " +
                    "[interval_method p/d/r/a] [method_param]");
        }

        directoryPath = args[0];
        String method = args[1];
        double methodParam = Double.parseDouble(args[2]);
    }
}
