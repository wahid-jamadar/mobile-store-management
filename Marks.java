import java.util.Scanner;

public class Marks {
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter The Marks of Sub 1");
        int n1 = sc.nextInt();
        System.out.println("Enter The Marks of Sub 2");
        int n2 = sc.nextInt();
        System.out.println("Enter The Marks of Sub 3");
        int n3 = sc.nextInt();
        System.out.println("Enter The Marks of Sub 4");
        int n4 = sc.nextInt();
        System.out.println("Enter The Marks of Sub 5");
        int n5 = sc.nextInt();
        
        int total_marks = n1 + n2 + n3 + n4 + n5;
        System.out.println("The total marks of student out of 500 are " + total_marks);
        
        double percentage = ((double) total_marks / 500) * 100;
        
        System.out.println("The Percentage of student is " + percentage + "%");
        
        sc.close();
    }
}