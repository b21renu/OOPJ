# VIRTUAL PRIVATE NETWORK (VPN)
It is a service that creates a secure and encrypted connection over a less secure network, typically the internet. This allows users to protect their online privacy and access resources remotely as if they were on a private network.

## FUNCTIONALITY
Both the codes simulates a VPN server that manages client connections and opens a specific website when the server address falls within a particular IP range.

## DIFFERENCE BTW THE 2 CODES
The primary difference lies in how exceptions are handled, one uses a custom "VPNException" and the other uses a "Throwable". "VPNException" is more robust and specific in its error handling, while "Throwable" is simpler and uses broader error handling with less robust main method error management.
