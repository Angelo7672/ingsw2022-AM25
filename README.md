# Prova Finale Ingegneria del Software 2021-2022
## Team AM25

-   Angelo Attivissimo ([@angelo7672](https://github.com/angelo7672)): angelo.attivissimo@mail.polimi.it
-   Ginevra Bozza ([@ginevra-bozza](https://github.com/ginevra-bozza)): ginevra.bozza@mail.polimi.it
-   Chiara Di Pasquale ([@chiaradipasquale](https://github.com/chiaradipasquale)): chiara2.dipasquale@mail.polimi.it

## Project Description
<img src="https://github.com/Angelo7672/ingsw2022-AM25/blob/main/src/main/resources/graphics/eriantys_banner2.jpg" width=300px height=162 px align="right" /> <br>
The project is the final test of the Software Engineering course held at Politecnico di Milano <br> 
**Final Score**: 30/30 cum laude <br> <br>
It consists of a Java version of the board game [*Eriantys*](https://www.craniocreations.it/prodotto/eriantys/), by CranioCreations (the rules of the game can be found [here](https://www.craniocreations.it/wp-content/uploads/2021/11/Eriantys_ITA_bassa.pdf)). <br>
This repository includes the source code of the game and of the unity tests, and the executable versione of the game (.jar), plus all the information regarding the implementation.

## How to run
#### Java version: 17 <br>
To run the jar via terminal, you need to go to the directory where the jar file is locate and use the following command: <br>
`java -jar AM25-1.0.jar` <br>
Make sure to have the correct Java version installed. <br> <br>
When the jar file is started, the user can select between Server and Client. It is necessary to start the Server first, then is possible to connect the Clients. A server supports from 2 to 4 clients. <br>
Select 0 to start the Server, 1 to run the Client with *CLI* (Command Line Interface) or *GUI* (Graphical Interface) <br> <br>
**Note**: when starting the GUI, it will take some time to load 

## Implemented functionalities

| Functionality                |                      State                         |
|:-----------------------------|:--------------------------------------------------:|
| Basic rules                  | 🟢 |
| Complete rules               | 🟢 |
| Socket                       | 🟢 |
| GUI                          | 🟢 |
| CLI                          | 🟢 |
| Special Characters           | 🟢 |
| 4-players Game               | 🟢 |
| Multiple Games               | 🔴 |
| Persistence                  | 🟢 |
| Resilience to disconnections | 🔴 |


🔴 not implemented <br>
🟢 implemented <br>
🟡 in progress <br>

## Tests coverage
| Package     |  Lines coverage  |  Classes coverage |
|:------------|:----------------:|:-----------------:|
| Model       |       83%        |       100%        |
| Controller  |       81%        |       91%         |


