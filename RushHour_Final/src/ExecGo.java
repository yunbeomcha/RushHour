import java.io.*;

public class ExecGo {

   public static String solve(Board B) {

      // Board to Serial
      char boardToSeiral[] = ".".repeat(36).toCharArray();
      char toChar;
      for (Piece piece : B.getPieces()) {
         if (piece.getSerial() == 'x') {
            toChar = 'x';
         } else {
            toChar = (char) (piece.getSerial() + 'A');
         }
         if(piece.getStride()==0) {
        	 for(int i=piece.getX1();i<=piece.getX2();i++) {
        		 boardToSeiral[piece.getY1() * B.getSize() + i] = toChar;
        	 }
         }
         else {
        	 for(int i=piece.getY1();i<=piece.getY2();i++) {
        		 boardToSeiral[i * B.getSize() + piece.getX1()] = toChar;
        	 }
         }
      }

      //System.out.println(boardToSeiral);

      String s = null;
      String result = "";
      String execUrl = "src/Solver/main.exe " + String.valueOf(boardToSeiral);

     // System.out.println(execUrl);
      try {
         // print a message
         //System.out.println("Executing python code");
         Process process = Runtime.getRuntime().exec(execUrl);
         BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

         BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

         // read the output from the command
         //System.out.println("python should be run.");
         while ((s = stdInput.readLine()) != null) {
            //System.out.println(s);
            result += s;
         }

         //System.out.println("Here is the standard error of the command (if any):\n");
         while ((s = stdError.readLine()) != null) {
            //System.out.println(s);
         }

//         System.exit(0);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(-1);
      }
      System.out.println(result);
      return result;
   }

   public static String getNextSolution(Board B) {
      return solve(B).substring(7, 10);
   }

   public static int puzzleMinCount(Board B) {
      String result = solve(B);
      int pointIndex = result.indexOf(']') + 2;
      try {
         return Integer.parseInt(result.substring(pointIndex, pointIndex + 3));
      } catch (Exception e) {
         return Integer.parseInt(result.substring(pointIndex, pointIndex + 2));
      }
   }
}