# Prova Finale Ingegneria del Software 2021-2022
## Gruppo AM25

-   Angelo Attivissimo ([@angelo7672](https://github.com/angelo7672)): angelo.attivissimo@mail.polimi.it
-   Ginevra Bozza ([@ginevra-bozza](https://github.com/ginevra-bozza)): ginevra.bozza@mail.polimi.it
-   Chiara Di Pasquale ([@chiaradipasquale](https://github.com/chiaradipasquale)): chiara2.dipasquale@mail.polimi.it

## Project Description
The project consists of a Java version of the board game Eriantys, by Cranio Creation <br>
Rules: https://www.craniocreations.it/wp-content/uploads/2021/11/Eriantys_ITA_bassa.pdf
### Eriantys
<img src="https://github.com/Angelo7672/ingsw2022-AM25/blob/main/src/main/resources/graphics/eriantys_banner.png" width=200px height=200px align="center" />

## How to run
#### Java version: 17 <br>
To run the jar via terminal, you need to go to the directory where the jar file is locate and use the command: java -jar AM25-1.0.jar <br>
Select 0 to start the Server, 1 to run the Client with CLI or GUI <br> <br>
Note: when starting the GUI, it will take some time to load 

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

