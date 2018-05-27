import java.util.*;
import java.io.*;

public class generator {
	
	public static void main(String[] args) {
		System.out.println("hi");
		writeFile();
	}
	
	private static void writeFile() {
		Scanner input = new Scanner(System.in);
		
		
		System.out.println("How many attributes would you like? (integers only)");
		String attributeNo = input.nextLine();
		while (attributeNo.matches("/^[a-zA-Z]+$/")) {
			System.out.println("incorrect input");
			System.out.println("How many attributes would you like? (integers only)");
			attributeNo = input.nextLine();
		}
		int attriNo = Integer.valueOf(attributeNo);
		
		
		System.out.println("what are these attributes?");
		String[] attribute = new String[attriNo];
		for (int i = 0; i < attriNo; i++) {
			int id = i+1;
			System.out.print("attribute "+id+" is: ");
			attribute[i] = input.nextLine();
			System.out.println();
		}
		System.out.println("What are the possible values for the following attributes?");
		String[] attriSize = new String[attriNo];
		
		for (int i = 0; i < attriNo; i++) {
			System.out.print("value for "+attribute[i]+" is: ");
			attriSize[i] = input.nextLine()+" ";
			while (true) {
				System.out.println("Do you want to add more?");
				if (input.nextLine().equals("n")) {
					break;
				}
				System.out.print("value for "+attribute[i]+" is: ");
				attriSize[i] += input.nextLine()+" ";
			}
		}
		try {
		FileWriter out = new FileWriter("generated-configuration.config");
		
		for (int i = 0;i < attriNo ; i++) {
			out.write(attribute[i] +" "+attriSize[i]+"\n");
		}
		out.write("\n");
			
		System.out.println("How many people do you want in this config file?");
		String peopleSize = input.nextLine();
		//requires error check here
		int pplSize = Integer.valueOf(peopleSize);
		
		System.out.println("The config file will be generated randomly");
		Random random = new Random();
		
		for (int i=0; i < pplSize;i++) {
			int id = i+1;
			out.write("P"+id+"\n");
			for (int j =0 ; j < attriNo;j++) {
				String[] words = attriSize[j].split(" ");
				int valueRand= random.nextInt((words.length));
				while (valueRand > words.length) {
					valueRand= random.nextInt((attriSize.length));
				}
				out.write(attribute[j]+" "+words[valueRand]+"\n");
			}
			out.write("\n");
		}
		
		out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}