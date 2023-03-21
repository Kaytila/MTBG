package net.ck.mtbg.playground;

import java.util.Scanner;

public class Circle2 {

public static void main(String[] args) {

    final double pi = 3.1416;
    Scanner values = new Scanner(System.in);
    char response;
    double area, perimeter, radius = 0; 

    do // put code in loop
    {
        // offer menu of options

        System.out.println();
        System.out.println("*** CIRCLE MENU ***"); // create a blank line
        System.out.println("[1] Set radius");
        System.out.println("[2] Display radius");
        System.out.println("[3] Display area");
        System.out.println("[4] Display perimeter");
        System.out.println("[5] Quit");
        System.out.println("Enter choice [1,2,3,4,5}: ");
        response = values.next().charAt(0); // get response
        System.out.println(); // create a blank line

        switch(response) // process response
        {
            case '1': System.out.println("Enter a value for the radius: ");
                      radius = values.nextDouble();
                        while(radius < 0)
                        {
                        System.out.println("Please enter a non-negative radius: ");
                        radius = values.nextDouble(); 
                        }
                      break;
            case '2': System.out.println("The radius is " + radius);
                      break;
            case '3': System.out.println("The area is " +  (pi * radius * radius));
                      break;
            case '4': System.out.println("The perimeter is " + (2 * pi * radius));
                      break;
            case '5': System.out.println("Goodbye!");
                      break;
            default:  System.out.println("Options 1-5 only!");
        }

    } while (response != '5'); // test for Quit option  
}   
}
