# VIRTUAL PRIVATE NETWORK (VPN)
It is a service that creates a secure and encrypted connection over a less secure network, typically the internet. This allows users to protect their online privacy and access resources remotely as if they were on a private network.

## ABSTRACT
The project signifies the use of multithreading and exception handling to make a basic functional VPN with out data encryption. The program blocks any IP address class whose octet is out of range of the given class set in the program ( For ex : only class B ip address when given, the rvu website will open after the VPN allows to connect the client and the server else not ). Although the program gives the flexibility to change the range to large or the most minute, the only required to be made is the octet number.

## FUNCTIONALITY
Both the codes simulate a VPN server that manages client connections and opens a specific website when the server address falls within a particular IP range.

## DIFFERENCE BTW THE 2 CODES
The primary difference lies in how exceptions are handled, one uses a custom "VPNException" and the other uses a "Throwable". "VPNException" is more robust and specific in its error handling, while "Throwable" is simpler and uses broader error handling with less robust main method error management.

## MODULES IMPLEMENTED
  01. Abstract classes
  02. Encapsulation
  03. Inheritance
  04. this operator-Aggregation 
  05. library:Desktop ,URI class ,Scanner 
  06. Interface 
  07. Polymorphism (Overriding)
  08. Access Modifiers 
  09. Mutlithreading
  10. Exception Handling(User defined ,Built in : Try and catch)
  11. Throwable
  12. Sychronization
  13. String Manipulation
  14. Instance variables and methods
  15. Static variables and methods 
  16. Constructors
