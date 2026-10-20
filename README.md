# Network Programming

All code from each TU857 Y3 network programming lab (CMPU3027)

## Lab[N]

All labs are in their own Java file under src/main/java/dev/petko/np/lab[N]

## Building

This project uses Maven as a baseline tool and also provides a Makefile to make things easier

- To build this using Maven, simply just run `mvn package`.
- To run a lab use: `java -cp ./target/dev.petko.np-1.0.jar dev.petko.np.lab[n].Main`

Or use the make file:

- Build: `make build`
- Run a Lab: `make run LAB=[n]`
