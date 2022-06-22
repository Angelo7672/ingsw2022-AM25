package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.View;

public class Printable {
    private final View view;
    private String ANSI_RESET = "\u001B[0m";
    private String ANSI_BLACK = "\u001B[30m";
    private String ANSI_RED = "\u001B[31m";
    private String ANSI_GREEN = "\u001B[32m";
    private String ANSI_YELLOW = "\u001B[33m";
    private String ANSI_BLUE = "\u001B[34m";
    private String ANSI_PURPLE = "\u001B[35m";
    private String ANSI_CYAN = "\u001B[36m";
    private String ANSI_WHITE = "\u001B[37m";
    private String  UNDERLINE = "\u001B[4m";

    public Printable(View view) {
        this.view = view;
    }

    protected void printIslandStudent(int i){
        System.out.println("\t"+"Island " + (i + 1) + ":" +"\t"+"     Students:" + ANSI_GREEN +" Green "+ view.getStudentsIsland(i)[0 ] + ANSI_RED  + ", Red " + view.getStudentsIsland(i)[1] +
                ANSI_YELLOW +", Yellow " + view.getStudentsIsland(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsIsland(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsIsland(i)[4]+ANSI_RESET);
    }
    protected void printTowersValue(int i){
        System.out.print("\t"+"Island "+(i+1)+": ");
        System.out.println("\t"+"\t"+"Island size: " + view.getIslandTowers(i));
        System.out.println();
    }
    protected void printTowersOwner(int i){
        System.out.print("New play: " + "\t"+"\t"+"Island "+(i+1)+": ");
        if(view.getTowersColor(i)==0) System.out.println("\t"+"    Towers Team: " + "WHITE" + ANSI_RESET);
        else if(view.getTowersColor(i)==1) System.out.println("\t"+"    Towers Team: " + ANSI_BLACK+ "BLACK" + ANSI_RESET);
        else if(view.getTowersColor(i)==2) System.out.println("\t"+"    Towers Team: " + ANSI_WHITE+ "GREY" + ANSI_RESET);
        else if(view.getTowersColor(i)==-1) System.out.println("\t"+"    Towers Team: NO ONE");
        System.out.println();
    }

    protected void printMotherNature(){
        System.out.println("New play: "+"\t"+"\t"+UNDERLINE+"Mother Nature"+ANSI_RESET+" is on island " + (view.getMotherPosition()+1));
        System.out.println();
    }

    protected void printNickname(int i){
        System.out.print("\t"+"School of " + view.getNickname(i)+": ");
    }
    protected void printEntrance(int i){
        System.out.println("\t"+" Entrance students:" + ANSI_GREEN + " Green " + view.getStudentsEntrance(i)[0] + ANSI_RED  + ", Red " + view.getStudentsEntrance(i)[1] +
                ANSI_YELLOW + ", Yellow " + view.getStudentsEntrance(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsEntrance(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsEntrance(i)[4]+ANSI_RESET);
    }
    protected void printTable(int i){
        System.out.println("\t"+" Table students:" + ANSI_GREEN + " Green " + view.getStudentsTable(i)[0] + ANSI_RED  + ", Red " + view.getStudentsTable(i)[1] +
                ANSI_YELLOW + ", Yellow " + view.getStudentsTable(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsTable(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsTable(i)[4]+ANSI_RESET);
    }
    protected void printProf(int i){
        System.out.print("New play: "+"\t");
        printNickname(i);
        System.out.println("\t"+" Professor: " + ANSI_GREEN + "Green "+ view.getProfessors(i)[0] + ANSI_RED  + ", Red " + view.getProfessors(i)[1] +
                ANSI_YELLOW + ", Yellow " + view.getProfessors(i)[2] +ANSI_PURPLE + ", Pink " + view.getProfessors(i)[3] + ANSI_BLUE+ ", Blue " + view.getProfessors(i)[4]+ANSI_RESET);
        System.out.println();
    }
    protected void printSchoolTowers(int i){
        System.out.println("\t" + "Towers number: " + view.getSchoolTowers(i) + ".");
        System.out.println();
    }
    protected void printCoins(int i){
        System.out.print("New play: ");
        printNickname(i);
        System.out.println("\t"+" Coins: " + view.getCoins(i));
        System.out.println();
    }

    protected void printCloud(){
        System.out.println(UNDERLINE+"CLOUDS:"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            if (view.getStudentsCloud(i)[0]+view.getStudentsCloud(i)[1]+view.getStudentsCloud(i)[2]+view.getStudentsCloud(i)[3]+view.getStudentsCloud(i)[4] != 0){
                System.out.println("\t"+"Cloud " + (i + 1) + ": Students:" +ANSI_GREEN + " Green " + view.getStudentsCloud(i)[0] + ANSI_RED + ", Red " + view.getStudentsCloud(i)[1] +
                        ANSI_YELLOW + ", Yellow " + view.getStudentsCloud(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsCloud(i)[3] + ANSI_BLUE+ ", Blue " + view.getStudentsCloud(i)[4]+ANSI_RESET);
            }
        }
        System.out.println();
    }

    protected void printLastCard(){
        System.out.println();
        System.out.print("New play: "+"\t"+UNDERLINE+"LAST PLAYED CARDS:"+ANSI_RESET+"\t" );
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print(" "+view.getNickname(i) + ": " + view.getLastCard(i) + ". ");
        }
        System.out.println();
        System.out.println();
    }

    protected void printTowersChange(int place, int componentRef){
        System.out.print("New play: " + "\t");
        switch (place) {
            case (0) -> {
                printNickname(componentRef);
                printSchoolTowers(componentRef);
            }
            case (1) -> printTowersValue(componentRef);
        }
        //System.out.println();
    }

    protected void printStudentsChange(int place, int componentRef){
        switch (place) {
            case (0) -> {
                System.out.print("New play: " + "\t");
                printNickname(componentRef);
                printEntrance(componentRef);
                System.out.println();
            }
            case (1) -> {
                System.out.print("New play: " + "\t");
                printNickname(componentRef);
                printTable(componentRef);
                System.out.println();
            }
            case (2) -> {
                System.out.print("New play: " + "\t");
                printIslandStudent(componentRef);
                System.out.println();
            }
        }
    }

    protected void printSpecialList(){
        System.out.println(UNDERLINE+"SPECIALS"+ANSI_RESET);
        for(int i=0; i<3; i++){
            System.out.print("\t"+"Special "+view.getSpecialName(i)+": Cost "+view.getSpecialCost(i));
            if(specialArray(view.getSpecialName(i))) System.out.println(". Students: "+ ANSI_GREEN +" Green "+ view.getSpecialStudents(i)[0] + ANSI_RED  + ", Red " + view.getSpecialStudents(i)[1] +
                    ANSI_YELLOW +", Yellow " + view.getSpecialStudents(i)[2] + ANSI_PURPLE + ", Pink " + view.getSpecialStudents(i)[3] + ANSI_BLUE + ", Blue " + view.getSpecialStudents(i)[4]+ANSI_RESET);
            else if(view.getSpecialName(i)==5) System.out.println(". No Entry tiles: "+view.getNoEntry(i));
            else System.out.println();
        }
        System.out.println();
    }
    protected void printSpecialStudents(int special){
        System.out.println();
        System.out.print("New play: "+"\t"+"\t");
        System.out.print("Special "+view.getSpecialName(special)+" students: "+view.getSpecialName(special));
        System.out.println("\t"+"\t"+"\t"+ ANSI_GREEN + " Green " + view.getSpecialStudents(special)[0] + ANSI_RED  + ", Red " + view.getSpecialStudents(special)[1] +
                ANSI_YELLOW + ", Yellow " + view.getSpecialStudents(special)[2] + ANSI_PURPLE + ", Pink " + view.getSpecialStudents(special)[3] + ANSI_BLUE + ", Blue " + view.getSpecialStudents(special)[4]+ANSI_RESET);
    }

    protected void cli(){

        System.out.println(System.lineSeparator().repeat(10));
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(UNDERLINE+"ISLANDS"+ANSI_RESET);
        for (int i = 0; i < view.getIslandSize(); i++) {
            System.out.println("\t"+"Island " + (i + 1) + ": Students:" + ANSI_GREEN +" Green "+ view.getStudentsIsland(i)[0 ] + ANSI_RED  + ", Red " + view.getStudentsIsland(i)[1] +
                    ANSI_YELLOW +", Yellow " + view.getStudentsIsland(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsIsland(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsIsland(i)[4]+ANSI_RESET);
            if (view.getInhibited(i) != 0) System.out.print("\t"+"\t"+"\t"+"  No Entry tiles: " + view.getInhibited(i) + ". ");
            if(view.getTowersColor(i)==0) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + "WHITE" + ANSI_RESET);
            else if(view.getTowersColor(i)==1) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + ANSI_BLACK+ "BLACK" + ANSI_RESET);
            else if(view.getTowersColor(i)==2) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + ANSI_WHITE+ "GREY" + ANSI_RESET);
            else if(view.getTowersColor(i)==-1) System.out.print("\t"+"\t"+"\t"+"  Towers Team: " + "NO ONE" + ANSI_RESET);
            System.out.println(". Island size: " + view.getIslandTowers(i));
        }
        System.out.println();
        System.out.println(UNDERLINE+"Mother Nature"+ANSI_RESET+" is on island " + (view.getMotherPosition()+1));//aggiungere
        System.out.println();

        System.out.println(UNDERLINE+"SCHOOLS"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print("\t" + "Nickname: " + view.getNickname(i) + ". Character: " + view.getCharacter(i) + ". Team: ");
            if (view.getTeam(i).equals("WHITE")) System.out.println(view.getTeam(i) + ".");
            if (view.getTeam(i).equals("BLACK")) System.out.println(ANSI_BLACK + view.getTeam(i) + "." + ANSI_RESET);
            if (view.getTeam(i).equals("GREY")) System.out.println(ANSI_WHITE + view.getTeam(i) + "." + ANSI_RESET);
            System.out.println("\t" + "Entrance students:" + ANSI_GREEN + " Green " + view.getStudentsEntrance(i)[0] + ANSI_RED + ", Red " + view.getStudentsEntrance(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getStudentsEntrance(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsEntrance(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsEntrance(i)[4] + ANSI_RESET);
            System.out.println("\t" + "Table students:" + ANSI_GREEN + " Green " + view.getStudentsTable(i)[0] + ANSI_RED + ", Red " + view.getStudentsTable(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getStudentsTable(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsTable(i)[3] + ANSI_BLUE + ", Blue " + view.getStudentsTable(i)[4] + ANSI_RESET);
            System.out.println("\t" + "Professor: " + ANSI_GREEN + "Green " + view.getProfessors(i)[0] + ANSI_RED + ", Red " + view.getProfessors(i)[1] +
                    ANSI_YELLOW + ", Yellow " + view.getProfessors(i)[2] + ANSI_PURPLE + ", Pink " + view.getProfessors(i)[3] + ANSI_BLUE + ", Blue " + view.getProfessors(i)[4] + ANSI_RESET);
            System.out.print("\t" + "Towers number: " + view.getSchoolTowers(i) + ".");
            if (view.getExpertMode()) System.out.println(" Coins: " + view.getCoins(i));
            else System.out.println();
            System.out.println();
        }

        System.out.println(UNDERLINE+"CLOUDS"+ANSI_RESET);
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            if (view.getStudentsCloud(i)[0]+view.getStudentsCloud(i)[1]+view.getStudentsCloud(i)[2]+view.getStudentsCloud(i)[3]+view.getStudentsCloud(i)[4] != 0){
                System.out.println("\t"+"Cloud " + (i + 1) + ": Students:" +ANSI_GREEN + " Green " + view.getStudentsCloud(i)[0] + ANSI_RED + ", Red " + view.getStudentsCloud(i)[1] +
                        ANSI_YELLOW + ", Yellow " + view.getStudentsCloud(i)[2] + ANSI_PURPLE + ", Pink " + view.getStudentsCloud(i)[3] + ANSI_BLUE+ ", Blue " + view.getStudentsCloud(i)[4]+ANSI_RESET);
            }
        }
        System.out.println();

        System.out.println(UNDERLINE+"LAST PLAYED CARDS"+ANSI_RESET);
        System.out.print("\t");
        for (int i = 0; i < view.getNumberOfPlayers(); i++) {
            System.out.print(view.getNickname(i) + ": " + view.getLastCard(i) + ". ");
        }
        System.out.println();
        System.out.println();

        System.out.println(UNDERLINE+"YOUR CARDS"+ANSI_RESET);
        for (int i = 0; i < view.getCards().size(); i++) {
            System.out.println("\t"+view.getCards().get(i).name()+" value: "+view.getCards().get(i).getValue()+
                    " movement: "+view.getCards().get(i).getMovement());
        }

        if(view.getExpertMode()){
            System.out.println();
            System.out.println(UNDERLINE+"SPECIALS"+ANSI_RESET);
            for(int i=0; i<3; i++){
                System.out.print("\t"+"Special "+view.getSpecialName(i)+": Cost "+view.getSpecialCost(i));
                if(specialArray(view.getSpecialName(i))) System.out.println(". Students: "+ ANSI_GREEN +" Green "+ view.getSpecialStudents(i)[0] + ANSI_RED  + ", Red " + view.getSpecialStudents(i)[1] +
                        ANSI_YELLOW +", Yellow " + view.getSpecialStudents(i)[2] + ANSI_PURPLE + ", Pink " + view.getSpecialStudents(i)[3] + ANSI_BLUE + ", Blue " + view.getSpecialStudents(i)[4]+ANSI_RESET);
                else if(view.getSpecialName(i)==5) System.out.println(". No Entry tiles: "+view.getNoEntry(i));
                else System.out.println();
            }
        }
    }

    private boolean specialArray(int name){
        if(name == 1) return true;
        if(name == 7) return true;
        if(name == 11) return true;
        return false;
    }
}